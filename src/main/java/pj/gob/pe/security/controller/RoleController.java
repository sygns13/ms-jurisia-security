package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.entities.Role;
import pj.gob.pe.security.service.RoleService;

import java.util.List;

@Tag(name = "Roles", description = "API de Consulta de Roles")
@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Consulta Lista de Roles", description = "Retorna una Lista de Roles")
    @GetMapping("/get-all")
    public ResponseEntity<List<Role>> listarAll() throws Exception{

        List<Role> resultado = roleService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta un Rol por ID", description = "Retorna un Rol filtrado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Role> listarPorId(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        Role obj = roleService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Role>(obj, HttpStatus.OK);
    }
}
