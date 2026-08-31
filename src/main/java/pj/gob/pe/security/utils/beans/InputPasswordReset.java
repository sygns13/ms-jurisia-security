package pj.gob.pe.security.utils.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Input para restablecer la contraseña de un Usuario desde la administración")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputPasswordReset {

    @Schema(description = "Nueva contraseña del Usuario")
    @NotNull(message = "Ingrese la nueva contraseña del Usuario")
    @Size(min = 1, max = 250, message = "La nueva contraseña debe tener entre 1 y 250 caracteres")
    private String passwordNuevo;
}
