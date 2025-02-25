package pj.gob.pe.security.model.keycloak;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Verificacion de Password Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPasswordResponse {

    private boolean result;
}
