package pj.gob.pe.security.dao.mysql;

import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.model.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Acceso a las tablas de relacion AplicacionsHasUsers, UsersHasRoles y RolesHasModulos.
 * No extiende GenericDAO porque estas tablas no tienen entidad propia ni clave simple.
 */
public interface AsignacionDAO {

    List<Aplicacion> listarAplicacionesDeUsuario(Long userId) throws Exception;
    List<Role> listarRolesDeUsuario(Long userId) throws Exception;
    List<Modulo> listarModulosDeRol(Long roleId) throws Exception;
    List<Modulo> listarModulosDeAplicacion(Long aplicacionId) throws Exception;
    List<User> listarUsuariosDeAplicacion(Long aplicacionId) throws Exception;
    List<Modulo> listarModulosEfectivosDeUsuario(Long userId) throws Exception;

    Set<Long> listarRolesEfectivosDeUsuario(Long userId) throws Exception;
    Map<Long, List<String>> listarNombresModulosPorRol(List<Long> roleIds) throws Exception;

    boolean existeAplicacionUsuario(Long userId, Long aplicacionId) throws Exception;
    int asignarAplicacion(Long userId, Long aplicacionId) throws Exception;
    int quitarAplicacion(Long userId, Long aplicacionId) throws Exception;
    void reemplazarAplicaciones(Long userId, List<Long> aplicacionIds) throws Exception;

    boolean existeRolUsuario(Long userId, Long roleId) throws Exception;
    int asignarRol(Long userId, Long roleId, Long sessionUserId) throws Exception;
    int quitarRol(Long userId, Long roleId) throws Exception;
    void reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId) throws Exception;

    boolean existeModuloRol(Long roleId, Long moduloId) throws Exception;
    int asignarModuloARol(Long roleId, Long moduloId) throws Exception;
    int quitarModuloDeRol(Long roleId, Long moduloId) throws Exception;
    void reemplazarModulosDeRol(Long roleId, List<Long> moduloIds) throws Exception;
}
