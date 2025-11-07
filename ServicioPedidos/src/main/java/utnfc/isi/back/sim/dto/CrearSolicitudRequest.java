package utnfc.isi.back.sim.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para la creación de solicitudes con destino incluido
 * Reemplaza el anterior CrearSolicitudRequest que solo tenía clienteId y contenedorId
 */
public class CrearSolicitudRequest {
    
    @NotNull(message = "El cliente ID es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El contenedor ID es obligatorio")
    private Long contenedorId;
    
    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(max = 500, message = "La dirección de destino no puede exceder 500 caracteres")
    private String direccionDestino;
    
    @NotNull(message = "La latitud de destino es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
    private BigDecimal latitudDestino;
    
    @NotNull(message = "La longitud de destino es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
    private BigDecimal longitudDestino;
    
    // Constructores
    public CrearSolicitudRequest() {}
    
    public CrearSolicitudRequest(Long clienteId, Long contenedorId, String direccionDestino, 
                                BigDecimal latitudDestino, BigDecimal longitudDestino) {
        this.clienteId = clienteId;
        this.contenedorId = contenedorId;
        this.direccionDestino = direccionDestino;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
    }
    
    // Getters y Setters
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public Long getContenedorId() {
        return contenedorId;
    }
    
    public void setContenedorId(Long contenedorId) {
        this.contenedorId = contenedorId;
    }
    
    public String getDireccionDestino() {
        return direccionDestino;
    }
    
    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }
    
    public BigDecimal getLatitudDestino() {
        return latitudDestino;
    }
    
    public void setLatitudDestino(BigDecimal latitudDestino) {
        this.latitudDestino = latitudDestino;
    }
    
    public BigDecimal getLongitudDestino() {
        return longitudDestino;
    }
    
    public void setLongitudDestino(BigDecimal longitudDestino) {
        this.longitudDestino = longitudDestino;
    }
    
    @Override
    public String toString() {
        return "CrearSolicitudRequest{" +
                "clienteId=" + clienteId +
                ", contenedorId=" + contenedorId +
                ", direccionDestino='" + direccionDestino + '\'' +
                ", latitudDestino=" + latitudDestino +
                ", longitudDestino=" + longitudDestino +
                '}';
    }
}