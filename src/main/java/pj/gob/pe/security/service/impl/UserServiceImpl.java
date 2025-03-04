package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.security.dao.mysql.UserDAO;
import pj.gob.pe.security.exception.ValidationServiceException;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.service.UserService;
import pj.gob.pe.security.utils.Constantes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

        User userDB = userDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        userDB.setUpdDate(fechaActualTime.toLocalDate());
        userDB.setUpdDatetime(fechaActualTime);
        userDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        userDB.setUpdUserId(1L);
        //Oauth final

        userDB.setActivo(valor);

        userDAO.modificar(userDB);
    }

    @Override
    public Page<User> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("or_documento", buscar);
        filters.put("or_apellidos", buscar);
        filters.put("or_nombres", buscar);
        filters.put("or_username", buscar);
        filters.put("or_email", buscar);

        filters.put("dependencia.or_codigo", buscar);
        filters.put("dependencia.or_nombre", buscar);
        filters.put("dependencia.or_sigla", buscar);

        filters.put("cargo.or_codigo", buscar);
        filters.put("cargo.or_nombre", buscar);
        filters.put("cargo.or_sigla", buscar);

        filters.put("tipo_user.or_nombre", buscar);

        filters.put("borrado", 0);

        if(dependenciaId != null && dependenciaId > 0L){
            filters.put("dependencia.id", dependenciaId);
        }

        Map<String, Object> notEqualFilters = new HashMap<>();

        return userDAO.findUsersByFiltersPage(filters, notEqualFilters, pageable);
    }

    @Override
    public Page<User> listar(Pageable pageable, String buscar) throws Exception {
        return this.listar(pageable, buscar, null);
    }

    @Override
    public User registrar(User user) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        user.setRegDate(fechaActualTime.toLocalDate());
        user.setRegDatetime(fechaActualTime);
        user.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        user.setRegUserId(1L);
        //Oauth final

        user.setBorrado(Constantes.REGISTRO_NO_BORRADO);


        if(user.getDocumento() == null) user.setDocumento(Constantes.VOID_STRING);
        if(user.getApellidos() == null) user.setApellidos(Constantes.VOID_STRING);
        if(user.getNombres() == null) user.setNombres(Constantes.VOID_STRING);
        if(user.getUsername() == null) user.setUsername(Constantes.VOID_STRING);
        if(user.getPassword() == null) user.setPassword(Constantes.VOID_STRING);
        if(user.getEmail() == null) user.setEmail(Constantes.VOID_STRING);
        if(user.getTelefono() == null) user.setTelefono(Constantes.VOID_STRING);
        if(user.getDireccion() == null) user.setDireccion(Constantes.VOID_STRING);
        if(user.getActivo() == null) user.setActivo(Constantes.REGISTRO_ACTIVO);

        user.setDocumento(user.getDocumento().trim());
        user.setApellidos(user.getApellidos().trim());
        user.setNombres(user.getNombres().trim());
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setTelefono(user.getTelefono().trim());
        user.setDireccion(user.getDireccion().trim());


        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(user, resultValidacion);

        if(validacion) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return this.grabarRegistro(user);
        }

        String errorValidacion = "Error de validación Método Registrar User";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(User userEdit) throws Exception {

        User user = userDAO.listarPorId(userEdit.getId());

        user.setTipoDocumento(userEdit.getTipoDocumento());
        user.setDocumento(userEdit.getDocumento());
        user.setApellidos(userEdit.getApellidos());
        user.setNombres(userEdit.getNombres());
        user.setDependencia(userEdit.getDependencia());
        user.setCargo(userEdit.getCargo());
        user.setUsername(userEdit.getUsername());
        user.setPassword(userEdit.getPassword());
        user.setEmail(userEdit.getEmail());
        user.setTipoUser(userEdit.getTipoUser());
        user.setGenero(userEdit.getGenero());
        user.setTelefono(userEdit.getTelefono());
        user.setDireccion(userEdit.getDireccion());
        user.setActivo(userEdit.getActivo());

        LocalDateTime fechaActualTime = LocalDateTime.now();
        user.setUpdDate(fechaActualTime.toLocalDate());
        user.setUpdDatetime(fechaActualTime);
        user.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        user.setUpdUserId(1L);
        //Oauth final

        if(user.getDocumento() == null) user.setDocumento(Constantes.VOID_STRING);
        if(user.getApellidos() == null) user.setApellidos(Constantes.VOID_STRING);
        if(user.getNombres() == null) user.setNombres(Constantes.VOID_STRING);
        if(user.getUsername() == null) user.setUsername(Constantes.VOID_STRING);
        if(user.getPassword() == null) user.setPassword(Constantes.VOID_STRING);
        if(user.getEmail() == null) user.setEmail(Constantes.VOID_STRING);
        if(user.getTelefono() == null) user.setTelefono(Constantes.VOID_STRING);
        if(user.getDireccion() == null) user.setDireccion(Constantes.VOID_STRING);

        user.setDocumento(user.getDocumento().trim());
        user.setApellidos(user.getApellidos().trim());
        user.setNombres(user.getNombres().trim());
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setTelefono(user.getTelefono().trim());
        user.setDireccion(user.getDireccion().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(user, resultValidacion);

        if(validacion) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return this.grabarRectificar(user);
        }

        String errorValidacion = "Error de validación Método Modificar User";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<User> listar() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        return userDAO.findUsersByFiltersV2(filters, notEqualFilters);
    }

    @Override
    public User listarPorId(Long id) throws Exception {
        //return userDAO.listarPorId(id);

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<User> users = userDAO.findUsersByFiltersV2(filters, notEqualFilters);

        if(users.size() > 0)
            return users.get(0);
        else
            return null;

    }

    @Override
    public void eliminar(Long id) throws Exception {
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else {
            String errorValidacion = "Error de validación Método Eliminar User";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public User grabarRegistro(User user) throws Exception {
        return userDAO.registrar(user);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(User user) throws Exception {

        userDAO.modificar(user);
        return Constantes.CANTIDAD_UNIDAD_INTEGER;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) throws Exception {

        User userDB = userDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        userDB.setUpdDate(fechaActualTime.toLocalDate());
        userDB.setUpdDatetime(fechaActualTime);
        userDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        userDB.setUpdUserId(1L);
        //Oauth final

        userDB.setBorrado(Constantes.REGISTRO_BORRADO);

        userDAO.modificar(userDB);
    }

    @Override
    public boolean validacionRegistro(User user, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(user.getTipoDocumento() == null || user.getTipoDocumento() <= 0){
            resultado = false;
            error = "Seleccione el Tipo de Documento del Usuario";
            errors.add(error);
        }

        if(user.getDocumento() == null || user.getDocumento().isEmpty()){
            resultado = false;
            error = "Ingrese el Documento de Identidad del Usuario";
            errors.add(error);
        }

        if(user.getApellidos() == null || user.getApellidos().isEmpty()){
            resultado = false;
            error = "Ingrese los Apellidos del Usuario";
            errors.add(error);
        }

        if(user.getNombres() == null || user.getNombres().isEmpty()){
            resultado = false;
            error = "Ingrese los Nombres del Usuario";
            errors.add(error);
        }

        if(user.getDependencia() == null || user.getDependencia().getId() == null || user.getDependencia().getId() <= 0){
            resultado = false;
            error = "Seleccione la Dependencia del Usuario";
            errors.add(error);
        }

        if(user.getCargo() == null || user.getCargo().getId() == null || user.getCargo().getId() <= 0){
            resultado = false;
            error = "Seleccione el Cargo del Usuario";
            errors.add(error);
        }

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getEmail() == null || user.getEmail().isEmpty()){
            resultado = false;
            error = "Ingrese el Email del Usuario";
            errors.add(error);
        }

        if(user.getTipoUser() == null || user.getTipoUser().getId() == null || user.getTipoUser().getId() <= 0){
            resultado = false;
            error = "Seleccione el Tipo de Usuario";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("tipoDocumento", user.getTipoDocumento());
        filters.put("documento", user.getDocumento());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<User> users = userDAO.findUsersByFiltersV2(filters, notEqualFilters);

        if(!users.isEmpty()){
            resultado = false;
            error = "El Documento de Identidad del Usuario ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(User user, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(user.getId() == null){
            resultado = false;
            error = "Ingrese el Id del User";
            errors.add(error);
        }

        if(user.getTipoDocumento() == null || user.getTipoDocumento() <= 0){
            resultado = false;
            error = "Seleccione el Tipo de Documento del Usuario";
            errors.add(error);
        }

        if(user.getDocumento() == null || user.getDocumento().isEmpty()){
            resultado = false;
            error = "Ingrese el Documento de Identidad del Usuario";
            errors.add(error);
        }

        if(user.getApellidos() == null || user.getApellidos().isEmpty()){
            resultado = false;
            error = "Ingrese los Apellidos del Usuario";
            errors.add(error);
        }

        if(user.getNombres() == null || user.getNombres().isEmpty()){
            resultado = false;
            error = "Ingrese los Nombres del Usuario";
            errors.add(error);
        }

        if(user.getDependencia() == null || user.getDependencia().getId() == null || user.getDependencia().getId() <= 0){
            resultado = false;
            error = "Seleccione la Dependencia del Usuario";
            errors.add(error);
        }

        if(user.getCargo() == null || user.getCargo().getId() == null || user.getCargo().getId() <= 0){
            resultado = false;
            error = "Seleccione el Cargo del Usuario";
            errors.add(error);
        }

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getEmail() == null || user.getEmail().isEmpty()){
            resultado = false;
            error = "Ingrese el Email del Usuario";
            errors.add(error);
        }

        if(user.getTipoUser() == null || user.getTipoUser().getId() == null || user.getTipoUser().getId() <= 0){
            resultado = false;
            error = "Seleccione el Tipo de Usuario";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("tipoDocumento", user.getTipoDocumento());
        filters.put("documento", user.getDocumento());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();
        notEqualFilters.put("id", user.getId());

        List<User> users = userDAO.findUsersByFiltersV2(filters, notEqualFilters);

        if(!users.isEmpty()){
            resultado = false;
            error = "El Documento de Identidad del Usuario ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Producto

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}
