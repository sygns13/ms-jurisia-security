package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.TipoUser;

public interface TipoUserService extends GeneralService<TipoUser, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<TipoUser> listar(Pageable pageable, String buscar) throws Exception;

}