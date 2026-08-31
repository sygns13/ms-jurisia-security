package pj.gob.pe.security.utils.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Input para que el dueño de la sesión cambie su propia contraseña")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputCambioPassword {

    @Schema(description = "Contraseña actual del Usuario, se verifica antes de aplicar el cambio")
    @NotNull(message = "Ingrese su contraseña actual")
    @Size(min = 1, max = 250, message = "La contraseña actual debe tener entre 1 y 250 caracteres")
    private String passwordActual;

    @Schema(description = "Nueva contraseña del Usuario")
    @NotNull(message = "Ingrese su nueva contraseña")
    @Size(min = 1, max = 250, message = "La nueva contraseña debe tener entre 1 y 250 caracteres")
    private String passwordNuevo;
}
