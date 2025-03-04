package pj.gob.pe.security.dao.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pj.gob.pe.security.model.entities.Cargo;

import java.util.List;
import java.util.Map;

public interface CargoDAO extends GenericDAO<Cargo, Long>{

    List<Cargo> findCargosNoBorrados();

    List<Cargo> findCargosByFilters(Map<String, Object> filters);

    List<Cargo> findCargosByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters);

    Page<Cargo> findCargosByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable);
}
