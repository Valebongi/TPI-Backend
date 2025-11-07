package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOLICITUDES")
public class Solicitud {
    
    @Id
    @SequenceGenerator(name = "seq_solicitud", sequenceName = "SEQ_SOLICITUD_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_solicitud")
    @Column(name = "ID_SOLICITUD")
    private Long id;

    @Column(name = "NUMERO", nullable = false, unique = true, length = 50)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CONTENEDOR", nullable = false)
    @NotNull(message = "El contenedor es obligatorio")
    private Contenedor contenedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)  
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @Column(name = "COSTO_ESTIMADO", precision = 12, scale = 2)
    private BigDecimal costoEstimado;

    @Column(name = "TIEMPO_ESTIMADO_HORAS")
    private Integer tiempoEstimadoHoras;

    @Column(name = "COSTO_FINAL", precision = 12, scale = 2)
    private BigDecimal costoFinal;

    @Column(name = "TIEMPO_REAL_HORAS")
    private Integer tiempoRealHoras;

    @Column(name = "ESTADO", nullable = false, length = 50)
    private String estado; // BORRADOR, PROGRAMADA, EN_TRANSITO, ENTREGADA, CANCELADA

    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FECHA_PROGRAMACION")
    private LocalDateTime fechaProgramacion;

    @Column(name = "FECHA_INICIO_TRANSITO")
    private LocalDateTime fechaInicioTransito;

    @Column(name = "FECHA_ENTREGA")
    private LocalDateTime fechaEntrega;

    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    @Column(name = "RUTA_ID")
    private Long rutaId; // ID de la ruta en el servicio de logística

    // Campos de destino (movidos desde Contenedor)
    @Column(name = "DIRECCION_DESTINO", length = 500)
    private String direccionDestino;

    @Column(name = "LATITUD_DESTINO", precision = 10, scale = 8)
    private BigDecimal latitudDestino;

    @Column(name = "LONGITUD_DESTINO", precision = 11, scale = 8)
    private BigDecimal longitudDestino;

    // Campos de origen (obtenidos del depósito del contenedor)
    @Column(name = "DIRECCION_ORIGEN", length = 500)
    private String direccionOrigen;

    @Column(name = "LATITUD_ORIGEN", precision = 10, scale = 8)
    private BigDecimal latitudOrigen;

    @Column(name = "LONGITUD_ORIGEN", precision = 11, scale = 8)
    private BigDecimal longitudOrigen;

    // Constructors
    public Solicitud() {
        this.numero = generateSolicitudNumber();
        this.estado = "BORRADOR";
        this.fechaCreacion = LocalDateTime.now();
    }

    public Solicitud(Long id, String numero, Contenedor contenedor, Cliente cliente, 
                    BigDecimal costoEstimado, Integer tiempoEstimadoHoras, BigDecimal costoFinal,
                    Integer tiempoRealHoras, String estado, LocalDateTime fechaCreacion,
                    LocalDateTime fechaProgramacion, LocalDateTime fechaInicioTransito,
                    LocalDateTime fechaEntrega, String observaciones, Long rutaId,
                    String direccionDestino, BigDecimal latitudDestino, BigDecimal longitudDestino) {
        this.id = id;
        this.numero = numero;
        this.contenedor = contenedor;
        this.cliente = cliente;
        this.costoEstimado = costoEstimado;
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
        this.costoFinal = costoFinal;
        this.tiempoRealHoras = tiempoRealHoras;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaProgramacion = fechaProgramacion;
        this.fechaInicioTransito = fechaInicioTransito;
        this.fechaEntrega = fechaEntrega;
        this.observaciones = observaciones;
        this.rutaId = rutaId;
        this.direccionDestino = direccionDestino;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Contenedor getContenedor() { return contenedor; }
    public void setContenedor(Contenedor contenedor) { this.contenedor = contenedor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public BigDecimal getCostoEstimado() { return costoEstimado; }
    public void setCostoEstimado(BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; }

    public Integer getTiempoEstimadoHoras() { return tiempoEstimadoHoras; }
    public void setTiempoEstimadoHoras(Integer tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; }

    public BigDecimal getCostoFinal() { return costoFinal; }
    public void setCostoFinal(BigDecimal costoFinal) { this.costoFinal = costoFinal; }

    public Integer getTiempoRealHoras() { return tiempoRealHoras; }
    public void setTiempoRealHoras(Integer tiempoRealHoras) { this.tiempoRealHoras = tiempoRealHoras; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaProgramacion() { return fechaProgramacion; }
    public void setFechaProgramacion(LocalDateTime fechaProgramacion) { this.fechaProgramacion = fechaProgramacion; }

    public LocalDateTime getFechaInicioTransito() { return fechaInicioTransito; }
    public void setFechaInicioTransito(LocalDateTime fechaInicioTransito) { this.fechaInicioTransito = fechaInicioTransito; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Long getRutaId() { return rutaId; }
    public void setRutaId(Long rutaId) { this.rutaId = rutaId; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public BigDecimal getLatitudDestino() { return latitudDestino; }
    public void setLatitudDestino(BigDecimal latitudDestino) { this.latitudDestino = latitudDestino; }

    public BigDecimal getLongitudDestino() { return longitudDestino; }
    public void setLongitudDestino(BigDecimal longitudDestino) { this.longitudDestino = longitudDestino; }

    public String getDireccionOrigen() { return direccionOrigen; }
    public void setDireccionOrigen(String direccionOrigen) { this.direccionOrigen = direccionOrigen; }

    public BigDecimal getLatitudOrigen() { return latitudOrigen; }
    public void setLatitudOrigen(BigDecimal latitudOrigen) { this.latitudOrigen = latitudOrigen; }

    public BigDecimal getLongitudOrigen() { return longitudOrigen; }
    public void setLongitudOrigen(BigDecimal longitudOrigen) { this.longitudOrigen = longitudOrigen; }
    
    // Métodos helper para comunicación con servicios de logística
    public String getDestinoCoordenadas() {
        if (latitudDestino != null && longitudDestino != null) {
            return latitudDestino + "," + longitudDestino;
        }
        return null;
    }
    
    public String getOrigenCoordenadas() {
        if (latitudOrigen != null && longitudOrigen != null) {
            return latitudOrigen + "," + longitudOrigen;
        }
        return null;
    }
    
    public String getDestinoDescripcion() {
        return direccionDestino;
    }

    public String getOrigenDescripcion() {
        return direccionOrigen;
    }

    // Builder pattern
    public static SolicitudBuilder builder() {
        return new SolicitudBuilder();
    }

    public static class SolicitudBuilder {
        private Solicitud solicitud = new Solicitud();

        public SolicitudBuilder contenedor(Contenedor contenedor) {
            solicitud.setContenedor(contenedor);
            return this;
        }

        public SolicitudBuilder cliente(Cliente cliente) {
            solicitud.setCliente(cliente);
            return this;
        }

        public SolicitudBuilder estado(String estado) {
            solicitud.setEstado(estado);
            return this;
        }

        public SolicitudBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            solicitud.setFechaCreacion(fechaCreacion);
            return this;
        }

        public Solicitud build() {
            return solicitud;
        }
    }

    private static String generateSolicitudNumber() {
        return "SOL-" + System.currentTimeMillis();
    }
}