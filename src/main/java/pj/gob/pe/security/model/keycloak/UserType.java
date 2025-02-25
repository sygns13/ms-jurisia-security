package pj.gob.pe.security.model.keycloak;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Authorization TypeUser Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserType {

    private String id;
    private String realmRole;
}
