package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.service.DependenciaService;

import java.net.URI;
import java.util.List;

@Tag(name = "Dependencias", description = "API para gestionar dependencias de usuarios")
@RestController
@RequestMapping("/v1/dependencias")
@RequiredArgsConstructor
public class DependenciaController {

    private final DependenciaService dependenciaService;

    @Operation(summary = "Consulta Lista de todas las dependencias", description = "Retorna una Lista de todas las dependencias de usuarios")
    @GetMapping("/get-all")
    public ResponseEntity<List<Dependencia>> listarAll(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization
    ) throws Exception{

        //this.SetClaims(Authorization);

        List<Dependencia> resultado = dependenciaService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta Lista de dependencias Paginadas", description = "Retorna una Lista de dependencias de usuarios paginadas")
    @GetMapping
    public ResponseEntity<Page<Dependencia>> listar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "buscar", defaultValue = "") String buscar,
            @RequestParam(name = "dependenciaId", defaultValue = "0") long dependenciaId) throws Exception{

        // @RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization, @RequestHeader Map<String, String> headers
        //this.SetClaims(Authorization);

        Pageable pageable = PageRequest.of(page,size);
        Page<Dependencia> resultado = dependenciaService.listar(pageable, buscar, dependenciaId);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta una Dependencia por ID", description = "Retorna una Dependencia filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Dependencia> listarPorId(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        Dependencia obj = dependenciaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Dependencia>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Creación de una Dependencia", description = "Registro de una nueva Dependencia")
    @PostMapping
    public ResponseEntity<Dependencia> registrar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @Valid @RequestBody Dependencia a) throws Exception{

        //this.SetClaims(Authorization);

        a.setId(null);
        Dependencia obj = dependenciaService.registrar(a);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modificación de una Dependencia", description = "Modificación de una Dependencia")
    @PutMapping
    public ResponseEntity<Integer> modificar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @Valid @RequestBody Dependencia a) throws Exception{

        //this.SetClaims(Authorization);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Dependencia objBD = dependenciaService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = dependenciaService.modificar(a);

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Elimina una Dependencia por ID", description = "Elimina una Dependencia por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        Dependencia obj = dependenciaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        dependenciaService.eliminar(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activa o Desactiva un Dependencia por ID", description = "Activa o Desactiva un Dependencia por ID")
    @PatchMapping("/activation/{id}/{valor}")
    public ResponseEntity<Void> altabaja(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        //this.SetClaims(Authorization);

        Dependencia obj = dependenciaService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        dependenciaService.altabaja(id, valor);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
