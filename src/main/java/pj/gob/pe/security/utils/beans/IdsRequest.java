package pj.gob.pe.security.utils.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Input con la lista de identificadores a asignar")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdsRequest {

    @Schema(description = "Identificadores que quedarán asignados. Una lista vacía deja al registro sin asignaciones")
    private List<Long> ids;
}
