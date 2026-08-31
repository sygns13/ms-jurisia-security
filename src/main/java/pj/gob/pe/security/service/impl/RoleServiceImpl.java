package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.security.dao.mysql.AsignacionDAO;
import pj.gob.pe.security.dao.mysql.ModuloDAO;
import pj.gob.pe.security.dao.mysql.RoleDAO;
import pj.gob.pe.security.exception.ValidationServiceException;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleDAO roleDAO;
    private final AsignacionDAO asignacionDAO;
    private final ModuloDAO moduloDAO;
    
    @Override
    public Role registrar(Role role) throws Exception {
        return null;
    }

    @Override
    public int modificar(Role role) throws Exception {
        return 0;
    }

    @Override
    public List<Role> listar() throws Exception {
        return roleDAO.listar();
    }

    @Override
    public Role listarPorId(Long id) throws Exception {
        return roleDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {

    }

    @Override
    public Role grabarRegistro(Role role) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(Role role) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {

    }

    @Override
    public boolean validacionRegistro(Role role, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(Role role, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    // =====================================================================
    // Modulos del Rol (RolesHasModulos)
    // =====================================================================

    @Override
    public List<Modulo> listarModulos(Long roleId) throws Exception {
        return asignacionDAO.listarModulosDeRol(roleId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<Modulo> asignarModulo(Long roleId, Long moduloId) throws Exception {
        this.validarModulo(moduloId);

        if(asignacionDAO.existeModuloRol(roleId, moduloId)){
            throw new ValidationServiceException("El Módulo ya se encuentra asignado al Rol");
        }

        asignacionDAO.asignarModuloARol(roleId, moduloId);

        return asignacionDAO.listarModulosDeRol(roleId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<Modulo> quitarModulo(Long roleId, Long moduloId) throws Exception {
        if(!asignacionDAO.existeModuloRol(roleId, moduloId)){
            throw new ValidationServiceException("El Módulo no se encuentra asignado al Rol");
        }

        asignacionDAO.quitarModuloDeRol(roleId, moduloId);

        return asignacionDAO.listarModulosDeRol(roleId);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public List<Modulo> reemplazarModulos(Long roleId, List<Long> moduloIds) throws Exception {
        List<Long> ids = moduloIds != null ? moduloIds : new ArrayList<>();

        for (Long moduloId : ids) {
            this.validarModulo(moduloId);
        }

        asignacionDAO.reemplazarModulosDeRol(roleId, ids);

        return asignacionDAO.listarModulosDeRol(roleId);
    }

    private void validarModulo(Long moduloId) throws Exception {
        if(moduloId == null){
            throw new ValidationServiceException("Seleccione el Módulo");
        }

        Modulo modulo = moduloDAO.listarPorId(moduloId);

        if(modulo == null){
            throw new ValidationServiceException("El Módulo con ID " + moduloId + " no existe");
        }
    }
}
