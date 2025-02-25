package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Dependencia;

public interface DependenciaService extends GeneralService<Dependencia, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    Page<Dependencia> listar(Pageable pageable, String buscar) throws Exception;
    Page<Dependencia> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception;
}
