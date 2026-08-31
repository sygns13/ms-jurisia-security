package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Dependencia;

public interface DependenciaService extends GeneralService<Dependencia, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    void altabaja(Long id, Integer valor, Long userId) throws Exception;

    Page<Dependencia> listar(Pageable pageable, String buscar) throws Exception;
    Page<Dependencia> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception;

    Dependencia registrar(Dependencia dependencia, Long userId) throws Exception;

    int modificar(Dependencia dependencia, Long userId) throws Exception;

    void eliminar(Long id, Long userId) throws Exception;

    void grabarEliminar(Long id, Long userId) throws Exception;
}
