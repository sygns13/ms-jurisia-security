package pj.gob.pe.security.service.externals.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pj.gob.pe.security.configuration.ConfigProperties;
import pj.gob.pe.security.dao.mysql.UserDAO;
import pj.gob.pe.security.dao.redis.UserLoginRedisDao;
import pj.gob.pe.security.exception.AuthLoginException;
import pj.gob.pe.security.model.beans.ActiveSession;
import pj.gob.pe.security.model.beans.ResponseLogin;
import pj.gob.pe.security.model.beans.TokenResponse;
import pj.gob.pe.security.model.beans.UserLogin;
import pj.gob.pe.security.model.entities.*;
import pj.gob.pe.security.model.keycloak.UserAuth;
import pj.gob.pe.security.service.LoginService;
import pj.gob.pe.security.service.UserService;
import pj.gob.pe.security.service.externals.AuthService;
import pj.gob.pe.security.service.externals.SIJService;
import pj.gob.pe.security.service.keycloak.UsersServiceAuth;
import pj.gob.pe.security.utils.Constantes;
import pj.gob.pe.security.utils.NetworkUtils;
import pj.gob.pe.security.utils.beans.DataUsuarioDTO;
import pj.gob.pe.security.utils.beans.LoginInput;
import pj.gob.pe.security.utils.beans.LogoutInput;
import pj.gob.pe.security.utils.beans.VerifySessionInput;

import java.util.*;


@Service
public class AuthServiceImpl implements AuthService {

    private final RestClient restClient;
    private final UserDAO userDAO;
    private final ConfigProperties properties;
    private final LoginService loginService;
    private final UserLoginRedisDao userLoginRedisDao;
    private final UsersServiceAuth userService;
    private final SIJService sIJService;
    private final UserService userServiceCRUD;

    public AuthServiceImpl(RestClient.Builder builder, UserDAO userDAO, ConfigProperties properties,LoginService loginService, UserLoginRedisDao userLoginRedisDao, UsersServiceAuth userService, SIJService sIJService, UserService userServiceCRUD) {
        //this.restClient = builder.baseUrl("http://localhost:8080").build();
        this.userDAO = userDAO;
        this.properties = properties;
        this.restClient = builder.baseUrl(properties.getUrlAuthorization()).build();
        this.loginService = loginService;
        this.userLoginRedisDao = userLoginRedisDao;
        this.userService = userService;
        this.sIJService = sIJService;
        this.userServiceCRUD = userServiceCRUD;
    }

    @Override
    public TokenResponse authenticate(LoginInput login) throws Exception {

        //inicio - Consulta Datos SIJ
        UserAuth userAuth = userService.getUserDetails(login.getUsername());
        if(userAuth == null || userAuth.getUserId() == null){
            DataUsuarioDTO dataUsuarioSIJ = sIJService.getDatosUser(login.getUsername(), login.getPassword());

            if(dataUsuarioSIJ != null){
                User user = new User();
                user.setTipoDocumento(Constantes.TIPO_DOCUMENTO_DNI);
                user.setDocumento(dataUsuarioSIJ.getDni());
                user.setApellidos(dataUsuarioSIJ.getApePaterno() + " " + dataUsuarioSIJ.getApeMaterno());
                user.setNombres(dataUsuarioSIJ.getNombres());

                Dependencia dependencia = new Dependencia();
                dependencia.setId(Constantes.CANTIDAD_UNIDAD_LONG);
                user.setDependencia(dependencia);

                Cargo cargo = new Cargo();
                cargo.setId(Constantes.CANTIDAD_UNIDAD_LONG);
                user.setCargo(cargo);

                user.setUsername(dataUsuarioSIJ.getUsuario());
                user.setPassword(dataUsuarioSIJ.getDni());
                user.setEmail("mail_"+dataUsuarioSIJ.getDni()+"@gmail.com");

                TipoUser tipoUser = new TipoUser();
                tipoUser.setId(Constantes.TIPO_USER_USUARIO);
                user.setTipoUser(tipoUser);
                user.setActivo(Constantes.REGISTRO_ACTIVO);

                Role role1 = new Role();
                role1.setId(1L);

                Role role2 = new Role();
                role2.setId(2L);

                Role role3 = new Role();
                role3.setId(3L);

                Role role4 = new Role();
                role4.setId(4L);

                List<Role> roles = new ArrayList<>();

                roles.add(role1);
                roles.add(role2);
                roles.add(role3);
                roles.add(role4);

                user.setRoles(roles);

                userServiceCRUD.registrar(user);
            }
        }
        //fin - Consulta Datos SIJ

        //String url = "/realms/jurisia/protocol/openid-connect/token";
        // Construcción del cuerpo con Form-UrlEncoded
        String body = UriComponentsBuilder.newInstance()
                .queryParam("grant_type", login.getGrantType())
                .queryParam("client_id", login.getClientId())
                .queryParam("client_secret", login.getClientSecret())
                .queryParam("username", login.getUsername())
                .queryParam("password", login.getPassword())
                .build()
                .encode()
                .toString()
                .substring(1); // Elimina el "?" inicial

        // Realizar la petición con RestClient y mapear la respuesta directamente a TokenResponse
        return restClient.post()
                .uri(properties.getPathLogin())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthLoginException("Error de Credenciales de inicio de sesión o cuenta desabilitada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(TokenResponse.class); // Se convierte automáticamente el JSON a la clase Java
    }

    @Override
    public ResponseLogin generateSessionId(String username, TokenResponse token, String clientIp) throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("username", username);
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<User> users = userDAO.findUsersByFiltersV2(filters, notEqualFilters);
        User userEntity;

        if(users.size() > 0)
            userEntity = users.get(0);
        else
            throw new AuthLoginException("Error de Credenciales de inicio de sesión o cuenta desabilitada");


        ResponseLogin responseLogin = new ResponseLogin();
        UserLogin userLogin = new UserLogin();

        userLogin.setUserSessionsId(UUID.randomUUID().toString());
        userLogin.setIdUser(userEntity.getId());
        userLogin.setTipoDocumento(userEntity.getTipoDocumento());
        userLogin.setDocumento(userEntity.getDocumento());
        userLogin.setApellidos(userEntity.getApellidos());
        userLogin.setNombres(userEntity.getNombres());
        userLogin.setUsername(userEntity.getUsername());
        userLogin.setEmail(userEntity.getEmail());
        userLogin.setGenero(userEntity.getGenero());
        userLogin.setTelefono(userEntity.getTelefono());
        userLogin.setDireccion(userEntity.getDireccion());
        userLogin.setActivo(userEntity.getActivo());
        userLogin.setIdDependencia(userEntity.getDependencia() != null ? userEntity.getDependencia().getId() : 0L);
        userLogin.setNombreDependencia(userEntity.getDependencia() != null ? userEntity.getDependencia().getNombre() : "");
        userLogin.setCodigoDependencia(userEntity.getDependencia() != null ? userEntity.getDependencia().getCodigo() : "");
        userLogin.setSiglaDependencia(userEntity.getDependencia() != null ? userEntity.getDependencia().getSigla() : "");
        userLogin.setIdCargo(userEntity.getCargo() != null ? userEntity.getCargo().getId() : 0L);
        userLogin.setNombreCargo(userEntity.getCargo() != null ? userEntity.getCargo().getNombre() : "");
        userLogin.setCodigoCargo(userEntity.getCargo() != null ? userEntity.getCargo().getCodigo() : "");
        userLogin.setSiglaCargo(userEntity.getCargo() != null ? userEntity.getCargo().getSigla() : "");
        userLogin.setIdTipoUser(userEntity.getTipoUser() != null ? userEntity.getTipoUser().getId() : 0L);
        userLogin.setTipoUser(userEntity.getTipoUser() != null ? userEntity.getTipoUser().getNombre() : "");

        userLogin.setToken(token);

        responseLogin.setSuccess(true);
        responseLogin.setItemFound(true);
        responseLogin.setUser(userLogin);

        //Guardar Sesion en Redis
        userLoginRedisDao.saveUserSession("userLogin_"+userLogin.getUserSessionsId(), userLogin);

        //Guardar Login
        Login login = new Login();
        login.setUser(userEntity);
        login.setIp(clientIp);
        login.setMac(NetworkUtils.getMacAddress(clientIp));
        loginService.registrar(login);

        return responseLogin;

    }

