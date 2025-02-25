package pj.gob.pe.security.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.TipoUserDAO;
import pj.gob.pe.security.model.entities.TipoUser;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.TipoUserRepo;

@Repository
@RequiredArgsConstructor
public class TipoUserDAOImpl extends GenericDAOImpl<TipoUser, Long> implements TipoUserDAO {

    private final TipoUserRepo repo;

    @Override
    protected GenericRepo<TipoUser, Long> getRepo() {
        return repo;
    }
}
