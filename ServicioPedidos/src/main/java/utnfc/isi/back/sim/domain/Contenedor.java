package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "CONTENEDORES")
public class Contenedor {
    
    @Id
    @SequenceGenerator(name = "seq_contenedor", sequenceName = "SEQ_CONTENEDOR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contenedor")
    @Column(name = "ID_CONTENEDOR")
    private Long id;

    @Column(name = "CODIGO", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El código del contenedor es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @Column(name = "PESO", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0")
    private BigDecimal peso;

    @Column(name = "VOLUMEN", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El volumen es obligatorio")
    @DecimalMin(value = "0.01", message = "El volumen debe ser mayor a 0")
    private BigDecimal volumen;

    @Column(name = "ESTADO", nullable = false, length = 50)
    @NotBlank(message = "El estado es obligatorio")
    private String estado; // REGISTRADO, EN_TRANSITO, EN_DEPOSITO, ENTREGADO

    @Column(name = "DESCRIPCION", length = 500)
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @Column(name = "DIRECCION_ORIGEN", nullable = false, length = 500)
    @NotBlank(message = "La dirección de origen es obligatoria")
    @Size(max = 500, message = "La dirección de origen no puede exceder 500 caracteres")
    private String direccionOrigen;

    @Column(name = "LATITUD_ORIGEN", nullable = false, precision = 10, scale = 8)
    @NotNull(message = "La latitud de origen es obligatoria")
    private BigDecimal latitudOrigen;

    @Column(name = "LONGITUD_ORIGEN", nullable = false, precision = 11, scale = 8)
    @NotNull(message = "La longitud de origen es obligatoria")
    private BigDecimal longitudOrigen;

    @Column(name = "DIRECCION_DESTINO", nullable = false, length = 500)
    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(max = 500, message = "La dirección de destino no puede exceder 500 caracteres")
    private String direccionDestino;

    @Column(name = "LATITUD_DESTINO", nullable = false, precision = 10, scale = 8)
    @NotNull(message = "La latitud de destino es obligatoria")
    private BigDecimal latitudDestino;

    @Column(name = "LONGITUD_DESTINO", nullable = false, precision = 11, scale = 8)
    @NotNull(message = "La longitud de destino es obligatoria")
    private BigDecimal longitudDestino;

    // Constructors
    public Contenedor() {
        this.estado = "REGISTRADO";
    }

    public Contenedor(Long id, String codigo, BigDecimal peso, BigDecimal volumen, String estado,
                     String descripcion, Cliente cliente, String direccionOrigen, BigDecimal latitudOrigen,
                     BigDecimal longitudOrigen, String direccionDestino, BigDecimal latitudDestino,
                     BigDecimal longitudDestino) {
        this.id = id;
        this.codigo = codigo;
        this.peso = peso;
        this.volumen = volumen;
        this.estado = estado;
        this.descripcion = descripcion;
        this.cliente = cliente;
        this.direccionOrigen = direccionOrigen;
        this.latitudOrigen = latitudOrigen;
        this.longitudOrigen = longitudOrigen;
        this.direccionDestino = direccionDestino;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getVolumen() { return volumen; }
    public void setVolumen(BigDecimal volumen) { this.volumen = volumen; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getDireccionOrigen() { return direccionOrigen; }
    public void setDireccionOrigen(String direccionOrigen) { this.direccionOrigen = direccionOrigen; }

    public BigDecimal getLatitudOrigen() { return latitudOrigen; }
    public void setLatitudOrigen(BigDecimal latitudOrigen) { this.latitudOrigen = latitudOrigen; }

    public BigDecimal getLongitudOrigen() { return longitudOrigen; }
    public void setLongitudOrigen(BigDecimal longitudOrigen) { this.longitudOrigen = longitudOrigen; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public BigDecimal getLatitudDestino() { return latitudDestino; }
    public void setLatitudDestino(BigDecimal latitudDestino) { this.latitudDestino = latitudDestino; }

    public BigDecimal getLongitudDestino() { return longitudDestino; }
    public void setLongitudDestino(BigDecimal longitudDestino) { this.longitudDestino = longitudDestino; }
}