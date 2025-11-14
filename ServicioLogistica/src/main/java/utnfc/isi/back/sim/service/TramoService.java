package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.repository.TramoRepository;
import utnfc.isi.back.sim.client.AdministracionClient;
import utnfc.isi.back.sim.client.PedidosClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de tramos
 * Contiene la lógica de negocio para operaciones con tramos
 */
@Service
@Transactional
public class TramoService {
    
    private final TramoRepository tramoRepository;
    private final PedidosClient pedidosClient;
    private final AdministracionClient administracionClient;

    @Autowired
    public TramoService(TramoRepository tramoRepository, PedidosClient pedidosClient, AdministracionClient administracionClient) {
        this.tramoRepository = tramoRepository;
        this.pedidosClient = pedidosClient;
        this.administracionClient = administracionClient;
    }
    
    /**
     * Obtiene todos los tramos
     */
    @Transactional(readOnly = true)
    public List<Tramo> findAll() {
        // Log removed for Docker compatibility
        return tramoRepository.findAll();
    }
    
    /**
     * Obtiene un tramo por ID
     */
    @Transactional(readOnly = true)
    public Optional<Tramo> findById(Long id) {
        // Log removed for Docker compatibility
        return tramoRepository.findById(id);
    }
    
    /**
     * Obtiene tramos por ID de ruta
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByRutaId(Long rutaId) {
        // Log removed for Docker compatibility
        return tramoRepository.findByRutaIdOrderById(rutaId);
    }
    
    /**
     * Obtiene tramos por estado
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByEstado(Tramo.EstadoTramo estado) {
        // Log removed for Docker compatibility
        return tramoRepository.findByEstado(estado);
    }
    
    /**
     * Obtiene tramos asignados a un camión
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByCamionId(Long camionId) {
        // Log removed for Docker compatibility
        return tramoRepository.findByCamionId(camionId);
    }
    
    /**
     * Obtiene tramos pendientes de asignación
     */
    @Transactional(readOnly = true)
    public List<Tramo> findTramosPendientesDeAsignacion() {
        // Log removed for Docker compatibility
        return tramoRepository.findTramosPendientesDeAsignacion();
    }
    
    /**
     * Crea un nuevo tramo
     */
    public Tramo save(Tramo tramo) {
        // Log removed for Docker compatibility
        
        // Validaciones básicas
        if (tramo.getOrigenCoordenadas() == null || tramo.getOrigenCoordenadas().trim().isEmpty()) {
            throw new IllegalArgumentException("Las coordenadas de origen son obligatorias");
        }
        
        if (tramo.getDestinoCoordenadas() == null || tramo.getDestinoCoordenadas().trim().isEmpty()) {
            throw new IllegalArgumentException("Las coordenadas de destino son obligatorias");
        }
        
        Tramo tramoGuardado = tramoRepository.save(tramo);
        // Log removed for Docker compatibility
        
        return tramoGuardado;
    }
    
