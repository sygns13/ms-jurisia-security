package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.model.entities.User;
import pj.gob.pe.security.service.AplicacionService;
import pj.gob.pe.security.service.externals.AuthService;

import java.util.List;

@Tag(name = "Aplicacions", description = "API para gestionar Aplicaciones")
@RestController
@RequestMapping("/v1/aplicacions")
@RequiredArgsConstructor
public class AplicacionController {

    private final AplicacionService aplicacionService;
    private final AuthService authService;

    @Operation(summary = "Consulta Lista de Aplicaciones", description = "Retorna una Lista de Aplicaciones")
    @GetMapping("/get-all")
    public ResponseEntity<List<Aplicacion>> listarAll(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        authService.validarSesion(SessionId);

        List<Aplicacion> resultado = aplicacionService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta una Aplicacion por ID", description = "Retorna una Aplicacion filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Aplicacion> listarPorId(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        Aplicacion obj = aplicacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Aplicacion>(obj, HttpStatus.OK);
    }

    @Operation(summary = "Consulta los Modulos de una Aplicacion",
               description = "Retorna los Modulos que pertenecen a la Aplicacion")
    @GetMapping("/{id}/modulos")
    public ResponseEntity<List<Modulo>> listarModulos(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarAplicacion(id);

        return new ResponseEntity<>(aplicacionService.listarModulos(id), HttpStatus.OK);
    }

    @Operation(summary = "Consulta los Usuarios de una Aplicacion",
               description = "Retorna los Usuarios no borrados asignados a la Aplicacion en AplicacionsHasUsers")
    @GetMapping("/{id}/usuarios")
    public ResponseEntity<List<User>> listarUsuarios(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        this.validarAplicacion(id);

        return new ResponseEntity<>(aplicacionService.listarUsuarios(id), HttpStatus.OK);
    }

    /** Verifica que la Aplicacion exista. */
    private void validarAplicacion(Long id) throws Exception {
        Aplicacion obj = aplicacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }
    }
}
