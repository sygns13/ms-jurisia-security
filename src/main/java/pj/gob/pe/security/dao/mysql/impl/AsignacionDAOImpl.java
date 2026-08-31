package pj.gob.pe.security.dao.mysql.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.mysql.AsignacionDAO;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.repository.custom.AsignacionCustomRepo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AsignacionDAOImpl implements AsignacionDAO {

    private final AsignacionCustomRepo repo;

    @Override
    public List<Aplicacion> listarAplicacionesDeUsuario(Long userId) {
        return repo.findAplicacionesByUsuario(userId);
    }

    @Override
    public List<Role> listarRolesDeUsuario(Long userId) {
        return repo.findRolesByUsuario(userId);
    }

    @Override
    public List<Modulo> listarModulosDeRol(Long roleId) {
        return repo.findModulosByRol(roleId);
    }

    @Override
    public List<Modulo> listarModulosDeAplicacion(Long aplicacionId) {
        return repo.findModulosByAplicacion(aplicacionId);
    }

    @Override
    public List<User> listarUsuariosDeAplicacion(Long aplicacionId) {
        return repo.findUsuariosByAplicacion(aplicacionId);
    }

    @Override
    public List<Modulo> listarModulosEfectivosDeUsuario(Long userId) {
        return repo.findModulosEfectivosByUsuario(userId);
    }

    @Override
    public Set<Long> listarRolesEfectivosDeUsuario(Long userId) {
        return repo.findRolesEfectivosByUsuario(userId);
    }

    @Override
    public Map<Long, List<String>> listarNombresModulosPorRol(List<Long> roleIds) {
        return repo.findNombresModulosPorRol(roleIds);
    }

    @Override
    public boolean existeAplicacionUsuario(Long userId, Long aplicacionId) {
        return repo.existeAplicacionUsuario(userId, aplicacionId);
    }

    @Override
    public int asignarAplicacion(Long userId, Long aplicacionId) {
        return repo.asignarAplicacion(userId, aplicacionId);
    }

    @Override
    public int quitarAplicacion(Long userId, Long aplicacionId) {
        return repo.quitarAplicacion(userId, aplicacionId);
    }

    @Override
    public void reemplazarAplicaciones(Long userId, List<Long> aplicacionIds) {
        repo.reemplazarAplicaciones(userId, aplicacionIds);
    }

    @Override
    public boolean existeRolUsuario(Long userId, Long roleId) {
        return repo.existeRolUsuario(userId, roleId);
    }

    @Override
    public int asignarRol(Long userId, Long roleId, Long sessionUserId) {
        return repo.asignarRol(userId, roleId, sessionUserId);
    }

    @Override
    public int quitarRol(Long userId, Long roleId) {
        return repo.quitarRol(userId, roleId);
    }

    @Override
    public void reemplazarRoles(Long userId, List<Long> roleIds, Long sessionUserId) {
        repo.reemplazarRoles(userId, roleIds, sessionUserId);
    }

    @Override
    public boolean existeModuloRol(Long roleId, Long moduloId) {
        return repo.existeModuloRol(roleId, moduloId);
    }

    @Override
    public int asignarModuloARol(Long roleId, Long moduloId) {
        return repo.asignarModuloARol(roleId, moduloId);
    }

    @Override
    public int quitarModuloDeRol(Long roleId, Long moduloId) {
        return repo.quitarModuloDeRol(roleId, moduloId);
    }

    @Override
    public void reemplazarModulosDeRol(Long roleId, List<Long> moduloIds) {
        repo.reemplazarModulosDeRol(roleId, moduloIds);
    }
}
