package pj.gob.pe.security.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.User;

import java.util.List;
import java.util.Map;

public interface UserDAO extends GenericDAO<User, Long>{

    Page<User> findUsersByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable);

    List<User> findUsersByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters);

    User findByUsername(String username);
}
