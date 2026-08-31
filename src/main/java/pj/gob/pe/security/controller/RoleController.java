package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.service.RoleService;
import pj.gob.pe.security.service.externals.AuthService;
import pj.gob.pe.security.utils.beans.IdsRequest;

import java.util.List;

@Tag(name = "Roles", description = "API de Consulta de Roles")
@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final AuthService authService;

    @Operation(summary = "Consulta Lista de Roles", description = "Retorna una Lista de Roles")
    @GetMapping("/get-all")
    public ResponseEntity<List<Role>> listarAll(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        authService.validarSesion(SessionId);

        List<Role> resultado = roleService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta un Rol por ID", description = "Retorna un Rol filtrado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Role> listarPorId(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        Role obj = roleService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Role>(obj, HttpStatus.OK);
    }

    // =====================================================================
    // Modulos del Rol (RolesHasModulos)
    // =====================================================================

    @Operation(summary = "Consulta los Modulos de un Rol",
               description = "Retorna los Modulos asociados al Rol en RolesHasModulos. Un Rol sin Modulos no se propaga a Keycloak")
    @GetMapping("/{id}/modulos")
    public ResponseEntity<List<Modulo>> listarModulos(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarRol(id);

        return new ResponseEntity<>(roleService.listarModulos(id), HttpStatus.OK);
    }

    @Operation(summary = "Asigna un Modulo a un Rol",
               description = "Registra la relacion Rol - Modulo en RolesHasModulos")
    @PostMapping("/{id}/modulos/{moduloId}")
    public ResponseEntity<List<Modulo>> asignarModulo(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("moduloId") Long moduloId) throws Exception{

        authService.validarSesion(SessionId);

        this.validarRol(id);

        return new ResponseEntity<>(roleService.asignarModulo(id, moduloId), HttpStatus.CREATED);
    }

    @Operation(summary = "Quita un Modulo a un Rol",
               description = "Elimina la relacion Rol - Modulo de RolesHasModulos")
    @DeleteMapping("/{id}/modulos/{moduloId}")
    public ResponseEntity<List<Modulo>> quitarModulo(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @PathVariable("moduloId") Long moduloId) throws Exception{

        authService.validarSesion(SessionId);

        this.validarRol(id);

        return new ResponseEntity<>(roleService.quitarModulo(id, moduloId), HttpStatus.OK);
    }

    @Operation(summary = "Reemplaza los Modulos de un Rol",
               description = "El Rol queda asociado exactamente a los Modulos indicados. Una lista vacia lo deja sin Modulos")
    @PutMapping("/{id}/modulos")
    public ResponseEntity<List<Modulo>> reemplazarModulos(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id,
            @RequestBody IdsRequest request) throws Exception{

        authService.validarSesion(SessionId);

        this.validarRol(id);

        List<Modulo> resultado = roleService.reemplazarModulos(id, request != null ? request.getIds() : null);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    /** Verifica que el Rol exista. */
    private void validarRol(Long id) throws Exception {
        Role obj = roleService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
    }
}
