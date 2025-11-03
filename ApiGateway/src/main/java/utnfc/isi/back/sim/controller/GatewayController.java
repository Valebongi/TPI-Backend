package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
public class GatewayController {

    @Autowired
    private RouteLocator routeLocator;

    @GetMapping("/")
    public Mono<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("servicio", "API Gateway - TPI Backend");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("descripcion", "Gateway central para microservicios de transporte de contenedores");
        
        // Información de los microservicios
        Map<String, Object> microservicios = new HashMap<>();
        
        Map<String, Object> administracion = new HashMap<>();
        administracion.put("nombre", "Servicio de Administración");
        administracion.put("puerto", 8081);
        administracion.put("rutas", List.of(
            "GET /api/admin/camiones - Listar todos los camiones",
            "POST /api/admin/camiones - Crear nuevo camión",
            "GET /api/admin/depositos - Listar todos los depósitos",
            "POST /api/admin/depositos - Crear nuevo depósito",
            "GET /api/admin/tarifas - Listar todas las tarifas",
            "GET /api/admin/parametros - Listar parámetros globales"
        ));
        
        Map<String, Object> logistica = new HashMap<>();
        logistica.put("nombre", "Servicio de Logística");
        logistica.put("puerto", 8082);
        logistica.put("rutas", List.of(
            "GET /api/logistica/rutas - Listar todas las rutas",
            "POST /api/logistica/rutas - Crear nueva ruta",
            "GET /api/logistica/tramos - Listar todos los tramos",
            "PUT /api/logistica/tramos/{id} - Actualizar tramo"
        ));
        
        Map<String, Object> pedidos = new HashMap<>();
        pedidos.put("nombre", "Servicio de Pedidos");
        pedidos.put("puerto", 8083);
        pedidos.put("rutas", List.of(
            "GET /api/pedidos/clientes - Listar todos los clientes",
            "POST /api/pedidos/clientes - Crear nuevo cliente",
            "GET /api/pedidos/contenedores - Listar todos los contenedores",
            "POST /api/pedidos/contenedores - Crear nuevo contenedor",
            "GET /api/pedidos/solicitudes - Listar todas las solicitudes",
            "POST /api/pedidos/solicitudes - Crear nueva solicitud",
            "GET /api/pedidos/solicitudes/{id}/seguimiento - Seguimiento de solicitud"
        ));
        
        Map<String, Object> geolocalizacion = new HashMap<>();
        geolocalizacion.put("nombre", "Servicio de Geolocalización");
        geolocalizacion.put("puerto", 8084);
        geolocalizacion.put("rutas", List.of(
            "GET /api/geo/distancia?origen={lat,lng}&destino={lat,lng} - Calcular distancia y duración entre dos puntos usando Google Maps"
        ));
        
        microservicios.put("administracion", administracion);
        microservicios.put("logistica", logistica);
        microservicios.put("pedidos", pedidos);
        microservicios.put("geolocalizacion", geolocalizacion);
        
        response.put("microservicios", microservicios);
        
        // Instrucciones de uso
        List<String> instrucciones = new ArrayList<>();
        instrucciones.add("1. Para acceder al servicio de administración use: http://localhost:8085/api/admin/[endpoint]");
        instrucciones.add("2. Para acceder al servicio de logística use: http://localhost:8085/api/logistica/[endpoint]");
        instrucciones.add("3. Para acceder al servicio de pedidos use: http://localhost:8085/api/pedidos/[endpoint]");
        instrucciones.add("4. Para acceder al servicio de geolocalización use: http://localhost:8085/api/geo/[endpoint]");
        instrucciones.add("5. También puede acceder directamente a cada servicio:");
        instrucciones.add("   - Administración: http://localhost:8081");
        instrucciones.add("   - Logística: http://localhost:8082");
        instrucciones.add("   - Pedidos: http://localhost:8083");
        instrucciones.add("   - Geolocalización: http://localhost:8084");
        
        response.put("instrucciones", instrucciones);
        
        return Mono.just(response);
    }

    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("gateway", "RUNNING");
        health.put("timestamp", LocalDateTime.now());
        
        // Verificar estado de los microservicios (simplificado)
        Map<String, String> services = new HashMap<>();
        services.put("servicio-administracion", "http://localhost:8081 - CONFIGURADO");
        services.put("servicio-logistica", "http://localhost:8082 - CONFIGURADO");
        services.put("servicio-pedidos", "http://localhost:8083 - CONFIGURADO");
        services.put("servicio-geolocalizacion", "http://localhost:8084 - CONFIGURADO");
        
        health.put("services", services);
        
        return Mono.just(health);
    }

    @GetMapping("/routes")
    public Mono<Map<String, Object>> routes() {
        Map<String, Object> response = new HashMap<>();
        response.put("titulo", "Rutas configuradas en el API Gateway");
        response.put("timestamp", LocalDateTime.now());
        
        List<Map<String, Object>> rutasConfiguradas = new ArrayList<>();
        
        Map<String, Object> ruta1 = new HashMap<>();
        ruta1.put("id", "servicio-administracion");
        ruta1.put("path", "/api/admin/**");
        ruta1.put("uri", "http://localhost:8081");
        ruta1.put("descripcion", "Gestión de camiones, depósitos, tarifas y parámetros");
        rutasConfiguradas.add(ruta1);
        
        Map<String, Object> ruta2 = new HashMap<>();
        ruta2.put("id", "servicio-logistica");
        ruta2.put("path", "/api/logistica/**");
        ruta2.put("uri", "http://localhost:8082");
        ruta2.put("descripcion", "Gestión de rutas, tramos y planificación de viajes");
        rutasConfiguradas.add(ruta2);
        
        Map<String, Object> ruta3 = new HashMap<>();
        ruta3.put("id", "servicio-pedidos");
        ruta3.put("path", "/api/pedidos/**");
        ruta3.put("uri", "http://localhost:8083");
        ruta3.put("descripcion", "Gestión de clientes, contenedores y solicitudes");
        rutasConfiguradas.add(ruta3);
        
        Map<String, Object> ruta4 = new HashMap<>();
        ruta4.put("id", "servicio-geolocalizacion");
        ruta4.put("path", "/api/geo/**");
        ruta4.put("uri", "http://localhost:8084");
        ruta4.put("descripcion", "Cálculo de distancias usando Google Maps Distance Matrix API");
        rutasConfiguradas.add(ruta4);
        
        response.put("rutas", rutasConfiguradas);
        
        return Mono.just(response);
    }
}