package pj.gob.pe.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Cargo;

public interface CargoService extends GeneralService<Cargo, Long> {

    void altabaja(Long id, Integer valor) throws Exception;

    void altabaja(Long id, Integer valor, Long userId) throws Exception;

    Page<Cargo> listar(Pageable pageable, String buscar) throws Exception;

    Cargo registrar(Cargo cargo, Long userId) throws Exception;

    int modificar(Cargo cargo, Long userId) throws Exception;

    void eliminar(Long id, Long userId) throws Exception;

    void grabarEliminar(Long id, Long userId) throws Exception;

}