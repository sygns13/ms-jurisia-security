package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.AplicacionDAO;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.service.AplicacionService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AplicacionServiceImpl implements AplicacionService {

    private final AplicacionDAO aplicacionDAO;

    @Override
    public Aplicacion registrar(Aplicacion aplicacion) throws Exception {
        return null;
    }

    @Override
    public int modificar(Aplicacion aplicacion) throws Exception {
        return 0;
    }

    @Override
    public List<Aplicacion> listar() throws Exception {
        return aplicacionDAO.listar();
    }

    @Override
    public Aplicacion listarPorId(Long id) throws Exception {
        return aplicacionDAO.listarPorId(id);
    }

    @Override
    public void eliminar(Long id) throws Exception {

    }

    @Override
    public Aplicacion grabarRegistro(Aplicacion aplicacion) throws Exception {
        return null;
    }

    @Override
    public int grabarRectificar(Aplicacion aplicacion) throws Exception {
        return 0;
    }

    @Override
    public void grabarEliminar(Long id) throws Exception {

    }

    @Override
    public boolean validacionRegistro(Aplicacion aplicacion, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionModificado(Aplicacion aplicacion, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {
        return false;
    }
}
