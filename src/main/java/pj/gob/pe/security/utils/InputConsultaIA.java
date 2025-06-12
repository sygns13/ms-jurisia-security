package pj.gob.pe.security.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Input Consulta IA Filters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputConsultaIA {

    @Schema(description = "Documento de Identidad de Usuario")
    private String documento;

    @Schema(description = "Apellidos de Usuario")
    private String apellidos;

    @Schema(description = "Nombres de Usuario")
    private String nombres;

    @Schema(description = "Cargo de Usuario")
    private String cargo;

    @Schema(description = "Username de Usuario")
    private String username;

    @Schema(description = "Email de Usuario")
    private String email;
}
