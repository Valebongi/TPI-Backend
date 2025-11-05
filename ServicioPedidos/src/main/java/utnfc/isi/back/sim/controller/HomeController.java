package utnfc.isi.back.sim.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class HomeController {
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> home() {
        // Log removed for Docker compatibility
        
        List<Map<String, Object>> endpoints = new ArrayList<>();
        
        // Endpoints de Clientes
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "GET",
                "endpoint", "/clientes",
                "descripcion", "Listar todos los clientes",
                "parametros", "?filtro=string&soloActivos=boolean"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "GET",
                "endpoint", "/clientes/{id}",
                "descripcion", "Obtener cliente por ID",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "GET",
                "endpoint", "/clientes/email/{email}",
                "descripcion", "Obtener cliente por email",
                "parametros", "email: String"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "POST",
                "endpoint", "/clientes",
                "descripcion", "Crear nuevo cliente",
                "parametros", "Body: Cliente JSON"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "PUT",
                "endpoint", "/clientes/{id}",
                "descripcion", "Actualizar cliente",
                "parametros", "id: Long, Body: Cliente JSON"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "DELETE",
                "endpoint", "/clientes/{id}",
                "descripcion", "Eliminar cliente",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "CLIENTES",
                "metodo", "PATCH",
                "endpoint", "/clientes/{id}/desactivar",
                "descripcion", "Desactivar cliente",
                "parametros", "id: Long"
        ));
        
        // Endpoints de Contenedores
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "GET",
                "endpoint", "/contenedores",
                "descripcion", "Listar contenedores",
                "parametros", "?clienteId=Long&estado=String&filtro=String"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "GET",
                "endpoint", "/contenedores/{id}",
                "descripcion", "Obtener contenedor por ID",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "GET",
                "endpoint", "/contenedores/codigo/{codigo}",
                "descripcion", "Obtener contenedor por código",
                "parametros", "codigo: String"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "POST",
                "endpoint", "/contenedores",
                "descripcion", "Crear nuevo contenedor",
                "parametros", "Body: Contenedor JSON"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "PUT",
                "endpoint", "/contenedores/{id}",
                "descripcion", "Actualizar contenedor",
                "parametros", "id: Long, Body: Contenedor JSON"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "PATCH",
                "endpoint", "/contenedores/{id}/estado",
                "descripcion", "Actualizar estado del contenedor",
                "parametros", "id: Long, ?estado=String"
        ));
        endpoints.add(Map.of(
                "grupo", "CONTENEDORES",
                "metodo", "DELETE",
                "endpoint", "/contenedores/{id}",
                "descripcion", "Eliminar contenedor",
                "parametros", "id: Long"
        ));
        
        // Endpoints de Solicitudes
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "GET",
                "endpoint", "/solicitudes",
                "descripcion", "Listar solicitudes",
                "parametros", "?clienteId=Long&estado=String&fechaInicio=ISO&fechaFin=ISO"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "GET",
                "endpoint", "/solicitudes/{id}",
                "descripcion", "Obtener solicitud por ID",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "GET",
                "endpoint", "/solicitudes/numero/{numero}",
                "descripcion", "Obtener solicitud por número",
                "parametros", "numero: String"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "GET",
                "endpoint", "/solicitudes/{id}/seguimiento",
                "descripcion", "Obtener seguimiento de solicitud",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "POST",
                "endpoint", "/solicitudes",
                "descripcion", "Crear nueva solicitud de transporte",
                "parametros", "Body: {clienteId: Long, contenedor: Contenedor}"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "PUT",
                "endpoint", "/solicitudes/{id}",
                "descripcion", "Actualizar solicitud",
                "parametros", "id: Long, Body: Solicitud JSON"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "PATCH",
                "endpoint", "/solicitudes/{id}/estado",
                "descripcion", "Actualizar estado de solicitud",
                "parametros", "id: Long, ?estado=String"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "DELETE",
                "endpoint", "/solicitudes/{id}",
                "descripcion", "Eliminar solicitud",
                "parametros", "id: Long"
        ));
        endpoints.add(Map.of(
                "grupo", "SOLICITUDES",
                "metodo", "GET",
                "endpoint", "/solicitudes/estadisticas",
                "descripcion", "Obtener estadísticas de solicitudes",
                "parametros", "Ninguno"
        ));
        
        // Endpoints de Sistema
        endpoints.add(Map.of(
                "grupo", "SISTEMA",
                "metodo", "GET",
                "endpoint", "/health",
                "descripcion", "Health check del servicio",
                "parametros", "Ninguno"
        ));
        endpoints.add(Map.of(
                "grupo", "SISTEMA",
                "metodo", "GET",
                "endpoint", "/actuator/health",
                "descripcion", "Health check detallado (Actuator)",
                "parametros", "Ninguno"
        ));
        endpoints.add(Map.of(
                "grupo", "SISTEMA",
                "metodo", "GET",
                "endpoint", "/actuator/metrics",
                "descripcion", "Métricas del servicio",
                "parametros", "Ninguno"
        ));
        
        Map<String, Object> response = Map.of(
                "servicio", "Servicio de Pedidos",
                "version", "1.0.0",
                "estado", "ACTIVO",
                "puerto", "8083",
                "timestamp", LocalDateTime.now(),
                "descripcion", "Microservicio para gestión de pedidos, clientes, contenedores y solicitudes de transporte",
                "baseUrl", "http://localhost:8083",
                "endpoints", endpoints,
                "documentacion", Map.of(
                        "swagger", "Próximamente disponible",
                        "postman", "Importar colección desde endpoints listados"
                )
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        // Log removed for Docker compatibility
        
        Map<String, String> response = Map.of(
                "status", "UP",
                "service", "servicio-pedidos",
                "port", "8083",
                "timestamp", LocalDateTime.now().toString()
        );
        
        return ResponseEntity.ok(response);
    }
}
