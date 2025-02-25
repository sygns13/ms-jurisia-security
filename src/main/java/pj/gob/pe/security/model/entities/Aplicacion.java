package pj.gob.pe.security.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Aplicacion Model")
@Entity
@Table(name = "Aplicacions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aplicacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Realm de la Aplicacion")
    @Column(name = "realm", nullable = true, length = 50)
    private String realm;

    @Schema(description = "Codigo de la Aplicacion")
    @Column(name = "codigo", nullable = true, length = 20)
    private String codigo;

    @Schema(description = "Nombre de la Aplicacion")
    @Column(name = "nombre", nullable = true, length = 200)
    private String nombre;

    @Schema(description = "Siglas de la Aplicacion")
    @Column(name = "sigla", nullable = true, length = 50)
    private String sigla;

    @Schema(description = "Descripcion de la Aplicacion")
    @Column(name = "descripcion", nullable = true, length = 500)
    private String descripcion;

    @Schema(description = "Estado de la Aplicacion 1 -> Activo | 0 -> Inactivo")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Modulos de la Aplicacion")
    @JsonIgnore
    @OneToMany(mappedBy = "aplicacion" , fetch = FetchType.LAZY)
    private List<Modulo> modulos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AplicacionsHasUsers", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "aplicacionId"), // Clave foránea a Aplicacion
            inverseJoinColumns = @JoinColumn(name = "userId") // Clave foránea a User
    )
    @JsonIgnore
    private List<User> users;
}
