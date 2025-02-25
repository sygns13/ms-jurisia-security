package pj.gob.pe.security.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.LoginDAO;
import pj.gob.pe.security.model.entities.Login;
import pj.gob.pe.security.repository.LoginRepo;
import pj.gob.pe.security.repository.GenericRepo;

@Repository
@RequiredArgsConstructor
public class LoginDAOImpl extends GenericDAOImpl<Login, Long> implements LoginDAO {

    private final LoginRepo repo;

    @Override
    protected GenericRepo<Login, Long> getRepo() {
        return repo;
    }
}
