package pj.gob.pe.security.service;

import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.User;

import java.util.List;

public interface AplicacionService extends GeneralService<Aplicacion, Long>{

    List<Modulo> listarModulos(Long aplicacionId) throws Exception;
    List<User> listarUsuarios(Long aplicacionId) throws Exception;
}
