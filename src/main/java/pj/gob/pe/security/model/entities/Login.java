package pj.gob.pe.security.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Logins Model")
@Entity
@Table(name = "Logins")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Fecha de Login")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="fecha", nullable = true)
    private LocalDate fecha;

    @Schema(description = "Hora de Login")
    @JsonFormat(pattern="HH:mm:ss")
    @Column(name="hora", nullable = true)
    private LocalTime hora;

    @Schema(description = "IP del Login")
    @Column(name = "ip", nullable = true, length = 60)
    private String ip;

    @Schema(description = "MAC del Login")
    @Column(name = "mac", nullable = true, length = 60)
    private String mac;

    @Schema(description = "Users")
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_logins_users1"))
    private User user;
}
