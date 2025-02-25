package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.User;

public interface UserService extends GeneralService<User, Long>{

    void altabaja(Long id, Integer valor) throws Exception;

    Page<User> listar(Pageable pageable, String buscar) throws Exception;
    Page<User> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception;
}
