package pj.gob.pe.security.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.repository.custom.AsignacionCustomRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class AsignacionCustomRepoImpl implements AsignacionCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    // ------------------------------------------------------------------ Consultas

    @Override
    public List<Aplicacion> findAplicacionesByUsuario(Long userId) {
        return entityManager.createQuery(
                        "SELECT a FROM Aplicacion a JOIN a.users u WHERE u.id = :userId ORDER BY a.id", Aplicacion.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Role> findRolesByUsuario(Long userId) {
        return entityManager.createQuery(
                        "SELECT r FROM User u JOIN u.roles r WHERE u.id = :userId ORDER BY r.id", Role.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Modulo> findModulosByRol(Long roleId) {
        return entityManager.createQuery(
                        "SELECT m FROM Modulo m JOIN m.roles r WHERE r.id = :roleId ORDER BY m.id", Modulo.class)
                .setParameter("roleId", roleId)
                .getResultList();
    }

    @Override
    public List<Modulo> findModulosByAplicacion(Long aplicacionId) {
        return entityManager.createQuery(
                        "SELECT m FROM Modulo m WHERE m.aplicacion.id = :aplicacionId ORDER BY m.id", Modulo.class)
                .setParameter("aplicacionId", aplicacionId)
                .getResultList();
    }

    @Override
    public List<User> findUsuariosByAplicacion(Long aplicacionId) {
        return entityManager.createQuery(
                        "SELECT u FROM Aplicacion a JOIN a.users u WHERE a.id = :aplicacionId AND u.borrado = 0 ORDER BY u.id", User.class)
                .setParameter("aplicacionId", aplicacionId)
                .getResultList();
    }

    /**
     * Modulos a los que el usuario llega realmente: los de sus Aplicaciones cuyos Roles
     * coinciden con los Roles que tiene asignados. Reproduce la resolucion que hace
     * UsersServiceAuthImpl.getUserDetails() para el proveedor de usuarios de Keycloak.
     */
    @Override
    public List<Modulo> findModulosEfectivosByUsuario(Long userId) {
        List<?> ids = entityManager.createNativeQuery(
                        "SELECT DISTINCT m.id " +
                        "FROM Modulos m " +
                        "JOIN RolesHasModulos rm ON rm.moduloId = m.id " +
                        "JOIN UsersHasRoles ur ON ur.roleId = rm.roleId AND ur.userId = :userId " +
                        "JOIN AplicacionsHasUsers au ON au.aplicacionId = m.aplicacionId AND au.userId = :userId " +
                        "ORDER BY m.id")
                .setParameter("userId", userId)
                .getResultList();

        if (ids.isEmpty()) return new ArrayList<>();

        List<Long> moduloIds = new ArrayList<>();
        ids.forEach(o -> moduloIds.add(((Number) o).longValue()));

        return entityManager.createQuery(
                        "SELECT m FROM Modulo m WHERE m.id IN :ids ORDER BY m.id", Modulo.class)
                .setParameter("ids", moduloIds)
                .getResultList();
    }

    @Override
    public Set<Long> findRolesEfectivosByUsuario(Long userId) {
        List<?> filas = entityManager.createNativeQuery(
                        "SELECT DISTINCT ur.roleId " +
                        "FROM UsersHasRoles ur " +
                        "JOIN RolesHasModulos rm ON rm.roleId = ur.roleId " +
                        "JOIN Modulos m ON m.id = rm.moduloId " +
                        "JOIN AplicacionsHasUsers au ON au.aplicacionId = m.aplicacionId AND au.userId = ur.userId " +
                        "WHERE ur.userId = :userId")
                .setParameter("userId", userId)
                .getResultList();

        Set<Long> resultado = new HashSet<>();
        filas.forEach(o -> resultado.add(((Number) o).longValue()));
        return resultado;
    }

    @Override
    public Map<Long, List<String>> findNombresModulosPorRol(List<Long> roleIds) {
        Map<Long, List<String>> resultado = new HashMap<>();
        if (roleIds == null || roleIds.isEmpty()) return resultado;

        List<?> filas = entityManager.createNativeQuery(
                        "SELECT rm.roleId, m.nombre " +
                        "FROM RolesHasModulos rm " +
                        "JOIN Modulos m ON m.id = rm.moduloId " +
                        "WHERE rm.roleId IN :roleIds " +
                        "ORDER BY rm.roleId, m.id")
                .setParameter("roleIds", roleIds)
                .getResultList();

        for (Object o : filas) {
            Object[] fila = (Object[]) o;
            Long roleId = ((Number) fila[0]).longValue();
            resultado.computeIfAbsent(roleId, k -> new ArrayList<>()).add((String) fila[1]);
        }
        return resultado;
    }

    // --------------------------------------------------- Aplicacion - Usuario

    @Override
    public boolean existeAplicacionUsuario(Long userId, Long aplicacionId) {
        Number n = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM AplicacionsHasUsers WHERE userId = :userId AND aplicacionId = :aplicacionId")
                .setParameter("userId", userId)
                .setParameter("aplicacionId", aplicacionId)
                .getSingleResult();
        return n.intValue() > 0;
    }

    @Override
    public int asignarAplicacion(Long userId, Long aplicacionId) {
        if (existeAplicacionUsuario(userId, aplicacionId)) return 0;
        return entityManager.createNativeQuery(
                        "INSERT INTO AplicacionsHasUsers (aplicacionId, userId) VALUES (:aplicacionId, :userId)")
                .setParameter("aplicacionId", aplicacionId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public int quitarAplicacion(Long userId, Long aplicacionId) {
        return entityManager.createNativeQuery(
                        "DELETE FROM AplicacionsHasUsers WHERE userId = :userId AND aplicacionId = :aplicacionId")
                .setParameter("userId", userId)
                .setParameter("aplicacionId", aplicacionId)
                .executeUpdate();
    }

    @Override
    public void reemplazarAplicaciones(Long userId, List<Long> aplicacionIds) {
        entityManager.createNativeQuery("DELETE FROM AplicacionsHasUsers WHERE userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        if (aplicacionIds == null) return;

        for (Long aplicacionId : new HashSet<>(aplicacionIds)) {
            if (aplicacionId == null) continue;
            entityManager.createNativeQuery(
                            "INSERT INTO AplicacionsHasUsers (aplicacionId, userId) VALUES (:aplicacionId, :userId)")
                    .setParameter("aplicacionId", aplicacionId)
                    .setParameter("userId", userId)
                    .executeUpdate();
        }
    }

    // --------------------------------------------------------- Rol - Usuario

    @Override
    public boolean existeRolUsuario(Long userId, Long roleId) {
        Number n = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM UsersHasRoles WHERE userId = :userId AND roleId = :roleId")
                .setParameter("userId", userId)
                .setParameter("roleId", roleId)
                .getSingleResult();
        return n.intValue() > 0;
    }

    @Override
    public int asignarRol(Long userId, Long roleId, Long sessionUserId) {
        if (existeRolUsuario(userId, roleId)) return 0;
        return insertarRol(userId, roleId, sessionUserId);
    }

    @Override
    public int quitarRol(Long userId, Long roleId) {
        return entityManager.createNativeQuery(
                        "DELETE FROM UsersHasRoles WHERE userId = :userId AND roleId = :roleId")
                .setParameter("userId", userId)
                .setParameter("roleId", roleId)
                .executeUpdate();
    }

    @Override
    public void reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId) {
        entityManager.createNativeQuery("DELETE FROM UsersHasRoles WHERE userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        if (roleIds == null) return;

        for (Long roleId : new HashSet<>(roleIds)) {
            if (roleId == null) continue;
            insertarRol(userId, roleId, sessionUserId);
        }
    }

    /** UsersHasRoles lleva auditoria; se puebla aqui porque el ManyToMany de JPA la deja en NULL. */
    private int insertarRol(Long userId, Long roleId, Long sessionUserId) {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        LocalDate fechaActual = fechaActualTime.toLocalDate();

        return entityManager.createNativeQuery(
                        "INSERT INTO UsersHasRoles (userId, roleId, regDate, regDatetime, regTimestamp, regUserId) " +
                        "VALUES (:userId, :roleId, :regDate, :regDatetime, :regTimestamp, :regUserId)")
                .setParameter("userId", userId)
                .setParameter("roleId", roleId)
                .setParameter("regDate", fechaActual)
                .setParameter("regDatetime", fechaActualTime)
                .setParameter("regTimestamp", fechaActualTime.toEpochSecond(ZoneOffset.UTC))
                .setParameter("regUserId", sessionUserId)
                .executeUpdate();
    }

    // --------------------------------------------------------- Modulo - Rol

    @Override
    public boolean existeModuloRol(Long roleId, Long moduloId) {
        Number n = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM RolesHasModulos WHERE roleId = :roleId AND moduloId = :moduloId")
                .setParameter("roleId", roleId)
                .setParameter("moduloId", moduloId)
                .getSingleResult();
        return n.intValue() > 0;
    }

    @Override
    public int asignarModuloARol(Long roleId, Long moduloId) {
        if (existeModuloRol(roleId, moduloId)) return 0;
        return entityManager.createNativeQuery(
                        "INSERT INTO RolesHasModulos (roleId, moduloId) VALUES (:roleId, :moduloId)")
                .setParameter("roleId", roleId)
                .setParameter("moduloId", moduloId)
                .executeUpdate();
    }

    @Override
    public int quitarModuloDeRol(Long roleId, Long moduloId) {
        return entityManager.createNativeQuery(
                        "DELETE FROM RolesHasModulos WHERE roleId = :roleId AND moduloId = :moduloId")
                .setParameter("roleId", roleId)
                .setParameter("moduloId", moduloId)
                .executeUpdate();
    }

    @Override
    public void reemplazarModulosDeRol(Long roleId, List<Long> moduloIds) {
        entityManager.createNativeQuery("DELETE FROM RolesHasModulos WHERE roleId = :roleId")
                .setParameter("roleId", roleId)
                .executeUpdate();

        if (moduloIds == null) return;

        for (Long moduloId : new HashSet<>(moduloIds)) {
            if (moduloId == null) continue;
            entityManager.createNativeQuery(
                            "INSERT INTO RolesHasModulos (roleId, moduloId) VALUES (:roleId, :moduloId)")
                    .setParameter("roleId", roleId)
                    .setParameter("moduloId", moduloId)
                    .executeUpdate();
        }
    }
}
