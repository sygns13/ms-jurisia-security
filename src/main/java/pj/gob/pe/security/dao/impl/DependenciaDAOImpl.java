package pj.gob.pe.security.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.DependenciaDAO;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.repository.DependenciaRepo;
import pj.gob.pe.security.repository.GenericRepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DependenciaDAOImpl extends GenericDAOImpl<Dependencia, Long> implements DependenciaDAO {

    private final DependenciaRepo repo;

    @Override
    protected GenericRepo<Dependencia, Long> getRepo() {
        return repo;
    }

    public Page<Dependencia> findDependenciasByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        return repo.findDependenciasByFiltersPage(filters, notEqualFilters, pageable);
    }

    public List<Dependencia> findDependenciasByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        return repo.findDependenciasByFiltersV2(filters, notEqualFilters);
    }
}
