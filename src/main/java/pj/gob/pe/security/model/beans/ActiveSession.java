package pj.gob.pe.security.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Schema(description = "ActiveSession Data Model")
@RedisHash("ActiveSession")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSession {

    private long exp; // Expiración del token
    private long iat; // Fecha de emisión
    private String jti; // ID único del token
    private String iss; // Issuer (URL de Keycloak)
    private String sub; // Identificador del usuario
    private String typ; // Tipo de token (Bearer)
    private String azp; // Cliente autorizado
    private String sid; // ID de sesión
    private String acr; // Nivel de autenticación
    private String scope; // Alcances del token

    @JsonProperty("email_verified")
    private boolean emailVerified;

    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String email;

    @JsonProperty("client_id")
    private String clientId;

    private String username;

    @JsonProperty("token_type")
    private String tokenType;

    private boolean active;
}
