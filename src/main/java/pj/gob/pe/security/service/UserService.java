package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.utils.InputConsultaIA;

import java.util.List;

public interface UserService extends GeneralService<User, Long>{

    void altabaja(Long id, Integer valor) throws Exception;

    Page<User> listar(Pageable pageable, String buscar) throws Exception;
    Page<User> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception;

    List<User> buscarUsuarios(InputConsultaIA inputConsultaIA) throws Exception;
}
