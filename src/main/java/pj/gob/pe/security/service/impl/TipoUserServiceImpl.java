package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.mysql.TipoUserDAO;
import pj.gob.pe.security.model.entities.TipoUser;
import pj.gob.pe.security.service.TipoUserService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TipoUserServiceImpl implements TipoUserService {

    private final TipoUserDAO tipoUserDAO;

    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

    }

    @Override
    public Page<TipoUser> listar(Pageable pageable, String buscar) throws Exception {
        return null;
    }

    @Override
    public TipoUser registrar(TipoUser tipoUser) throws Exception {
        return null;
    }

    @Override
    public int modificar(TipoUser tipoUser) throws Exception {
        return 0;
    }

    @Override
    public List<TipoUser> listar() throws Exception {
        return tipoUserDAO.listar();
    }

    @Override
    public TipoUser listarPorId(Long id) throws Exception {
        return tipoUserDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {

    }

    @Override
    public TipoUser grabarRegistro(TipoUser tipoUser) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(TipoUser tipoUser) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {

    }

    @Override
    public boolean validacionRegistro(TipoUser tipoUser, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(TipoUser tipoUser, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}
