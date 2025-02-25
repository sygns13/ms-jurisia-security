package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.ModuloDAO;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.service.ModuloService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ModuloServiceImpl implements ModuloService {

    private final ModuloDAO moduloDAO;
    
    @Override
    public Modulo registrar(Modulo modulo) throws Exception {
        return null;
    }

    @Override
    public int modificar(Modulo modulo) throws Exception {
        return 0;
    }

    @Override
    public List<Modulo> listar() throws Exception {
        return moduloDAO.listar();
    }

    @Override
    public Modulo listarPorId(Long id) throws Exception {
        return moduloDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {

    }

    @Override
    public Modulo grabarRegistro(Modulo modulo) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(Modulo modulo) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {

    }

    @Override
    public boolean validacionRegistro(Modulo modulo, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(Modulo modulo, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}
