package pj.gob.pe.security.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime fecha;
    private String mensaje;
    private String detalles;

}
