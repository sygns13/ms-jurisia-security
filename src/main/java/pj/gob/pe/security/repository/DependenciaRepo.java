package pj.gob.pe.security.repository;

import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.repository.custom.DependenciaCustomRepo;

public interface DependenciaRepo extends GenericRepo<Dependencia, Long>, DependenciaCustomRepo {
}
