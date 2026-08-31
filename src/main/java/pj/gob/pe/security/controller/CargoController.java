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
import pj.gob.pe.security.model.beans.UserLogin;
import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.service.CargoService;
import pj.gob.pe.security.service.externals.AuthService;

import java.net.URI;
import java.util.List;

@Tag(name = "Cargos", description = "API para gestionar cargos de usuarios")
@RestController
@RequestMapping("/v1/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;
    private final AuthService authService;

    @Operation(summary = "Consulta Lista de todos los cargos", description = "Retorna una Lista de todos los cargos de usuarios")
    @GetMapping("/get-all")
    public ResponseEntity<List<Cargo>> listarAll(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        authService.validarSesion(SessionId);

        List<Cargo> resultado = cargoService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta Lista de cargos Paginada", description = "Retorna una Lista de cargos de usuarios paginados")
    @GetMapping
    public ResponseEntity<Page<Cargo>> listar(
                                              @RequestHeader("SessionId") String SessionId,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size,
                                              @RequestParam(name = "buscar", defaultValue = "") String buscar) throws Exception{

        authService.validarSesion(SessionId);

        Pageable pageable = PageRequest.of(page,size);
        Page<Cargo> resultado = cargoService.listar(pageable, buscar);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta un Cargo por ID", description = "Retorna un Cargo filtrado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cargo> listarPorId(
                                             @RequestHeader("SessionId") String SessionId,
                                             @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        Cargo obj = cargoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Cargo>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Creación de un Cargo", description = "Registro de un nuevo Cargo")
    @PostMapping
    public ResponseEntity<Cargo> registrar(
                                @RequestHeader("SessionId") String SessionId,
                                @Valid @RequestBody Cargo a) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        a.setId(null);
        Cargo obj = cargoService.registrar(a, userLogin.getIdUser());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return  ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modificación de un Cargo", description = "Modificación de un Cargo")
    @PutMapping
    public ResponseEntity<Integer> modificar(
            @RequestHeader("SessionId") String SessionId,
            @Valid @RequestBody Cargo a) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        if(a.getId() == null){
            throw new ModeloNotFoundException("ID NO ENVIADO ");
        }

        Cargo objBD = cargoService.listarPorId(a.getId());

        if(objBD == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ a.getId());
        }

        int obj = cargoService.modificar(a, userLogin.getIdUser());

        return new ResponseEntity<Integer>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Elimina un Cargo por ID", description = "Elimina un Cargo por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
                                        @RequestHeader("SessionId") String SessionId,
                                        @PathVariable("id") Long id) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        Cargo obj = cargoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        cargoService.eliminar(id, userLogin.getIdUser());

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activa o Desactiva un Cargo por ID", description = "Activa o Desactiva un Cargo por ID")
    @PatchMapping("/activation/{id}/{valor}")
    public ResponseEntity<Void> altabaja(
                                         @RequestHeader("SessionId") String SessionId,
                                         @PathVariable("id") Long id, @PathVariable("valor") Integer valor) throws Exception{

        UserLogin userLogin = authService.validarSesion(SessionId);

        Cargo obj = cargoService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
        cargoService.altabaja(id, valor, userLogin.getIdUser());

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
