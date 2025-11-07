package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Ruta;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.repository.RutaRepository;
import utnfc.isi.back.sim.dto.RutaCreacionRequest;
import utnfc.isi.back.sim.dto.RutaTentativa;
import utnfc.isi.back.sim.dto.TramoTentativo;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Servicio de negocio para la gestión de rutas
 * Contiene la lógica de negocio para operaciones con rutas
 */
@Service
@Transactional
public class RutaService {
    
    private final RutaRepository rutaRepository;
    private final TramoService tramoService;

    @Autowired
    public RutaService(RutaRepository rutaRepository, TramoService tramoService) {
        this.rutaRepository = rutaRepository;
        this.tramoService = tramoService;
    }
    
    /**
     * Obtiene todas las rutas
     */
    @Transactional(readOnly = true)
    public List<Ruta> findAll() {
        // Log removed for Docker compatibility
        return rutaRepository.findAll();
    }
    
    /**
     * Obtiene una ruta por ID
     */
    @Transactional(readOnly = true)
    public Optional<Ruta> findById(Long id) {
        // Log removed for Docker compatibility
        return rutaRepository.findById(id);
    }
    
    /**
     * Obtiene una ruta por ID de solicitud
     */
    @Transactional(readOnly = true)
    public Optional<Ruta> findBySolicitudId(Long solicitudId) {
        // Log removed for Docker compatibility
        return rutaRepository.findBySolicitudId(solicitudId);
    }
    
    /**
     * Obtiene rutas por estado
     */
    @Transactional(readOnly = true)
    public List<Ruta> findByEstado(Ruta.EstadoRuta estado) {
        // Log removed for Docker compatibility
        return rutaRepository.findByEstado(estado);
    }
    
    /**
     * Obtiene rutas activas (planificadas o en progreso)
     */
    @Transactional(readOnly = true)
    public List<Ruta> findRutasActivas() {
        // Log removed for Docker compatibility
        return rutaRepository.findRutasActivas();
    }
    
