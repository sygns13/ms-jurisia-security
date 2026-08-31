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
import pj.gob.pe.security.model.entities.Login;
import pj.gob.pe.security.service.LoginService;
import pj.gob.pe.security.service.externals.AuthService;

import java.util.List;

@Tag(name = "Logins", description = "API de Consulta de Logins")
@RestController
@RequestMapping("/v1/logins")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final AuthService authService;

    @Operation(summary = "Consulta Lista de Logins", description = "Retorna una Lista de Logins")
    @GetMapping("/get-all")
    public ResponseEntity<List<Login>> listarAll(
            @RequestHeader("SessionId") String SessionId) throws Exception{

        authService.validarSesion(SessionId);

        List<Login> resultado = loginService.listar();

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(summary = "Consulta una Login por ID", description = "Retorna una Login filtrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Login> listarPorId(
            @RequestHeader("SessionId") String SessionId,
            @PathVariable("id") Long id) throws Exception{

        authService.validarSesion(SessionId);

        Login obj = loginService.listarPorId(id);

        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO "+ id);
        }

        return new ResponseEntity<Login>(obj, HttpStatus.OK);
    }
}
