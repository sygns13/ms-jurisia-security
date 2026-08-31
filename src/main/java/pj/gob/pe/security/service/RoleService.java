package pj.gob.pe.security.service;

import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;

import java.util.List;

public interface RoleService extends GeneralService<Role, Long>{

    // ---- Modulos del Rol (RolesHasModulos) ----
    List<Modulo> listarModulos(Long roleId) throws Exception;
    List<Modulo> asignarModulo(Long roleId, Long moduloId) throws Exception;
    List<Modulo> quitarModulo(Long roleId, Long moduloId) throws Exception;
    List<Modulo> reemplazarModulos(Long roleId, List<Long> moduloIds) throws Exception;
}
