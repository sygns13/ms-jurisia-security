package pj.gob.pe.security.repository;

import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.repository.custom.UserCustomRepo;

public interface UserRepo extends GenericRepo<User, Long>, UserCustomRepo {
}
