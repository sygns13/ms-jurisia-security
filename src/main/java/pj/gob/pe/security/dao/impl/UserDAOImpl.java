package pj.gob.pe.security.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.UserDAO;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.UserRepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO {

    private final UserRepo repo;

    @Override
    protected GenericRepo<User, Long> getRepo() {
        return repo;
    }

    public Page<User> findUsersByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        return repo.findUsersByFiltersPage(filters, notEqualFilters, pageable);
    }

    public List<User> findUsersByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        return repo.findUsersByFiltersV2(filters, notEqualFilters);
    }

    public User findByUsername(String username){
        return repo.findByUsername(username);
    }
}
