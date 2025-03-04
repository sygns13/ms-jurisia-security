package pj.gob.pe.security.dao.mysql.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.mysql.AplicacionDAO;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.AplicacionRepo;

@Repository
@RequiredArgsConstructor
public class AplicacionDAOImpl extends GenericDAOImpl<Aplicacion, Long> implements AplicacionDAO {

    private final AplicacionRepo repo;

    @Override
    protected GenericRepo<Aplicacion, Long> getRepo() {
        return repo;
    }
}
