package pj.gob.pe.security.model.keycloak;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Authorization User Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuth {

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String userId;
    private boolean enabled;
    private boolean emailVerified;
    private UserType userType;
    private List<Client> clients;
}
