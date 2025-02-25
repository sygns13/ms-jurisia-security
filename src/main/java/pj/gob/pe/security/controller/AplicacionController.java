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
import pj.gob.pe.security.model.entities.Aplicacion;
import pj.gob.pe.security.service.AplicacionService;

import java.util.List;

@Tag(name = "Aplicacions", description = "API para gestionar Aplicaciones")
@RestController
@RequestMapping("/v1/aplicacions")
@RequiredArgsConstructor
public class AplicacionController {

    private final AplicacionService aplicacionService;

    @Operation(summary = "Consulta Lista de Aplicaciones", description = "Retorna una Lista de Aplicaciones")
    @GetMapping("/get-all")
    public ResponseEntity<List<Aplicacion>> listarAll() throws Exception{

        List<Aplicacion> resultado = aplicacionService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta una Aplicacion por ID", description = "Retorna una Aplicacion filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Aplicacion> listarPorId(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        Aplicacion obj = aplicacionService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Aplicacion>(obj, HttpStatus.OK);
    }
}
