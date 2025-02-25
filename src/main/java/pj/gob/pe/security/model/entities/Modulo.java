package pj.gob.pe.security.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Modulo Model")
@Entity
@Table(name = "Modulos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Modulo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Codigo del Modulo")
    @Column(name = "codigo", nullable = true, length = 20)
    private String codigo;

    @Schema(description = "Nombre del Modulo")
    @Column(name = "nombre", nullable = true, length = 100)
    private String nombre;

    @Schema(description = "Descripcion del Modulo")
    @Column(name = "descripcion", nullable = true, length = 250)
    private String descripcion;

    @Schema(description = "Estado del Modulo 1 -> Activo | 0 -> Inactivo")
    @Column(name="estado", nullable = true)
    private Integer estado;

    @Schema(description = "Aplicacion")
    @ManyToOne
    @JoinColumn(name = "aplicacionId", nullable = false, foreignKey = @ForeignKey(name = "fk_modulo_aplicacion"))
    private Aplicacion aplicacion;

    @ManyToMany
    @JoinTable(
            name = "RolesHasModulos", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "moduloId"), // Clave foránea a Modulo
            inverseJoinColumns = @JoinColumn(name = "roleId") // Clave foránea a User
    )
    private List<Role> roles;
}
