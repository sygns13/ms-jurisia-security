package pj.gob.pe.security.service.keycloak.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pj.gob.pe.security.dao.UserDAO;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.model.keycloak.Client;
import pj.gob.pe.security.model.keycloak.Role;
import pj.gob.pe.security.model.keycloak.UserAuth;
import pj.gob.pe.security.model.keycloak.UserType;
import pj.gob.pe.security.service.keycloak.UsersServiceAuth;
import pj.gob.pe.security.utils.Constantes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsersServiceAuthImpl implements UsersServiceAuth {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAuth getUserDetails(String userName, String password) {
        UserAuth returnValue = null;

        User userEntity = userDAO.findByUsername(userName);

        if (userEntity == null) {
            return returnValue;
        }

        if (passwordEncoder.matches(password,
                userEntity.getPassword())) {
            System.out.println("password matches!!!");

            returnValue = new UserAuth();
            //BeanUtils.copyProperties(userEntity, returnValue);
            returnValue.setUserId(userEntity.getId().toString());
            returnValue.setFirstName(userEntity.getNombres());
            returnValue.setLastName(userEntity.getApellidos());
            returnValue.setEmail(userEntity.getEmail());
            returnValue.setUserName(userEntity.getUsername());
        }

        return returnValue;
    }

    @Override
    public UserAuth getUserDetails(String userName) {
        UserAuth returnValue = new UserAuth();

        User userEntity = userDAO.findByUsername(userName);
        if (userEntity == null) {
            return returnValue;
        }

        Hibernate.initialize(userEntity.getTipoUser());
        Hibernate.initialize(userEntity.getAplicacion());
        Hibernate.initialize(userEntity.getRoles());

        //Lista de Clients
        List<Client> clients = new ArrayList<>();
        UserType userType = new UserType();
        userType.setId(userEntity.getTipoUser() != null ? userEntity.getTipoUser().getId().toString() : null);
        userType.setRealmRole(userEntity.getTipoUser() != null ? "usertype_" +userEntity.getTipoUser().getId().toString() : "usertype_0");

        if(userEntity.getAplicacion() != null){
            userEntity.getAplicacion().forEach((aplicacion) -> {
                Client client = new Client();
                client.setAppId(aplicacion.getId()!= null ? aplicacion.getId().toString() : null);
                client.setClientId(aplicacion.getCodigo());

                Set<Role> roles = new HashSet<>();
                Hibernate.initialize(aplicacion);
                if(aplicacion.getModulos() != null){
                    aplicacion.getModulos().forEach((modulo) -> {
                        Hibernate.initialize(modulo);

                        modulo.getRoles().forEach((role1) -> {
                            userEntity.getRoles().forEach((role2) -> {
                                if(role1.getId().equals(role2.getId())){
                                    Role role = new Role();
                                    role.setId(role1.getId().toString());
                                    role.setRole(role1.getName());

                                    roles.add(role);
                                }
                            });
                        });
                    });
                }

                client.setRoles(roles);
                clients.add(client);
            });
        }

        //BeanUtils.copyProperties(userEntity, returnValue);
        returnValue.setUserId(userEntity.getId().toString());
        returnValue.setFirstName(userEntity.getNombres());
        returnValue.setLastName(userEntity.getApellidos());
        returnValue.setEmail(userEntity.getEmail());
        returnValue.setUserName(userEntity.getUsername());
        returnValue.setUserType(userType);
        returnValue.setClients(clients);
        returnValue.setEnabled(userEntity.getActivo().equals(Constantes.REGISTRO_ACTIVO));
        returnValue.setEmailVerified(true);

        return returnValue;
    }
}
