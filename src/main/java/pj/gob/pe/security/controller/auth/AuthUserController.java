package pj.gob.pe.security.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pj.gob.pe.security.model.keycloak.UserAuth;
import pj.gob.pe.security.model.keycloak.VerifyPasswordResponse;
import pj.gob.pe.security.service.keycloak.UsersServiceAuth;

@Tag(name = "Authorization Users", description = "API para gestionar Authorizaciones de Usuarios")
@RestController
@RequestMapping("/v1/auth/users")
@RequiredArgsConstructor
public class AuthUserController {

    private final UsersServiceAuth userService;

    @Operation(summary = "Consulta Usuario By Username", description = "Retorna Usuario By Username")
    @GetMapping("/{userName}")
    public UserAuth getUser(@PathVariable("userName") String userName) {
        return userService.getUserDetails(userName);
    }

    @PostMapping("/{userName}/verify-password")
    public VerifyPasswordResponse verifyUserPassword(@PathVariable("userName") String userName,
                                                     @RequestParam("password") String password) {

        VerifyPasswordResponse returnValue = new VerifyPasswordResponse(false);

        //password = password.replaceFirst("password=","");

        UserAuth user = userService.getUserDetails(userName, password);

        if (user != null) {
            returnValue.setResult(true);
        }

        return returnValue;
    }
}
