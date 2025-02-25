package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.LoginDAO;
import pj.gob.pe.security.model.entities.Login;
import pj.gob.pe.security.service.LoginService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginDAO loginDAO;

    @Override
    public Login registrar(Login login) throws Exception {
        return null;
    }

    @Override
    public int modificar(Login login) throws Exception {
        return 0;
    }

    @Override
    public List<Login> listar() throws Exception {
        return loginDAO.listar();
    }

    @Override
    public Login listarPorId(Long id) throws Exception {
        return loginDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {

    }

    @Override
    public Login grabarRegistro(Login login) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(Login login) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {

    }

    @Override
    public boolean validacionRegistro(Login login, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(Login login, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}
