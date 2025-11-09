package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;

/**
 * Cliente REST para comunicarse con el Servicio de Pedidos
 * Permite obtener información de solicitudes para crear rutas
 */
@Component
public class PedidosClient {
    
    private final RestTemplate restTemplate;
    private final String pedidosServiceUrl;

    public PedidosClient(RestTemplate restTemplate, 
                        @Value("${servicio.pedidos.url:http://tpi-api-gateway:8080/api/pedidos}") String pedidosServiceUrl) {
        this.restTemplate = restTemplate;
        this.pedidosServiceUrl = pedidosServiceUrl;
    }

    /**
     * Obtiene una solicitud por su ID
     */
    public SolicitudResponse obtenerSolicitud(Long solicitudId) {
        try {
            String url = pedidosServiceUrl + "/solicitudes/" + solicitudId;
            System.out.println("=== PEDIDOS CLIENT: Obteniendo solicitud ID: " + solicitudId + " ===");
            System.out.println("=== PEDIDOS CLIENT: URL: " + url + " ===");
            
            ResponseEntity<SolicitudResponse> response = restTemplate.getForEntity(url, SolicitudResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println("=== PEDIDOS CLIENT: Solicitud obtenida exitosamente ===");
                return response.getBody();
            } else {
                System.out.println("=== PEDIDOS CLIENT: Error - respuesta vacía o código no exitoso ===");
                throw new RuntimeException("Error al obtener solicitud: respuesta vacía");
            }
        } catch (Exception e) {
            System.out.println("=== PEDIDOS CLIENT: Error al obtener solicitud: " + e.getMessage() + " ===");
            throw new RuntimeException("Error al obtener solicitud ID " + solicitudId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el estado de una solicitud usando PUT (compatible con RestTemplate)
     */
    public boolean actualizarEstadoSolicitud(Long solicitudId, String nuevoEstado) {
        try {
            String url = pedidosServiceUrl + "/solicitudes/" + solicitudId + "/estado?estado=" + nuevoEstado;
            System.out.println("=== PEDIDOS CLIENT: Actualizando estado de solicitud ID: " + solicitudId + " a " + nuevoEstado + " ===");
            System.out.println("=== PEDIDOS CLIENT: URL: " + url + " ===");
            
            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Crear entity vacío para PUT
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            
            // Realizar PUT usando exchange (compatible con RestTemplate nativo)
            ResponseEntity<Object> response = restTemplate.exchange(
                url, 
                HttpMethod.PUT, 
                entity, 
                Object.class
            );
            
            int statusCode = response.getStatusCode().value();
            System.out.println("=== PEDIDOS CLIENT: Respuesta HTTP: " + statusCode + " ===");
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("=== PEDIDOS CLIENT: Estado actualizado exitosamente ===");
                return true;
            } else {
                System.out.println("=== PEDIDOS CLIENT: Error al actualizar estado - código: " + statusCode + " ===");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("=== PEDIDOS CLIENT: Error al actualizar estado: " + e.getMessage() + " ===");
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar estado de solicitud ID " + solicitudId + ": " + e.getMessage(), e);
        }
    }

    /**
     * DTO para recibir datos de solicitudes
     */
    public static class SolicitudResponse {
        private Long id;
        private String numero;
        private String estado;
        private String direccionOrigen;
        private BigDecimal latitudOrigen;
        private BigDecimal longitudOrigen;
        private String direccionDestino;
        private BigDecimal latitudDestino;
        private BigDecimal longitudDestino;
        private String observaciones;
        private ContenedorResponse contenedor;
        
        public SolicitudResponse() {}
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
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
        
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        
        public ContenedorResponse getContenedor() { return contenedor; }
        public void setContenedor(ContenedorResponse contenedor) { this.contenedor = contenedor; }
        
        // Métodos de conveniencia
        public String getOrigenCoordenadas() {
            return latitudOrigen + "," + longitudOrigen;
        }
        
        public String getDestinoCoordenadas() {
            return latitudDestino + "," + longitudDestino;
        }
    }
    
    /**
     * DTO para datos del contenedor dentro de la solicitud
     */
    public static class ContenedorResponse {
        private Long id;
        private String codigo;
        private BigDecimal peso;
        private BigDecimal volumen;
        
        public ContenedorResponse() {}
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public BigDecimal getPeso() { return peso; }
        public void setPeso(BigDecimal peso) { this.peso = peso; }
        
        public BigDecimal getVolumen() { return volumen; }
        public void setVolumen(BigDecimal volumen) { this.volumen = volumen; }
    }
    
    /**
     * DTO para actualización de solicitudes
     */
    public static class SolicitudUpdateRequest {
        private Long id;
        private String numero;
        private String estado;
        private String direccionDestino;
        private BigDecimal latitudDestino;
        private BigDecimal longitudDestino;
        private String direccionOrigen;
        private BigDecimal latitudOrigen;
        private BigDecimal longitudOrigen;
        private String observaciones;
        
        public SolicitudUpdateRequest() {}
        
        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
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
        
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }
    
    /**
     * MEJORA: Actualiza una solicitud a estado ENTREGADA cuando todos sus tramos están completados
     * @param solicitudId ID de la solicitud a actualizar
     * @param costoFinalReal Costo total real de todos los tramos
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarSolicitudAEntregada(Long solicitudId, Double costoFinalReal) {
        try {
            System.out.println("=== PEDIDOS CLIENT: Marcando solicitud " + solicitudId + " como ENTREGADA ===");
            System.out.println("=== PEDIDOS CLIENT: Costo final real: $" + costoFinalReal + " ===");
            
            // Primero actualizar el estado a ENTREGADA
            boolean estadoActualizado = actualizarEstadoSolicitud(solicitudId, "ENTREGADA");
            
            if (estadoActualizado) {
                System.out.println("=== PEDIDOS CLIENT: ✅ Solicitud " + solicitudId + " marcada como ENTREGADA exitosamente ===");
                
                // TODO: Si el ServicioPedidos tiene endpoint para actualizar costo final, agregarlo aquí
                // Por ahora solo actualizamos el estado
                
                return true;
            } else {
                System.err.println("=== PEDIDOS CLIENT: ❌ Error al actualizar estado de solicitud " + solicitudId + " ===");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("=== PEDIDOS CLIENT: ❌ Error actualizando solicitud a ENTREGADA: " + e.getMessage() + " ===");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Marca una solicitud como entregada usando el endpoint específico
     * @param solicitudId ID de la solicitud a marcar como entregada
     * @return true si fue exitoso, false en caso contrario
     */
    public boolean marcarSolicitudComoEntregada(Long solicitudId) {
        try {
            String url = pedidosServiceUrl + "/solicitudes/" + solicitudId + "/marcar-entregada";
            System.out.println("=== PEDIDOS CLIENT: Marcando solicitud como entregada - URL: " + url + " ===");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("=== PEDIDOS CLIENT: ✅ Solicitud " + solicitudId + " marcada como entregada exitosamente ===");
                return true;
            } else {
                System.err.println("=== PEDIDOS CLIENT: ❌ Error al marcar solicitud como entregada - Código: " + response.getStatusCode() + " ===");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("=== PEDIDOS CLIENT: ❌ Error marcando solicitud como entregada: " + e.getMessage() + " ===");
            e.printStackTrace();
            return false;
        }
    }
}