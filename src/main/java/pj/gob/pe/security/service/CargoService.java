package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Cargo;

public interface CargoService extends GeneralService<Cargo, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Cargo> listar(Pageable pageable, String buscar) throws Exception;

}