    /**
     * Crea una nueva ruta
     */
    public Ruta save(Ruta ruta) {
        // Log removed for Docker compatibility
        
        // Validar que no exista ya una ruta para esta solicitud
        if (ruta.getSolicitudId() != null && rutaRepository.findBySolicitudId(ruta.getSolicitudId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una ruta para la solicitud ID: " + ruta.getSolicitudId());
        }
        
        // Calcular cantidad de tramos si no está definida
        if (ruta.getTramos() != null) {
            ruta.setCantidadTramos(ruta.getTramos().size());
        }
        
        Ruta rutaGuardada = rutaRepository.save(ruta);
        // Log removed for Docker compatibility
        
        return rutaGuardada;
    }
    
    /**
     * Actualiza una ruta existente
     */
    public Ruta update(Long id, Ruta rutaActualizada) {
        // Log removed for Docker compatibility
        
        return rutaRepository.findById(id)
                .map(rutaExistente -> {
                    rutaExistente.setEstado(rutaActualizada.getEstado());
                    rutaExistente.setCostoTotalAproximado(rutaActualizada.getCostoTotalAproximado());
                    rutaExistente.setCostoTotalReal(rutaActualizada.getCostoTotalReal());
                    rutaExistente.setCantidadDepositos(rutaActualizada.getCantidadDepositos());
                    
                    return rutaRepository.save(rutaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con ID: " + id));
    }
    
    /**
     * Elimina una ruta
     */
    public void deleteById(Long id) {
        // Log removed for Docker compatibility
        
        if (!rutaRepository.existsById(id)) {
            throw new RuntimeException("Ruta no encontrada con ID: " + id);
        }
        
        rutaRepository.deleteById(id);
    }
    
    /**
     * Calcula el costo total de una ruta basándose en sus tramos
     */
    public void calcularCostoTotal(Long rutaId) {
        // Log removed for Docker compatibility
        
        rutaRepository.findById(rutaId).ifPresent(ruta -> {
            List<Tramo> tramos = tramoService.findByRutaId(rutaId);
            
            // Calcular costo aproximado
            double costoAproximado = tramos.stream()
                    .mapToDouble(tramo -> tramo.getCostoAproximado() != null ? tramo.getCostoAproximado() : 0.0)
                    .sum();
            ruta.setCostoTotalAproximado(costoAproximado);
            
            // Calcular costo real si todos los tramos tienen costo real
            double costoReal = tramos.stream()
                    .mapToDouble(tramo -> tramo.getCostoReal() != null ? tramo.getCostoReal() : 0.0)
                    .sum();
            
            boolean todosTramosTienenCostoReal = tramos.stream()
                    .allMatch(tramo -> tramo.getCostoReal() != null);
            
            if (todosTramosTienenCostoReal) {
                ruta.setCostoTotalReal(costoReal);
            }
            
            rutaRepository.save(ruta);
        });
    }
    
    /**
     * Actualiza el estado de una ruta
     */
    public Ruta actualizarEstado(Long id, Ruta.EstadoRuta nuevoEstado) {
        // Log removed for Docker compatibility
        
        return rutaRepository.findById(id)
                .map(ruta -> {
                    ruta.setEstado(nuevoEstado);
                    return rutaRepository.save(ruta);
                })
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con ID: " + id));
    }
    
    /**
     * Crea una ruta automáticamente basada en una solicitud de transporte
     * Este método es llamado por otros microservicios para generar rutas automáticamente
     */
    public Ruta crearRutaAutomatica(RutaCreacionRequest request) {
        // Log removed for Docker compatibility: Creando ruta automática para solicitud
        
        try {
            // Validar que no exista ya una ruta para esta solicitud
            if (rutaRepository.findBySolicitudId(request.getSolicitudId()).isPresent()) {
                throw new IllegalArgumentException("Ya existe una ruta para la solicitud ID: " + request.getSolicitudId());
            }
            
            // Crear la ruta principal
            Ruta ruta = Ruta.builder()
                    .solicitudId(request.getSolicitudId())
                    .estado(Ruta.EstadoRuta.PLANIFICADA)
                    .fechaCreacion(LocalDateTime.now())
                    .observaciones("Ruta creada automáticamente")
                    .build();
            
            // Guardar la ruta primero para obtener el ID
            ruta = rutaRepository.save(ruta);
            
            // Crear tramos automáticamente basados en origen y destino
            crearTramosAutomaticos(ruta, request);
            
            // Calcular costos después de crear los tramos
            calcularCostoTotal(ruta.getId());
            
            // Recargar la ruta con los datos actualizados
            return rutaRepository.findById(ruta.getId()).orElse(ruta);
            
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al crear ruta automática
            throw new RuntimeException("Error al crear ruta automática: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea tramos automáticamente para una ruta basándose en el origen y destino
     */
    private void crearTramosAutomaticos(Ruta ruta, RutaCreacionRequest request) {
        // Crear un tramo principal desde origen hasta destino
        // En una implementación real, aquí se podría integrar con APIs de mapas para calcular rutas óptimas
        
        Tramo tramo = Tramo.builder()
                .ruta(ruta)
                .numero(1)
                .tipo(Tramo.TipoTramo.ENTREGA)
                .estado(Tramo.EstadoTramo.PENDIENTE)
                .origenCoordenadas(request.getOrigenCoordenadas())
                .destinoCoordenadas(request.getDestinoCoordenadas())
                .origenDescripcion(request.getOrigenDescripcion())
                .destinoDescripcion(request.getDestinoDescripcion())
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        // Calcular costo aproximado basado en peso y volumen del contenedor
        BigDecimal costoBase = BigDecimal.valueOf(1000); // Costo base
        BigDecimal costoPorPeso = request.getPesoContenedor() != null ? 
                request.getPesoContenedor().multiply(BigDecimal.valueOf(10)) : BigDecimal.ZERO;
        BigDecimal costoPorVolumen = request.getVolumenContenedor() != null ? 
                request.getVolumenContenedor().multiply(BigDecimal.valueOf(50)) : BigDecimal.ZERO;
        
        Double costoAproximado = costoBase.add(costoPorPeso).add(costoPorVolumen).doubleValue();
        tramo.setCostoAproximado(costoAproximado);
        
        // Guardar el tramo
        tramoService.save(tramo);
        
        // Actualizar la cantidad de tramos en la ruta
        ruta.setCantidadTramos(1);
        rutaRepository.save(ruta);
    }
    
    /**
     * FASE 2: Calcula rutas tentativas usando Google Maps API
     * Permite al operador evaluar diferentes opciones de ruta antes de asignar
     */
    @Transactional(readOnly = true)
    public List<RutaTentativa> calcularRutasTentativas(String origen, String destino) {
        try {
            List<RutaTentativa> rutasTentativas = new ArrayList<>();
            
            // Por ahora, simulamos 3 opciones de ruta diferentes
            // TODO: Integrar con Google Maps API para calcular rutas reales
            
            // Opción 1: Ruta directa (más rápida)
            RutaTentativa rutaDirecta = crearRutaTentativa(
                origen, destino, "DIRECTA", 
                350.0, 4.5, 2800.0
            );
            rutasTentativas.add(rutaDirecta);
            
            // Opción 2: Ruta económica (más barata)
            RutaTentativa rutaEconomica = crearRutaTentativa(
                origen, destino, "ECONOMICA", 
                420.0, 6.0, 2200.0
            );
            rutasTentativas.add(rutaEconomica);
            
            // Opción 3: Ruta alternativa (balanceada)
            RutaTentativa rutaAlternativa = crearRutaTentativa(
                origen, destino, "ALTERNATIVA", 
                385.0, 5.2, 2500.0
            );
            rutasTentativas.add(rutaAlternativa);
            
            return rutasTentativas;
            
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al calcular rutas tentativas
            throw new RuntimeException("Error al calcular rutas tentativas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea una ruta tentativa simulada
     */
    private RutaTentativa crearRutaTentativa(String origen, String destino, String tipo, 
                                           double distancia, double duracion, double costo) {
        
        // Crear tramo simulado
        TramoTentativo tramo = new TramoTentativo(
            origen, 
            destino, 
            distancia, 
            duracion, 
            "CAMION_GRANDE"
        );
        
        List<TramoTentativo> tramos = new ArrayList<>();
        tramos.add(tramo);
        
        return new RutaTentativa(
            origen, 
            destino, 
            tramos, 
            distancia, 
            duracion, 
            costo
        );
    }
}
