package pj.gob.pe.security.model.keycloak;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Schema(description = "Authorization Client Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private String appId;
    private String clientId;
    private Set<Role> roles;

}
