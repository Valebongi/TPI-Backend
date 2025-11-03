package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "CLIENTES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {
    
    @Id
    @SequenceGenerator(name = "seq_cliente", sequenceName = "SEQ_CLIENTE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
    @Column(name = "ID_CLIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    @Column(name = "TELEFONO", length = 20)
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @Column(name = "DIRECCION", length = 500)
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccion;

    @Column(name = "KEYCLOAK_ID", length = 100)
    @Size(max = 100, message = "El ID de Keycloak no puede exceder 100 caracteres")
    private String keycloakId;

    @Column(name = "ACTIVO")
    @Builder.Default
    private Boolean activo = true;
}