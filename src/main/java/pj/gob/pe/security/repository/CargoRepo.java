package pj.gob.pe.security.repository;

import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.repository.custom.CargoCustomRepo;

public interface CargoRepo extends GenericRepo<Cargo, Long>, CargoCustomRepo {
}
