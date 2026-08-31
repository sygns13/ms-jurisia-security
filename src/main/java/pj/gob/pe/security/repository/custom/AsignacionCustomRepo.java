package pj.gob.pe.security.repository.custom;

import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.model.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Consultas y escrituras sobre las tablas de relación AplicacionsHasUsers, UsersHasRoles
 * y RolesHasModulos. Las escrituras se hacen con SQL nativo y no con las colecciones
 * @ManyToMany de las entidades, porque UsersHasRoles tiene columnas de auditoría
 * (regDate, regDatetime, regTimestamp, regUserId) que JPA no puebla.
 */
public interface AsignacionCustomRepo {

    // Consultas
    List<Aplicacion> findAplicacionesByUsuario(Long userId);
    List<Role> findRolesByUsuario(Long userId);
    List<Modulo> findModulosByRol(Long roleId);
    List<Modulo> findModulosByAplicacion(Long aplicacionId);
    List<User> findUsuariosByAplicacion(Long aplicacionId);
    List<Modulo> findModulosEfectivosByUsuario(Long userId);

    /** Identificadores de los Roles del usuario que sí se propagan a Keycloak. */
    Set<Long> findRolesEfectivosByUsuario(Long userId);

    /** Nombres de los Módulos asociados a cada uno de los Roles indicados. */
    Map<Long, List<String>> findNombresModulosPorRol(List<Long> roleIds);

    // Aplicacion - Usuario
    boolean existeAplicacionUsuario(Long userId, Long aplicacionId);
    int asignarAplicacion(Long userId, Long aplicacionId);
    int quitarAplicacion(Long userId, Long aplicacionId);
    void reemplazarAplicaciones(Long userId, List<Long> aplicacionIds);

    // Rol - Usuario
    boolean existeRolUsuario(Long userId, Long roleId);
    int asignarRol(Long userId, Long roleId, Long sessionUserId);
    int quitarRol(Long userId, Long roleId);
    void reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId);

    // Modulo - Rol
    boolean existeModuloRol(Long roleId, Long moduloId);
    int asignarModuloARol(Long roleId, Long moduloId);
    int quitarModuloDeRol(Long roleId, Long moduloId);
    void reemplazarModulosDeRol(Long roleId, List<Long> moduloIds);
}
