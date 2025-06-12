package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.service.UserService;
import pj.gob.pe.security.utils.InputConsultaIA;

import java.net.URI;
import java.util.List;

@Tag(name = "Users", description = "API para gestionar Usuarios")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Consulta Lista de todos los usuarios", description = "Retorna una Lista de todos los usuarios")
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> listarAll(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization
    ) throws Exception{

        //this.SetClaims(Authorization);

        List<User> resultado = userService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta Lista de usuarios Paginadas", description = "Retorna una Lista de usuarios paginadas")
    @GetMapping
    public ResponseEntity<Page<User>> listar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "buscar", defaultValue = "") String buscar,
            @RequestParam(name = "dependenciaId", defaultValue = "0") long dependenciaId) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        //this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<User> resultado = userService.listar(pageable, buscar, dependenciaId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta un Usuario por ID", description = "Retorna una User filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> listarPorId(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<User>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Creación de un Usuario", description = "Registro de un nuevo Usuario")
    @PostMapping
    public ResponseEntity<User> registrar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @Valid @RequestBody User a) throws Exception{

        //this.SetClaims(Authorization);

        a.setId(null);
        User obj = userService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modificación de un Usuario", description = "Modificación de un Usuario")
    @PutMapping
    public ResponseEntity<Integer> modificar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @Valid @RequestBody User a) throws Exception{

        //this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        User objBD = userService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = userService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Elimina un Usuario por ID", description = "Elimina un Usuario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        userService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activa o Desactiva un Usuario por ID", description = "Activa o Desactiva un Usuario por ID")
    @PatchMapping("/activation/{id}/{valor}")
    public ResponseEntity<Void> altabaja(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        //this.SetClaims(Authorization);

        User obj = userService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        userService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "Busqueda de Usuarios por Criterios", description = "Busqueda de Usuarios por Criterios")
    @PostMapping("/listar-interservices")
    public ResponseEntity<List<User>> reportCabConsultaIA(@Valid @RequestBody InputConsultaIA inputData) throws Exception{

        List<User> dataResponse = userService.buscarUsuarios(inputData);

        if(dataResponse == null) {
            throw new ModeloNotFoundException("Error de procesamiento de Datos. Comunicarse con un administrador ");
        }

        return new ResponseEntity<List<User>>(dataResponse, HttpStatus.OK);
    }
}
