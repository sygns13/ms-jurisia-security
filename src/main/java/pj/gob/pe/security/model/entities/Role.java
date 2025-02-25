package pj.gob.pe.security.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Role Model")
@Entity
@Table(name = "Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del Rol")
    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Schema(description = "Descripcion del Rol")
    @Column(name = "descripcion", nullable = true, length = 200)
    private String descripcion;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Modulo> modulos;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;
}