    /**
     * Actualiza un tramo existente
     */
    public Tramo update(Long id, Tramo tramoActualizado) {
        // Log removed for Docker compatibility
        
        return tramoRepository.findById(id)
                .map(tramoExistente -> {
                    tramoExistente.setOrigenCoordenadas(tramoActualizado.getOrigenCoordenadas());
                    tramoExistente.setDestinoCoordenadas(tramoActualizado.getDestinoCoordenadas());
                    tramoExistente.setOrigenDescripcion(tramoActualizado.getOrigenDescripcion());
                    tramoExistente.setDestinoDescripcion(tramoActualizado.getDestinoDescripcion());
                    tramoExistente.setTipo(tramoActualizado.getTipo());
                    tramoExistente.setEstado(tramoActualizado.getEstado());
                    tramoExistente.setCostoAproximado(tramoActualizado.getCostoAproximado());
                    tramoExistente.setCostoReal(tramoActualizado.getCostoReal());
                    tramoExistente.setDistanciaKm(tramoActualizado.getDistanciaKm());
                    tramoExistente.setTiempoEstimadoMinutos(tramoActualizado.getTiempoEstimadoMinutos());
                    tramoExistente.setTiempoRealMinutos(tramoActualizado.getTiempoRealMinutos());
                    tramoExistente.setCamionId(tramoActualizado.getCamionId());
                    
                    return tramoRepository.save(tramoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + id));
    }
    
    /**
     * Elimina un tramo
     */
    public void deleteById(Long id) {
        // Log removed for Docker compatibility
        
        if (!tramoRepository.existsById(id)) {
            throw new RuntimeException("Tramo no encontrado con ID: " + id);
        }
        
        tramoRepository.deleteById(id);
    }
    
    /**
     * FASE 4: Asigna un tramo a un camión con validaciones completas
     * MEJORADO: Validación con ServicioAdministracion usando endpoints internos
     */
    public Tramo asignarCamion(Long tramoId, Long camionId) {
        System.out.println("=== TRAMO SERVICE: Asignando camión ID: " + camionId + " a tramo ID: " + tramoId + " ===");
        
        // Validar que el tramo existe
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
        
        // Validar que el tramo esté en estado ESTIMADO
        if (!Tramo.EstadoTramo.ESTIMADO.equals(tramo.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden asignar camiones a tramos en estado ESTIMADO");
        }
        
        // Validar que el camión existe y está disponible
        if (!administracionClient.existeCamion(camionId)) {
            throw new IllegalArgumentException("El camión con ID " + camionId + " no existe");
        }
        
        if (!administracionClient.camionDisponible(camionId)) {
            throw new IllegalArgumentException("El camión con ID " + camionId + " no está disponible");
        }
        
        // Cambiar estado del camión a ASIGNADO
        boolean estadoCambiado = administracionClient.cambiarEstadoCamion(camionId, "ASIGNADO");
        if (!estadoCambiado) {
            throw new RuntimeException("Error al cambiar el estado del camión");
        }
        
        // Asignar el camión al tramo
        tramo.setCamionId(camionId);
        tramo.setEstado(Tramo.EstadoTramo.ASIGNADO);
        
        System.out.println("=== TRAMO SERVICE: Camión asignado exitosamente ===");
        return tramoRepository.save(tramo);
    }
    
    /**
     * Inicia un tramo (transportista marca inicio)
     */
    public Tramo iniciarTramo(Long tramoId) {
        // Log removed for Docker compatibility
        
        return tramoRepository.findById(tramoId)
                .map(tramo -> {
                    if (tramo.getEstado() != Tramo.EstadoTramo.ASIGNADO) {
                        throw new IllegalStateException("El tramo debe estar asignado para poder iniciarse");
                    }
                    
                    tramo.setEstado(Tramo.EstadoTramo.EN_PROGRESO);
                    tramo.setFechaHoraInicio(LocalDateTime.now());
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
    }
    
    /**
     * Finaliza un tramo (transportista marca fin)
     */
    public Tramo finalizarTramo(Long tramoId, Double costoReal) {
        // Log removed for Docker compatibility
        
        return tramoRepository.findById(tramoId)
                .map(tramo -> {
                    if (tramo.getEstado() != Tramo.EstadoTramo.EN_PROGRESO) {
                        throw new IllegalStateException("El tramo debe estar en progreso para poder finalizarse");
                    }
                    
                    LocalDateTime ahora = LocalDateTime.now();
                    tramo.setEstado(Tramo.EstadoTramo.COMPLETADO);
                    tramo.setFechaHoraFin(ahora);
                    tramo.setCostoReal(costoReal);
                    
                    // Calcular tiempo real si hay fecha de inicio
                    if (tramo.getFechaHoraInicio() != null) {
                        long minutos = java.time.Duration.between(tramo.getFechaHoraInicio(), ahora).toMinutes();
                        tramo.setTiempoRealMinutos((int) minutos);
                    }
                    
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
    }
    
    /**
     * Actualiza el estado de un tramo
     */
    public Tramo actualizarEstado(Long id, Tramo.EstadoTramo nuevoEstado) {
        // Log removed for Docker compatibility
        
        return tramoRepository.findById(id)
                .map(tramo -> {
                    tramo.setEstado(nuevoEstado);
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + id));
    }
    
    /**
     * MEJORA: Iniciar tramo con validación de secuencia
     * Valida que no se inicie un tramo posterior antes que el anterior
     */
    public Tramo iniciarTramoConValidacion(Long tramoId) {
        // Log removed for Docker compatibility
        
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
        
        // Validar estado actual
        if (tramo.getEstado() != Tramo.EstadoTramo.ASIGNADO) {
            throw new IllegalStateException("El tramo debe estar asignado para poder iniciarse");
        }
        
        // Validar secuencia: verificar que tramos anteriores estén completados
        validarSecuenciaTramos(tramo, "inicio");
        
        tramo.setEstado(Tramo.EstadoTramo.EN_PROGRESO);
        tramo.setFechaHoraInicio(LocalDateTime.now());
        
        return tramoRepository.save(tramo);
    }
    
    /**
     * MEJORA: Finalizar tramo con validación de secuencia y actualización automática de solicitud
     */
    public Tramo finalizarTramoConValidacion(Long tramoId, Double costoReal) {
        // Log removed for Docker compatibility
        
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
        
        // Validar estado actual
        if (tramo.getEstado() != Tramo.EstadoTramo.EN_PROGRESO) {
            throw new IllegalStateException("El tramo debe estar en progreso para poder finalizarse");
        }
        
        // Validar secuencia: verificar que tramos anteriores estén completados
        validarSecuenciaTramos(tramo, "finalizacion");
        
        LocalDateTime ahora = LocalDateTime.now();
        tramo.setEstado(Tramo.EstadoTramo.COMPLETADO);
        tramo.setFechaHoraFin(ahora);
        tramo.setCostoReal(costoReal);
        
        // Calcular tiempo real
        if (tramo.getFechaHoraInicio() != null) {
            long minutos = java.time.Duration.between(tramo.getFechaHoraInicio(), ahora).toMinutes();
            tramo.setTiempoRealMinutos((int) minutos);
        }
        
        Tramo tramoGuardado = tramoRepository.save(tramo);
        
        // MEJORA: Verificar si todos los tramos de la ruta están completados
        verificarYActualizarSolicitudSiCompleta(tramo.getRuta().getId());
        
        return tramoGuardado;
    }
    
    /**
     * NUEVO: Finalizar tramo con cálculo automático de costo real
     * No requiere pasar costoReal manualmente, lo calcula automáticamente
     */
    public Tramo finalizarTramoAutomatico(Long tramoId) {
        System.out.println("=== TRAMO SERVICE: Finalizando tramo automáticamente ID: " + tramoId + " ===");
        
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
        
        // Validar estado actual
        if (tramo.getEstado() != Tramo.EstadoTramo.EN_PROGRESO) {
            throw new IllegalStateException("El tramo debe estar en progreso para poder finalizarse");
        }
        
        // Validar secuencia: verificar que tramos anteriores estén completados
        validarSecuenciaTramos(tramo, "finalizacion");
        
        // Calcular costo real automáticamente
        Double costoReal = calcularCostoReal(tramo);
        
        LocalDateTime ahora = LocalDateTime.now();
        tramo.setEstado(Tramo.EstadoTramo.COMPLETADO);
        tramo.setFechaHoraFin(ahora);
        tramo.setCostoReal(costoReal);
        
        // Calcular tiempo real
        if (tramo.getFechaHoraInicio() != null) {
            long minutos = java.time.Duration.between(tramo.getFechaHoraInicio(), ahora).toMinutes();
            tramo.setTiempoRealMinutos((int) minutos);
        }
        
        Tramo tramoGuardado = tramoRepository.save(tramo);
        
        // MEJORA: Verificar si todos los tramos de la ruta están completados
        verificarYActualizarSolicitudSiCompleta(tramo.getRuta().getId());
        
        System.out.println("=== TRAMO SERVICE: Tramo finalizado con costo real calculado: $" + costoReal + " ===");
        
        return tramoGuardado;
    }
    
    /**
     * Calcula el costo real de un tramo basado en distancia, peso del contenedor y camión asignado
     */
    private Double calcularCostoReal(Tramo tramo) {
        try {
            // Obtener información del contenedor desde la solicitud
            Long solicitudId = tramo.getRuta().getSolicitudId();
            PedidosClient.SolicitudResponse solicitud = pedidosClient.obtenerSolicitud(solicitudId);
            
            // Extraer peso del contenedor
            Double pesoKg = solicitud.getContenedor().getPeso() != null ? 
                    solicitud.getContenedor().getPeso().doubleValue() : 1000.0;
            
            // Usar distancia del tramo
            Double distanciaKm = tramo.getDistanciaKm() != null ? tramo.getDistanciaKm() : 0.0;
            
            // Usar camión asignado
            Long camionId = tramo.getCamionId();
            
            // Calcular costo usando el cliente de administración
            Double costoCalculado = administracionClient.calcularCostoTramo(distanciaKm, pesoKg, camionId);
            
            System.out.println("=== CÁLCULO COSTO REAL: Distancia: " + distanciaKm + "km, Peso: " + pesoKg + "kg, Camión: " + camionId + ", Costo: $" + costoCalculado + " ===");
            
            return costoCalculado;
            
        } catch (Exception e) {
            System.out.println("=== TRAMO SERVICE: Error calculando costo real, usando costo aproximado: " + e.getMessage() + " ===");
            // Fallback: usar el costo aproximado si hay error
            return tramo.getCostoAproximado() != null ? tramo.getCostoAproximado() : 1000.0;
        }
    }
    
    /**
     * Valida la secuencia de ejecución de tramos
     * No se puede iniciar/finalizar un tramo posterior antes que los anteriores
     */
    private void validarSecuenciaTramos(Tramo tramoActual, String operacion) {
        List<Tramo> tramosDeRuta = tramoRepository.findByRutaIdOrderByNumero(tramoActual.getRuta().getId());
        
        for (Tramo tramo : tramosDeRuta) {
            // Solo verificar tramos anteriores (número menor)
            if (tramo.getNumero() < tramoActual.getNumero()) {
                // Para inicio: tramos anteriores deben estar al menos EN_PROGRESO o COMPLETADO
                if ("inicio".equals(operacion)) {
                    if (tramo.getEstado() == Tramo.EstadoTramo.ESTIMADO || 
                        tramo.getEstado() == Tramo.EstadoTramo.ASIGNADO) {
                        throw new IllegalStateException(
                            String.format("No se puede iniciar el tramo %d antes de completar el tramo %d", 
                                        tramoActual.getNumero(), tramo.getNumero()));
                    }
                }
                // Para finalización: tramos anteriores deben estar COMPLETADO
                else if ("finalizacion".equals(operacion)) {
                    if (tramo.getEstado() != Tramo.EstadoTramo.COMPLETADO) {
                        throw new IllegalStateException(
                            String.format("No se puede finalizar el tramo %d antes de completar el tramo %d", 
                                        tramoActual.getNumero(), tramo.getNumero()));
                    }
                }
            }
        }
    }
    
    /**
     * Verifica si todos los tramos de una ruta están completados y actualiza la solicitud
     */
    private void verificarYActualizarSolicitudSiCompleta(Long rutaId) {
        List<Tramo> tramosDeRuta = tramoRepository.findByRutaId(rutaId);
        
        boolean todosCompletados = tramosDeRuta.stream()
                .allMatch(tramo -> tramo.getEstado() == Tramo.EstadoTramo.COMPLETADO);
        
        if (todosCompletados) {
            // Log de éxito
            System.out.println("=== TODOS LOS TRAMOS COMPLETADOS - Ruta ID: " + rutaId + " ===");
            
            // TODO: Llamar al servicio de pedidos para actualizar solicitud a ENTREGADA
            // Esto requiere comunicación con el ServicioPedidos
            notificarSolicitudEntregada(rutaId, tramosDeRuta);
        }
    }
    
    /**
     * MEJORA: Notifica al ServicioPedidos que la solicitud debe marcarse como ENTREGADA
     * Implementa comunicación real a través del API Gateway
     */
    private void notificarSolicitudEntregada(Long rutaId, List<Tramo> tramos) {
        try {
            // Calcular costo total real
            Double costoTotalReal = tramos.stream()
                    .filter(tramo -> tramo.getCostoReal() != null)
                    .mapToDouble(Tramo::getCostoReal)
                    .sum();
            
            // Obtener la solicitudId desde cualquier tramo (todos pertenecen a la misma ruta)
            if (!tramos.isEmpty()) {
                Long solicitudId = tramos.get(0).getRuta().getSolicitudId();
                
                System.out.println("=== NOTIFICACIÓN: Iniciando actualización de solicitud ===");
                System.out.println("Solicitud ID: " + solicitudId);
                System.out.println("Ruta ID: " + rutaId);
                System.out.println("Costo total real: $" + costoTotalReal);
                System.out.println("Tramos completados: " + tramos.size());
                
                // Llamada real al ServicioPedidos a través del API Gateway
                boolean actualizado = pedidosClient.actualizarSolicitudAEntregada(solicitudId, costoTotalReal);
                
                if (actualizado) {
                    System.out.println("=== ✅ ÉXITO: Solicitud " + solicitudId + " marcada como ENTREGADA ===");
                } else {
                    System.err.println("=== ❌ ERROR: Falló la actualización de solicitud " + solicitudId + " ===");
                }
            } else {
                System.err.println("=== ERROR: No se encontraron tramos para la ruta " + rutaId + " ===");
            }
            
        } catch (Exception e) {
            System.err.println("=== ERROR CRÍTICO: Falló notificación de solicitud entregada ===");
            System.err.println("Ruta ID: " + rutaId + ", Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene todos los tramos de una ruta ordenados por número
     */
    public List<Tramo> findByRutaIdOrderByNumero(Long rutaId) {
        return tramoRepository.findByRutaIdOrderByNumero(rutaId);
    }
    
    /**
     * Verifica si una ruta está completamente finalizada
     */
    public boolean esRutaCompletada(Long rutaId) {
        List<Tramo> tramos = tramoRepository.findByRutaId(rutaId);
        return tramos.stream().allMatch(tramo -> tramo.getEstado() == Tramo.EstadoTramo.COMPLETADO);
    }
}
