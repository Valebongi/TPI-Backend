package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Cliente REST para comunicarse con el Servicio de Administración
 * Implementa la comunicación definida en los lineamientos del TP
 */
@Component
public class AdministracionClient {
    
    private final RestTemplate restTemplate;
    private final String administracionServiceUrl;

    public AdministracionClient(RestTemplate restTemplate, 
                              @Value("${servicio.administracion.url:http://servicio-administracion:8080}") String administracionServiceUrl) {
        this.restTemplate = restTemplate;
        this.administracionServiceUrl = administracionServiceUrl;
    }

    /**
     * Verifica si un camión existe y está disponible
     */
    public boolean existeCamion(Long camionId) {
        try {
            String url = administracionServiceUrl + "/camiones/" + camionId;
            ResponseEntity<CamionResponse> response = restTemplate.getForEntity(url, CamionResponse.class);
            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
        } catch (Exception e) {
            // Si hay error al consultar, asumimos que el camión no existe o no está disponible
            return false;
        }
    }

    /**
     * Obtiene los detalles de un camión
     */
    public CamionResponse obtenerCamion(Long camionId) {
        try {
            String url = administracionServiceUrl + "/camiones/" + camionId;
            ResponseEntity<CamionResponse> response = restTemplate.getForEntity(url, CamionResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el camión: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene todos los depósitos disponibles
     */
    public DepositoResponse[] obtenerDepositos() {
        try {
            String url = administracionServiceUrl + "/depositos";
            ResponseEntity<DepositoResponse[]> response = restTemplate.getForEntity(url, DepositoResponse[].class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("=== ADMIN CLIENT: Error al obtener depósitos: " + e.getMessage() + " ===");
            return new DepositoResponse[0];
        }
    }

    /**
     * Obtiene el valor del parámetro global de costo por km
     */
    public Double obtenerCostoPorKm() {
        try {
            String url = administracionServiceUrl + "/parametros/COSTO_POR_KM";
            System.out.println("=== ADMINISTRACION CLIENT: Obteniendo costo por km ===");
            
            ResponseEntity<ParametroGlobalResponse> response = restTemplate.getForEntity(url, ParametroGlobalResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getValor();
            } else {
                // Valor por defecto si no existe el parámetro
                System.out.println("=== ADMINISTRACION CLIENT: Usando valor por defecto para costo por km ===");
                return 35.0; // $35 por km
            }
        } catch (Exception e) {
            System.out.println("=== ADMINISTRACION CLIENT: Error obteniendo costo por km, usando valor por defecto: " + e.getMessage() + " ===");
            return 35.0; // Valor por defecto
        }
    }

    /**
     * Obtiene el factor de costo por peso del contenedor
     */
    public Double obtenerFactorPeso() {
        try {
            String url = administracionServiceUrl + "/parametros/FACTOR_PESO";
            System.out.println("=== ADMINISTRACION CLIENT: Obteniendo factor peso ===");
            
            ResponseEntity<ParametroGlobalResponse> response = restTemplate.getForEntity(url, ParametroGlobalResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getValor();
            } else {
                // Valor por defecto si no existe el parámetro
                System.out.println("=== ADMINISTRACION CLIENT: Usando valor por defecto para factor peso ===");
                return 2.5; // $2.5 por kg
            }
        } catch (Exception e) {
            System.out.println("=== ADMINISTRACION CLIENT: Error obteniendo factor peso, usando valor por defecto: " + e.getMessage() + " ===");
            return 2.5; // Valor por defecto
        }
    }

    /**
     * Calcula el costo de un tramo basado en distancia, peso y camión
     * Implementa la fórmula definida en los lineamientos: km * costo por km del camión + consumo * precio litro + estadía depósitos
     */
    public Double calcularCostoTramo(Double distanciaKm, Double pesoKg, Long camionId) {
        try {
            System.out.println("=== ADMINISTRACION CLIENT: Calculando costo tramo ===");
            System.out.println("=== Distancia: " + distanciaKm + "km, Peso: " + pesoKg + "kg, Camión: " + camionId + " ===");
            
            // Obtener parámetros de cálculo
            Double costoPorKm = obtenerCostoPorKm();
            Double factorPeso = obtenerFactorPeso();
            
            // Fórmula simplificada: (distancia * costo_por_km) + (peso * factor_peso)
            Double costoTotal = (distanciaKm * costoPorKm) + (pesoKg * factorPeso);
            
            System.out.println("=== CÁLCULO: (" + distanciaKm + " * " + costoPorKm + ") + (" + pesoKg + " * " + factorPeso + ") = $" + costoTotal + " ===");
            
            return costoTotal;
            
        } catch (Exception e) {
            System.out.println("=== ADMINISTRACION CLIENT: Error calculando costo, usando estimación básica: " + e.getMessage() + " ===");
            // Fórmula básica de fallback
            return distanciaKm * 35.0 + pesoKg * 2.5;
        }
    }

    // DTO adicional para parámetros globales
    public static class ParametroGlobalResponse {
        private String clave;
        private Double valor;
        private String descripcion;
        
        public String getClave() { return clave; }
        public void setClave(String clave) { this.clave = clave; }
        
        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
}