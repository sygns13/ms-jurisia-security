package pj.gob.pe.security.controller.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pj.gob.pe.security.exception.AuthLoginException;
import pj.gob.pe.security.model.beans.ResponseLogin;
import pj.gob.pe.security.model.beans.TokenResponse;
import pj.gob.pe.security.service.externals.AuthService;
import pj.gob.pe.security.utils.beans.LoginInput;
import pj.gob.pe.security.utils.beans.LogoutInput;
import pj.gob.pe.security.utils.beans.VerifySessionInput;

import javax.naming.AuthenticationException;

@Tag(name = "Login Users", description = "API para gestionar Logins de Usuarios")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthLoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> loginAppJurisia(@Valid @RequestBody LoginInput login, HttpServletRequest request) throws Exception {

        // Obtener la IP del cliente
        String clientIp = request.getRemoteAddr();

        TokenResponse token = authService.authenticate(login);
        if(token == null){
            throw new AuthLoginException("Error de Credenciales de inicio de sesión o cuenta desabilitada");
        }

        ResponseLogin response = authService.generateSessionId(login.getUsername(), token, clientIp);
        if(response == null){
            throw new AuthLoginException("Error de Credenciales de inicio de sesión o cuenta desabilitada");
        }

        return new ResponseEntity<ResponseLogin>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-session")
    public ResponseEntity<ResponseLogin> verifySession(@Valid @RequestBody VerifySessionInput verifySessionInput, HttpServletRequest request) throws Exception {

        ResponseLogin response = authService.verifySession(verifySessionInput);

        if(response == null){
            throw new AuthLoginException("Sesión caducada");
        }

        return new ResponseEntity<ResponseLogin>(response, HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutInput logoutInput) throws Exception {

        authService.logout(logoutInput);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ResponseLogin> sessionData(@PathVariable("sessionId") String sessionId) throws Exception {
        if(sessionId == null || sessionId.trim().isEmpty()){
            throw new AuthLoginException("Remita de forma correcta el sessionId");
        }

        ResponseLogin response = authService.sesionData(sessionId);
        if(response == null){
            throw new AuthLoginException("Session no encontrada");
        }

        return new ResponseEntity<ResponseLogin>(response, HttpStatus.OK);
    }
}
