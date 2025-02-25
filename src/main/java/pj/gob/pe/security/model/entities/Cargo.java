package pj.gob.pe.security.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Cargo de Usuario Model")
@Entity
@Table(name = "Cargos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Código de Cargo de Usuario")
    @NotNull( message = "{cargos.codigo.notnull}")
    @Size(min = 1, max = 20, message = "{cargos.codigo.size}")
    @Column(name = "codigo", nullable = true, length = 20)
    private String codigo;

    @Schema(description = "Nombre de Cargo de Usuario")
    @NotNull( message = "{cargos.nombre.notnull}")
    @Size(min = 1, max = 200, message = "{cargos.nombre.size}")
    @Column(name = "nombre", nullable = true, length = 200)
    private String nombre;

    @Schema(description = "Siglas de Cargo de Usuario")
    @Size(max = 25, message = "{cargos.sigla.size}")
    @Column(name = "sigla", nullable = true, length = 25)
    private String sigla;

    @Schema(description = "Fecha de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="regDate", nullable = true)
    private LocalDate regDate;

    @Schema(description = "Fecha y Hora de Creación del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="regDatetime", nullable = true)
    private LocalDateTime regDatetime;

    @Schema(description = "Epoch de Creación del Registro")
    @Column(name="regTimestamp", nullable = true)
    private Long regTimestamp;

    @Schema(description = "Usuario que insertó el registro")
    @Column(name="regUserId", nullable = true)
    private Long regUserId;

    @Schema(description = "Fecha de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="updDate", nullable = true)
    private LocalDate updDate;

    @Schema(description = "Fecha y Hora de Edición del Registro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="updDatetime", nullable = true)
    private LocalDateTime updDatetime;

    @Schema(description = "Epoch de Edición del Registro")
    @Column(name="updTimestamp", nullable = true)
    private Long updTimestamp;

    @Schema(description = "Usuario que editó el registro")
    @Column(name="updUserId", nullable = true)
    private Long updUserId;

    @Schema(description = "Estado del Registro")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico del Registro")
    @Column(name="borrado", nullable = true)
    private Integer borrado;

    @Schema(description = "Users")
    @JsonIgnore
    @OneToMany(mappedBy = "cargo" , fetch = FetchType.LAZY)
    private List<User> users;

}
