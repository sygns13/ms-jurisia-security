package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.RoleDAO;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.service.RoleService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleDAO roleDAO;
    
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
}
