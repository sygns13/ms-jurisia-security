package pj.gob.pe.security.dao.mysql.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.mysql.RoleDAO;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.RoleRepo;

@Repository
@RequiredArgsConstructor
public class RoleDAOImpl extends GenericDAOImpl<Role, Long> implements RoleDAO {

    private final RoleRepo repo;

    @Override
    protected GenericRepo<Role, Long> getRepo() {
        return repo;
    }
}
