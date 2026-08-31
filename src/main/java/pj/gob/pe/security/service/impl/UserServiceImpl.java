package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.security.dao.mysql.AplicacionDAO;
import pj.gob.pe.security.dao.mysql.AsignacionDAO;
import pj.gob.pe.security.dao.mysql.RoleDAO;
import pj.gob.pe.security.dao.mysql.UserDAO;
import pj.gob.pe.security.exception.ValidationServiceException;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.utils.beans.ResponseRolUsuario;
import pj.gob.pe.security.service.UserService;
import pj.gob.pe.security.utils.Constantes;
import pj.gob.pe.security.utils.InputConsultaIA;
import pj.gob.pe.security.utils.UserIdsRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final AsignacionDAO asignacionDAO;
    private final RoleDAO roleDAO;
    private final AplicacionDAO aplicacionDAO;

    @Override
    public void altabaja(Long id, Integer valor) throws Exception {
        this.altabaja(id, valor, Constantes.USUARIO_SISTEMA_ID);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor, Long userId) throws Exception {

        User userDB = userDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        userDB.setUpdDate(fechaActualTime.toLocalDate());
        userDB.setUpdDatetime(fechaActualTime);
        userDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        userDB.setUpdUserId(userId);
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

        //filters.put("cargo.or_codigo", buscar);
        //filters.put("cargo.or_nombre", buscar);
        //filters.put("cargo.or_sigla", buscar);

        filters.put("tipo_user.or_nombre", buscar);

        filters.put("borrado", 0);

        if(dependenciaId != null && dependenciaId > 0L){
            filters.put("dependencia.id", dependenciaId);
        }

        Map<String, Object> notEqualFilters = new HashMap<>();

        return userDAO.findUsersByFiltersPage(filters, notEqualFilters, pageable);
    }

    @Override
    public List<User> buscarUsuarios(InputConsultaIA inputConsultaIA) throws Exception {
        Map<String, Object> filters = new HashMap<>();
        Boolean hacerBusqueda = false;

        if(inputConsultaIA.getDocumento() != null && !inputConsultaIA.getDocumento().isEmpty()) {
            filters.put("like_documento", inputConsultaIA.getDocumento().trim());
            hacerBusqueda = true;
        }

        if(inputConsultaIA.getApellidos() != null && !inputConsultaIA.getApellidos().isEmpty()) {
            filters.put("like_apellidos", inputConsultaIA.getApellidos());
            hacerBusqueda = true;
        }

        if(inputConsultaIA.getNombres() != null && !inputConsultaIA.getNombres().isEmpty()) {
            filters.put("like_nombres", inputConsultaIA.getNombres());
            hacerBusqueda = true;
        }

        if(inputConsultaIA.getUsername() != null && !inputConsultaIA.getUsername().isEmpty()) {
            filters.put("like_username", inputConsultaIA.getUsername());
            hacerBusqueda = true;
        }

        if(inputConsultaIA.getEmail() != null && !inputConsultaIA.getEmail().isEmpty()) {
            filters.put("like_email", inputConsultaIA.getEmail());
            hacerBusqueda = true;
        }

        if(!hacerBusqueda) {
            List<User> users = new ArrayList<>();
            return users;
        }

        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        return userDAO.findUsersByFiltersV2(filters, notEqualFilters);
    }

    @Override
    public Page<User> listar(Pageable pageable, String buscar) throws Exception {
        return this.listar(pageable, buscar, null);
    }

    @Override
    public User registrar(User user) throws Exception {
        return this.registrar(user, Constantes.USUARIO_SISTEMA_ID);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public User registrar(User user, Long userId) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        user.setRegDate(fechaActualTime.toLocalDate());
        user.setRegDatetime(fechaActualTime);
        user.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        user.setRegUserId(userId);
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
        if(user.getCargo() == null) user.setCargo(Constantes.VOID_STRING);
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

            // Los Roles no se persisten por la colección @ManyToMany porque UsersHasRoles
            // lleva auditoría; se apartan y se graban luego por la misma ruta que usan
            // los endpoints de asignación.
            List<Role> rolesSolicitados = user.getRoles();
            user.setRoles(null);

            User userRegistrado = this.grabarRegistro(user);

            if(rolesSolicitados != null && !rolesSolicitados.isEmpty()){
                List<Long> roleIds = new ArrayList<>();
                rolesSolicitados.forEach(rol -> {
                    if(rol != null && rol.getId() != null) roleIds.add(rol.getId());
                });

                for (Long roleId : roleIds) {
                    this.validarRol(roleId);
                }

                asignacionDAO.reemplazarRoles(userRegistrado.getId(), roleIds, userId);
            }

            return userRegistrado;
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
        return this.modificar(userEdit, Constantes.USUARIO_SISTEMA_ID);
    }

    @Override
    public int modificar(User userEdit, Long userId) throws Exception {

        User user = userDAO.listarPorId(userEdit.getId());

        user.setTipoDocumento(userEdit.getTipoDocumento());
        user.setDocumento(userEdit.getDocumento());
        user.setApellidos(userEdit.getApellidos());
        user.setNombres(userEdit.getNombres());
        user.setDependencia(userEdit.getDependencia());
        user.setCargo(userEdit.getCargo());
        user.setUsername(userEdit.getUsername());
        user.setEmail(userEdit.getEmail());
        user.setTipoUser(userEdit.getTipoUser());
        user.setGenero(userEdit.getGenero());
        user.setTelefono(userEdit.getTelefono());
        user.setDireccion(userEdit.getDireccion());
        user.setCargo(userEdit.getCargo());
        user.setActivo(userEdit.getActivo());

        LocalDateTime fechaActualTime = LocalDateTime.now();
        user.setUpdDate(fechaActualTime.toLocalDate());
        user.setUpdDatetime(fechaActualTime);
        user.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        user.setUpdUserId(userId);
        //Oauth final

        if(user.getDocumento() == null) user.setDocumento(Constantes.VOID_STRING);
        if(user.getApellidos() == null) user.setApellidos(Constantes.VOID_STRING);
        if(user.getNombres() == null) user.setNombres(Constantes.VOID_STRING);
        if(user.getUsername() == null) user.setUsername(Constantes.VOID_STRING);
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

        // El password solo se reemplaza si el cliente envia uno nuevo; en caso
        // contrario se conserva el que ya tiene el usuario en la base de datos.
        String passwordNuevo = userEdit.getPassword();
        boolean cambiaPassword = passwordNuevo != null && !passwordNuevo.trim().isEmpty();

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(user, resultValidacion);

        if(validacion) {
            if(cambiaPassword) {
                user.setPassword(passwordEncoder.encode(passwordNuevo.trim()));
            }
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
        this.eliminar(id, Constantes.USUARIO_SISTEMA_ID);
    }

    @Override
    public void eliminar(Long id, Long userId) throws Exception {
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id, userId);
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

    @Override
    public void grabarEliminar(Long id) throws Exception {
        this.grabarEliminar(id, Constantes.USUARIO_SISTEMA_ID);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id, Long userId) throws Exception {

        User userDB = userDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        userDB.setUpdDate(fechaActualTime.toLocalDate());
        userDB.setUpdDatetime(fechaActualTime);
        userDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        userDB.setUpdUserId(userId);
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

        /*
        if(user.getCargo() == null || user.getCargo().getId() == null || user.getCargo().getId() <= 0){
            resultado = false;
            error = "Seleccione el Cargo del Usuario";
            errors.add(error);
        }*/
        if(user.getCargo() == null || user.getCargo().isEmpty()){
            resultado = false;
            error = "Ingrese el Cargo del Usuario";
            errors.add(error);
        }

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            resultado = false;
            error = "Ingrese el Password del Usuario";
            errors.add(error);
        }

        /*
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            resultado = false;
            error = "Ingrese el Email del Usuario";
            errors.add(error);
        }
         */

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

        /*
        if(user.getCargo() == null || user.getCargo().getId() == null || user.getCargo().getId() <= 0){
            resultado = false;
            error = "Seleccione el Cargo del Usuario";
            errors.add(error);
        }*/
        if(user.getCargo() == null || user.getCargo().isEmpty()){
            resultado = false;
            error = "Ingrese el Cargo del Usuario";
            errors.add(error);
        }

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            resultado = false;
            error = "Ingrese el Username del Usuario";
            errors.add(error);
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            resultado = false;
            error = "Ingrese el Password del Usuario";
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

    @Override
    public List<User> getUsersByIds(UserIdsRequest inputUsers) throws Exception {
        return userDAO.getUsersByIds(inputUsers);
    }

    // =====================================================================
    // Aplicaciones del Usuario (AplicacionsHasUsers)
    // =====================================================================

    @Override
    public List<Aplicacion> listarAplicaciones(Long userId) throws Exception {
        return asignacionDAO.listarAplicacionesDeUsuario(userId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void asignarAplicacion(Long userId, Long aplicacionId) throws Exception {
        this.validarAplicacion(aplicacionId);

        if(asignacionDAO.existeAplicacionUsuario(userId, aplicacionId)){
            throw new ValidationServiceException("La Aplicación ya se encuentra asignada al Usuario");
        }

        asignacionDAO.asignarAplicacion(userId, aplicacionId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void quitarAplicacion(Long userId, Long aplicacionId) throws Exception {
        if(!asignacionDAO.existeAplicacionUsuario(userId, aplicacionId)){
            throw new ValidationServiceException("La Aplicación no se encuentra asignada al Usuario");
        }

        asignacionDAO.quitarAplicacion(userId, aplicacionId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<Aplicacion> reemplazarAplicaciones(Long userId, List<Long> aplicacionIds) throws Exception {
        List<Long> ids = aplicacionIds != null ? aplicacionIds : new ArrayList<>();

        for (Long aplicacionId : ids) {
            this.validarAplicacion(aplicacionId);
        }

        asignacionDAO.reemplazarAplicaciones(userId, ids);

        return asignacionDAO.listarAplicacionesDeUsuario(userId);
    }

    private void validarAplicacion(Long aplicacionId) throws Exception {
        if(aplicacionId == null){
            throw new ValidationServiceException("Seleccione la Aplicación");
        }

        Aplicacion aplicacion = aplicacionDAO.listarPorId(aplicacionId);

        if(aplicacion == null){
            throw new ValidationServiceException("La Aplicación con ID " + aplicacionId + " no existe");
        }
    }

    // =====================================================================
    // Roles del Usuario (UsersHasRoles)
    // =====================================================================

    @Override
    public List<ResponseRolUsuario> listarRoles(Long userId) throws Exception {
        List<Role> roles = asignacionDAO.listarRolesDeUsuario(userId);

        List<Long> roleIds = new ArrayList<>();
        roles.forEach(rol -> roleIds.add(rol.getId()));

        Set<Long> efectivos = asignacionDAO.listarRolesEfectivosDeUsuario(userId);
        Map<Long, List<String>> modulosPorRol = asignacionDAO.listarNombresModulosPorRol(roleIds);

        List<ResponseRolUsuario> respuesta = new ArrayList<>();

        for (Role rol : roles) {
            ResponseRolUsuario item = new ResponseRolUsuario();
            item.setId(rol.getId());
            item.setName(rol.getName());
            item.setDescripcion(rol.getDescripcion());

            List<String> modulos = modulosPorRol.getOrDefault(rol.getId(), new ArrayList<>());
            item.setModulos(modulos);

            boolean efectivo = efectivos.contains(rol.getId());
            item.setEfectivoEnKeycloak(efectivo);

            if(!efectivo){
                if(modulos.isEmpty()){
                    item.setAdvertencia("El Rol no está asociado a ningún Módulo, por lo que no se propaga a Keycloak. " +
                            "Asocie el Rol a un Módulo en /v1/roles/" + rol.getId() + "/modulos");
                } else {
                    item.setAdvertencia("El Rol está asociado a Módulos que no pertenecen a ninguna de las Aplicaciones " +
                            "del Usuario, por lo que no se propaga a Keycloak");
                }
            }

            respuesta.add(item);
        }

        return respuesta;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<ResponseRolUsuario> asignarRol(Long userId, Long roleId, Long sessionUserId) throws Exception {
        this.validarRol(roleId);

        if(asignacionDAO.existeRolUsuario(userId, roleId)){
            throw new ValidationServiceException("El Rol ya se encuentra asignado al Usuario");
        }

        asignacionDAO.asignarRol(userId, roleId, sessionUserId);

        return this.listarRoles(userId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<ResponseRolUsuario> quitarRol(Long userId, Long roleId) throws Exception {
        if(!asignacionDAO.existeRolUsuario(userId, roleId)){
            throw new ValidationServiceException("El Rol no se encuentra asignado al Usuario");
        }

        asignacionDAO.quitarRol(userId, roleId);

        return this.listarRoles(userId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<ResponseRolUsuario> reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId) throws Exception {
        List<Long> ids = roleIds != null ? roleIds : new ArrayList<>();

        for (Long roleId : ids) {
            this.validarRol(roleId);
        }

        asignacionDAO.reemplazarRoles(userId, ids, sessionUserId);

        return this.listarRoles(userId);
    }

    private void validarRol(Long roleId) throws Exception {
        if(roleId == null){
            throw new ValidationServiceException("Seleccione el Rol");
        }

        Role rol = roleDAO.listarPorId(roleId);

        if(rol == null){
            throw new ValidationServiceException("El Rol con ID " + roleId + " no existe");
        }
    }

    // =====================================================================
    // Modulos a los que el Usuario llega realmente
    // =====================================================================

    @Override
    public List<Modulo> listarModulosEfectivos(Long userId) throws Exception {
        return asignacionDAO.listarModulosEfectivosDeUsuario(userId);
    }

    // =====================================================================
    // Contraseña
    // =====================================================================

    /** Restablecimiento administrativo: no exige la contraseña anterior. */
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void cambiarPassword(Long userId, String passwordNuevo, Long sessionUserId) throws Exception {

        User user = userDAO.listarPorId(userId);

        if(user == null || Constantes.REGISTRO_BORRADO.equals(user.getBorrado())){
            throw new ValidationServiceException("El Usuario con ID " + userId + " no existe");
        }

        this.aplicarPassword(user, passwordNuevo, sessionUserId);
    }

    /** Autoservicio: el dueño de la sesión cambia su propia contraseña verificando la actual. */
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void cambiarPasswordPropio(Long sessionUserId, String passwordActual, String passwordNuevo) throws Exception {

        User user = userDAO.listarPorId(sessionUserId);

        if(user == null || Constantes.REGISTRO_BORRADO.equals(user.getBorrado())){
            throw new ValidationServiceException("El Usuario de la sesión no existe");
        }

        if(passwordActual == null || passwordActual.trim().isEmpty()){
            throw new ValidationServiceException("Ingrese su contraseña actual");
        }

        if(user.getPassword() == null || !passwordEncoder.matches(passwordActual, user.getPassword())){
            throw new ValidationServiceException("La contraseña actual no es correcta");
        }

        this.aplicarPassword(user, passwordNuevo, sessionUserId);
    }

    /** Encripta la nueva contraseña y deja la traza de edición del registro. */
    private void aplicarPassword(User user, String passwordNuevo, Long sessionUserId) throws Exception {

        if(passwordNuevo == null || passwordNuevo.trim().isEmpty()){
            throw new ValidationServiceException("Ingrese la nueva contraseña del Usuario");
        }

        String passwordLimpio = passwordNuevo.trim();

        LocalDateTime fechaActualTime = LocalDateTime.now();
        user.setUpdDate(fechaActualTime.toLocalDate());
        user.setUpdDatetime(fechaActualTime);
        user.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        user.setUpdUserId(sessionUserId);
        //Oauth final

        user.setPassword(passwordEncoder.encode(passwordLimpio));

        userDAO.modificar(user);
    }
}
