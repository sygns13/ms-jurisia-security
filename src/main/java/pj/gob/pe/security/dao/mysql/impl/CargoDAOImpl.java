package pj.gob.pe.security.dao.mysql.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.security.dao.mysql.CargoDAO;
import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.repository.GenericRepo;
import pj.gob.pe.security.repository.CargoRepo;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CargoDAOImpl extends GenericDAOImpl<Cargo, Long> implements CargoDAO {


    private final CargoRepo repo;

    @Override
    protected GenericRepo<Cargo, Long> getRepo() {
        return repo;
    }

    public List<Cargo> findCargosNoBorrados() {
        return repo.findCargosNoBorrados();
    }

    public List<Cargo> findCargosByFilters(Map<String, Object> filters) {
        return repo.findCargosByFilters(filters);
    }

    public List<Cargo> findCargosByFiltersV2(Map<String, Object> filters, Map<String, Object> notEqualFilters) {
        return repo.findCargosByFiltersV2(filters, notEqualFilters);
    }

    public Page<Cargo> findCargosByFiltersPage(Map<String, Object> filters, Map<String, Object> notEqualFilters, Pageable pageable) {
        return repo.findCargosByFiltersPage(filters, notEqualFilters, pageable);
    }
}
