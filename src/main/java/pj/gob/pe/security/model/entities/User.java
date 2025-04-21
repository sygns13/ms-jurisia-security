package pj.gob.pe.security.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "User Model")
@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Tipo Documento de Usuario " +
            "1-> DNI\n" +
            "2->RUC\n" +
            "3->CE\n" +
            "4->PS")
    @Column(name="tipoDocumento", nullable = true)
    @NotNull( message = "{users.tipodocumento.notnull}")
    private Integer tipoDocumento;

    @Schema(description = "Documento del Usuario")
    @NotNull( message = "{users.documento.notnull}")
    @Size(min = 1, max = 20, message = "{users.documento.size}")
    @Column(name="documento", nullable = true, length = 20)
    private String documento;

    @Schema(description = "Apellidos del Usuario")
    @NotNull( message = "{users.apellidos.notnull}")
    @Size(min = 1, max = 150, message = "{users.apellidos.size}")
    @Column(name="apellidos", nullable = true, length = 150)
    private String apellidos;

    @Schema(description = "Nombres del Usuario")
    @NotNull( message = "{users.nombres.notnull}")
    @Size(min = 1, max = 150, message = "{users.nombres.size}")
    @Column(name="nombres", nullable = true, length = 150)
    private String nombres;

    @Schema(description = "Dependencia")
    @ManyToOne
    @JoinColumn(name = "dependenciaId", nullable = false, foreignKey = @ForeignKey(name = "fk_users_Dependencias1"))
    private Dependencia dependencia;

    @Schema(description = "Cargo")
    @ManyToOne
    @JoinColumn(name = "cargoId", nullable = false, foreignKey = @ForeignKey(name = "fk_users_Cargos1"))
    private Cargo cargo;

    @Schema(description = "Username del Usuario")
    @NotNull( message = "{users.username.notnull}")
    @Size(min = 1, max = 250, message = "{users.username.size}")
    @Column(name="username", nullable = true, length = 250)
    private String username;

    @Schema(description = "Password del Usuario")
    @NotNull( message = "{users.password.notnull}")
    @Size(min = 1, max = 250, message = "{users.password.size}")
    @Column(name="password", nullable = true, length = 250)
    @JsonIgnore
    private String password;

    @Schema(description = "Email del Usuario")
    //@NotNull( message = "{users.email.notnull}")
    //@Size(min = 1, max = 250, message = "{users.email.size}")
    @Email(message = "{users.email.email}")
    @Column(name="email", nullable = true, length = 250)
    private String email;

    @Schema(description = "Tipo de usuario")
    @ManyToOne
    @JoinColumn(name = "tipoUserId", nullable = false, foreignKey = @ForeignKey(name = "fk_users_tipoUsers1"))
    private TipoUser tipoUser;

    @Schema(description = "Genero del Usuario " +
            "1-> Masculino\n" +
            "0->Femenino")
    @Column(name="genero", nullable = true)
    private Integer genero;

    @Schema(description = "Telefono del Usuario")
    @Column(name="telefono", nullable = true, length = 45)
    private String telefono;

    @Schema(description = "Direccion del Usuario")
    @Column(name="direccion", nullable = true, length = 500)
    private String direccion;

    @Schema(description = "Estado del Registro")
    @Column(name="activo", nullable = true)
    private Integer activo;

    @Schema(description = "Borrado Lógico del Registro")
    @Column(name="borrado", nullable = true)
    private Integer borrado;

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


    @Schema(description = "Logins del Usuario")
    @JsonIgnore
    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    private List<Login> logins;

    @Schema(description = "Aplicacion")
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Aplicacion> aplicacion;

    @Schema(description = "Roles")
    @ManyToMany
    @JoinTable(
            name = "UsersHasRoles", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "userId"), // Clave foránea a User
            inverseJoinColumns = @JoinColumn(name = "roleId") // Clave foránea a Roles
    )
    private List<Role> roles;

}
