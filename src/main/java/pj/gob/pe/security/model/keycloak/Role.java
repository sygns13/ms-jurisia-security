package pj.gob.pe.security.model.keycloak;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Authorization Role Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private String id;
    private String role;
}
