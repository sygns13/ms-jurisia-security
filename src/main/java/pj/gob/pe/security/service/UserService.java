package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.utils.beans.ResponseRolUsuario;
import pj.gob.pe.security.utils.InputConsultaIA;
import pj.gob.pe.security.utils.UserIdsRequest;

import java.util.List;

public interface UserService extends GeneralService<User, Long>{

    void altabaja(Long id, Integer valor) throws Exception;

    void altabaja(Long id, Integer valor, Long userId) throws Exception;

    Page<User> listar(Pageable pageable, String buscar) throws Exception;
    Page<User> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception;

    User registrar(User user, Long userId) throws Exception;

    int modificar(User user, Long userId) throws Exception;

    void eliminar(Long id, Long userId) throws Exception;

    void grabarEliminar(Long id, Long userId) throws Exception;

    List<User> buscarUsuarios(InputConsultaIA inputConsultaIA) throws Exception;

    List<User> getUsersByIds(UserIdsRequest inputUsers) throws Exception;

    // ---- Aplicaciones del Usuario (AplicacionsHasUsers) ----
    List<Aplicacion> listarAplicaciones(Long userId) throws Exception;
    void asignarAplicacion(Long userId, Long aplicacionId) throws Exception;
    void quitarAplicacion(Long userId, Long aplicacionId) throws Exception;
    List<Aplicacion> reemplazarAplicaciones(Long userId, List<Long> aplicacionIds) throws Exception;

    // ---- Roles del Usuario (UsersHasRoles) ----
    List<ResponseRolUsuario> listarRoles(Long userId) throws Exception;
    List<ResponseRolUsuario> asignarRol(Long userId, Long roleId, Long sessionUserId) throws Exception;
    List<ResponseRolUsuario> quitarRol(Long userId, Long roleId) throws Exception;
    List<ResponseRolUsuario> reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId) throws Exception;

    // ---- Contraseña ----
    void cambiarPassword(Long userId, String passwordNuevo, Long sessionUserId) throws Exception;
    void cambiarPasswordPropio(Long sessionUserId, String passwordActual, String passwordNuevo) throws Exception;

    // ---- Modulos a los que el Usuario llega realmente ----
    List<Modulo> listarModulosEfectivos(Long userId) throws Exception;
}
