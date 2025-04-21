package pj.gob.pe.security.service.externals;

import pj.gob.pe.security.utils.beans.DataUsuarioDTO;

public interface SIJService {

    public DataUsuarioDTO getDatosUser(String username, String password);
}
