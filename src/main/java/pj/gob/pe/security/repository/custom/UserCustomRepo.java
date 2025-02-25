package pj.gob.pe.security.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.User;

import java.util.List;
import java.util.Map;

public interface UserCustomRepo {

    List<User> findUsersByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters);
    Page<User> findUsersByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable);

    User findByUsername(String username);
}
