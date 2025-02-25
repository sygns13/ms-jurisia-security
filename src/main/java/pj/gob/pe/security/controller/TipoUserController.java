package pj.gob.pe.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pj.gob.pe.security.model.entities.TipoUser;
import pj.gob.pe.security.service.TipoUserService;

import java.util.List;

@Tag(name = "TipoUsers", description = "API para gestionar tipos de usuarios")
@RestController
@RequestMapping("/v1/typeusers")
@RequiredArgsConstructor
public class TipoUserController {

    private final TipoUserService tipoUserService;

    @Operation(summary = "Consulta Lista de tipo de usuarios", description = "Retorna una Lista de tipo de usuarios")
    @GetMapping("/get-all")
    public ResponseEntity<List<TipoUser>> listarAll() throws Exception{

        List<TipoUser> resultado = tipoUserService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