    @Override
    public ResponseLogin sesionData(String userSessionsId) throws Exception {

        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setSuccess(false);
        responseLogin.setItemFound(false);

        UserLogin userLogin = userLoginRedisDao.getUserSession("userLogin_"+userSessionsId);

        if(userLogin != null && userLogin.getIdUser() != null){
            responseLogin.setSuccess(true);
            responseLogin.setItemFound(true);
            responseLogin.setUser(userLogin);
        }
        return responseLogin;
    }

    @Override
    public ResponseLogin verifySession(VerifySessionInput verifySession) {

        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setSuccess(false);
        responseLogin.setItemFound(false);

        UserLogin userLogin = userLoginRedisDao.getUserSession("userLogin_"+verifySession.getUserSessionsId());

        //Verifi Session in redis
        if(userLogin != null && userLogin.getIdUser() != null){
            responseLogin.setUser(userLogin);
            responseLogin.setItemFound(true);
        } else {
            return responseLogin;
        }

        //Verify Active Session
        ActiveSession activeSession = this.activeSession(verifySession);

        if(activeSession.isActive()){
            responseLogin.setSuccess(true);
        }

        return responseLogin;
    }

    private ActiveSession activeSession(VerifySessionInput verifySession) {
        //String url = "/realms/jurisia/protocol/openid-connect/token";
        // Construcción del cuerpo con Form-UrlEncoded
        String body = UriComponentsBuilder.newInstance()
                .queryParam("client_secret", verifySession.getClientSecret())
                .queryParam("client_id", verifySession.getClientId())
                .queryParam("username", verifySession.getUsername())
                .queryParam("token", verifySession.getToken())
                .build()
                .encode()
                .toString()
                .substring(1); // Elimina el "?" inicial

        // Realizar la petición con RestClient y mapear la respuesta directamente a TokenResponse
        return restClient.post()
                .uri(properties.getPathIntrospectToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthLoginException("Error de Credenciales de inicio de sesión o cuenta desabilitada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(ActiveSession.class); // Se convierte automáticamente el JSON a la clase Java
    }

    @Override
    public void logout(LogoutInput logout) {

        //Delete Session Redis
        userLoginRedisDao.deleteUserSession("userLogin_"+logout.getUserSessionsId());

        //Delete Session Keycloak
        String body = UriComponentsBuilder.newInstance()
                .queryParam("client_secret", logout.getClientSecret())
                .queryParam("client_id", logout.getClientId())
                .queryParam("refresh_token", logout.getRefreshToken())
                .build()
                .encode()
                .toString()
                .substring(1); // Elimina el "?" inicial

        restClient.post()
                .uri(properties.getPathLogout())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + logout.getAccessToken()) // Opcional en Keycloak
                .body(body)
                .retrieve()
                .toBodilessEntity();

    }

}
