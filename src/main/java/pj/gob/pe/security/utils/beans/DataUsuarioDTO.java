package pj.gob.pe.security.utils.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "Data Usuario SIJ Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dni;
    private String apePaterno;
    private String apeMaterno;
    private String nombres;
    private String usuario;
    private String desper;
    private Short instanciaCheck;
}
