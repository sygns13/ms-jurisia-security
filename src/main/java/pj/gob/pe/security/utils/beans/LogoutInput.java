package pj.gob.pe.security.utils.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Input Logout Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutInput {

    @Schema(description = "Client Id de Inicio de sesion")
    @NotNull( message = "{users.client_id.notnull}")
    @Size(min = 1, message = "{users.client_id.size}")
    @JsonProperty("client_id")
    private String clientId;

    @Schema(description = "Client Secret de Inicio de sesion")
    @NotNull( message = "{users.client_secret.notnull}")
    @Size(min = 1, message = "{users.client_secret.size}")
    @JsonProperty("client_secret")
    private String clientSecret;

    @Schema(description = "Token de sesion")
    @NotNull( message = "{users.token.notnull}")
    @Size(min = 1, message = "{users.token.size}")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Token de Refresco de sesion")
    @NotNull( message = "{users.refresh_token.notnull}")
    @Size(min = 1, message = "{users.refresh_token.size}")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Username del Usuario")
    @NotNull( message = "{users.userSessionsId.notnull}")
    @Size(min = 1, max = 250, message = "{users.userSessionsId.size}")
    private String userSessionsId;
}
