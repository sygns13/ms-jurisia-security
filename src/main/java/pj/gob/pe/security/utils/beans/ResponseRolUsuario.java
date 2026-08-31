package pj.gob.pe.security.utils.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Rol asignado a un Usuario, con el detalle de si llega o no a Keycloak")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRolUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Identificador del Rol")
    private Long id;

    @Schema(description = "Nombre del Rol (coincide con el realm role de Keycloak)")
    private String name;

    @Schema(description = "Descripcion del Rol")
    private String descripcion;

    @Schema(description = "Indica si el Rol se propaga a Keycloak. Es verdadero solo cuando el Rol " +
            "está asociado a un Módulo de alguna de las Aplicaciones del Usuario, porque así lo " +
            "resuelve el proveedor de usuarios remoto en /v1/auth/users/{userName}")
    private boolean efectivoEnKeycloak;

    @Schema(description = "Módulos por los que el Rol resulta efectivo para este Usuario")
    private List<String> modulos;

    @Schema(description = "Motivo por el que el Rol no es efectivo. Nulo cuando sí lo es")
    private String advertencia;
}
