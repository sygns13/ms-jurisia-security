package pj.gob.pe.security.dao.mysql.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.mysql.ModuloDAO;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.ModuloRepo;

@Repository
@RequiredArgsConstructor
public class ModuloDAOImpl extends GenericDAOImpl<Modulo, Long> implements ModuloDAO {

    private final ModuloRepo repo;

    @Override
    protected GenericRepo<Modulo, Long> getRepo() {
        return repo;
    }
}
