package pj.gob.pe.security.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Dependencia;

import java.util.List;
import java.util.Map;

public interface DependenciaCustomRepo {

    List<Dependencia> findDependenciasByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters);
    Page<Dependencia> findDependenciasByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable);
}
