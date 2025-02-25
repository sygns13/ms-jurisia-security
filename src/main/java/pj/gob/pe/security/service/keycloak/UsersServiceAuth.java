package pj.gob.pe.security.service.keycloak;

import pj.gob.pe.security.model.keycloak.UserAuth;

public interface UsersServiceAuth {

    UserAuth getUserDetails(String userName, String password);
    UserAuth getUserDetails(String userName);
}
