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
import pj.gob.pe.security.model.entities.Modulo;
import pj.gob.pe.security.service.ModuloService;

import java.util.List;

@Tag(name = "Modulos", description = "API de Consulta de Modulos")
@RestController
@RequestMapping("/v1/modulos")
@RequiredArgsConstructor
public class ModuloController {

    private final ModuloService moduloService;

    @Operation(summary = "Consulta Lista de Modulos", description = "Retorna una Lista de Modulos")
    @GetMapping("/get-all")
    public ResponseEntity<List<Modulo>> listarAll() throws Exception{

        List<Modulo> resultado = moduloService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta una Modulo por ID", description = "Retorna una Modulo filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Modulo> listarPorId(
            //@RequestHeader(HttpHeaders.AUTHORIZATION) String Authorization,
            @PathVariable("id") Long id) throws Exception{

        //this.SetClaims(Authorization);

        Modulo obj = moduloService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Modulo>(obj, HttpStatus.OK);
    }
}
