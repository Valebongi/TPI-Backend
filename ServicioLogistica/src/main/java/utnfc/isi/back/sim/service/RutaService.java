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
import utnfc.isi.back.sim.client.PedidosClient;
import utnfc.isi.back.sim.client.GeolocalizacionClient;
import utnfc.isi.back.sim.client.AdministracionClient;
import utnfc.isi.back.sim.client.DepositoResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Servicio de negocio para la gestión de rutas
 * Contiene la lógica de negocio para operaciones con rutas
 */
@Service
@Transactional
public class RutaService {
    
    private final RutaRepository rutaRepository;
    private final TramoService tramoService;
    private final PedidosClient pedidosClient;
    private final GeolocalizacionClient geolocalizacionClient;
    private final AdministracionClient administracionClient;
    
    // Constante para el límite máximo de kilómetros por tramo
    private static final double MAX_KM_POR_TRAMO = 500.0;

    @Autowired
    public RutaService(RutaRepository rutaRepository, TramoService tramoService, 
                      PedidosClient pedidosClient, GeolocalizacionClient geolocalizacionClient,
                      AdministracionClient administracionClient) {
        this.rutaRepository = rutaRepository;
        this.tramoService = tramoService;
        this.pedidosClient = pedidosClient;
        this.geolocalizacionClient = geolocalizacionClient;
        this.administracionClient = administracionClient;
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
     * Crea una ruta automáticamente basándose solo en el ID de la solicitud
     * Obtiene automáticamente todos los datos necesarios desde el servicio de pedidos
     */
    public Ruta crearRutaDesdeSolicitud(Long solicitudId) {
        System.out.println("=== RUTA SERVICE: Creando ruta desde solicitud ID: " + solicitudId + " ===");
        
        try {
            // Validar que no exista ya una ruta para esta solicitud
            if (rutaRepository.findBySolicitudId(solicitudId).isPresent()) {
                throw new IllegalArgumentException("Ya existe una ruta para la solicitud ID: " + solicitudId);
            }
            
            // Obtener datos de la solicitud desde el servicio de pedidos
            PedidosClient.SolicitudResponse solicitud = pedidosClient.obtenerSolicitud(solicitudId);
            
            // Validar que la solicitud esté en estado BORRADOR
            if (!"BORRADOR".equals(solicitud.getEstado())) {
                throw new IllegalArgumentException("La solicitud debe estar en estado BORRADOR para crear una ruta. Estado actual: " + solicitud.getEstado());
            }
            
            // Crear el request con los datos obtenidos
            RutaCreacionRequest request = new RutaCreacionRequest(
                solicitud.getId(),
                solicitud.getOrigenCoordenadas(),
                solicitud.getDestinoCoordenadas(),
                solicitud.getDireccionOrigen(),
                solicitud.getDireccionDestino(),
                solicitud.getContenedor() != null ? solicitud.getContenedor().getPeso() : BigDecimal.ZERO,
                solicitud.getContenedor() != null ? solicitud.getContenedor().getVolumen() : BigDecimal.ZERO
            );
            
            System.out.println("=== RUTA SERVICE: Datos obtenidos - Origen: " + request.getOrigenDescripcion() + ", Destino: " + request.getDestinoDescripcion() + " ===");
            
            // Crear la ruta usando el método existente
            Ruta rutaCreada = crearRutaAutomatica(request);
            
            // Actualizar el estado de la solicitud a PROGRAMADA
            boolean estadoActualizado = pedidosClient.actualizarEstadoSolicitud(solicitudId, "PROGRAMADA");
            if (estadoActualizado) {
                System.out.println("=== RUTA SERVICE: Estado de solicitud actualizado a PROGRAMADA ===");
            } else {
                System.out.println("=== RUTA SERVICE: WARNING - No se pudo actualizar el estado de la solicitud ===");
            }
            
            return rutaCreada;
            
        } catch (Exception e) {
            System.out.println("=== RUTA SERVICE: Error al crear ruta desde solicitud: " + e.getMessage() + " ===");
            throw new RuntimeException("Error al crear ruta desde solicitud ID " + solicitudId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Crea tramos automáticamente para una ruta basándose en el origen y destino
     * NUEVA LÓGICA: Divide en múltiples tramos si supera 500KM usando depósitos intermedios
     */
    private void crearTramosAutomaticos(Ruta ruta, RutaCreacionRequest request) {
        System.out.println("=== RUTA SERVICE: Creando tramos automáticos para ruta ID: " + ruta.getId() + " ===");
        
        try {
            // 1. Calcular distancia total usando el servicio de geolocalización
            GeolocalizacionClient.DistanciaResponse distanciaTotal = geolocalizacionClient.calcularDistancia(
                request.getOrigenCoordenadas(), 
                request.getDestinoCoordenadas()
            );
            
            double kmTotales = distanciaTotal.getKilometros();
            System.out.println("=== RUTA SERVICE: Distancia total calculada: " + kmTotales + " KM ===");
            
            // 2. Decidir si necesitamos dividir la ruta
            if (kmTotales <= MAX_KM_POR_TRAMO) {
                // Crear un solo tramo directo
                System.out.println("=== RUTA SERVICE: Distancia menor a " + MAX_KM_POR_TRAMO + "KM, creando tramo único ===");
                crearTramoUnico(ruta, request, kmTotales, distanciaTotal.getDuracionTexto());
            } else {
                // Dividir en múltiples tramos usando depósitos
                System.out.println("=== RUTA SERVICE: Distancia mayor a " + MAX_KM_POR_TRAMO + "KM, dividiendo en tramos múltiples ===");
                crearTramosMultiples(ruta, request, kmTotales);
            }
            
        } catch (Exception e) {
            System.out.println("=== RUTA SERVICE: Error al crear tramos automáticos: " + e.getMessage() + " ===");
            // Fallback: crear tramo único si hay error
            crearTramoUnico(ruta, request, 0.0, "Estimado");
        }
    }
    
    /**
     * Crea un solo tramo directo cuando la distancia es menor a 500KM
     */
    private void crearTramoUnico(Ruta ruta, RutaCreacionRequest request, double distanciaKm, String duracionTexto) {
        Tramo tramo = new Tramo();
        tramo.setRuta(ruta);
        tramo.setNumero(1);
        tramo.setTipo(Tramo.TipoTramo.ENTREGA);
        tramo.setEstado(Tramo.EstadoTramo.ESTIMADO); // Estado correcto según secuencia
        tramo.setOrigenCoordenadas(request.getOrigenCoordenadas());
        tramo.setDestinoCoordenadas(request.getDestinoCoordenadas());
        tramo.setOrigenDescripcion(request.getOrigenDescripcion());
        tramo.setDestinoDescripcion(request.getDestinoDescripcion());
        tramo.setDistanciaKm(distanciaKm);
        tramo.setFechaCreacion(LocalDateTime.now());
        
        // Calcular costo aproximado
        Double costoAproximado = calcularCostoAproximado(request, distanciaKm);
        tramo.setCostoAproximado(costoAproximado);
        
        // Guardar el tramo
        tramoService.save(tramo);
        
        // Actualizar la cantidad de tramos en la ruta
        ruta.setCantidadTramos(1);
        ruta.setCantidadDepositos(0);
        rutaRepository.save(ruta);
        
        System.out.println("=== RUTA SERVICE: Tramo único creado - Distancia: " + distanciaKm + "KM, Costo: " + costoAproximado + " ===");
    }
    
    /**
     * Crea múltiples tramos cuando la distancia supera 500KM
     * Busca depósitos intermedios para dividir la ruta
     */
    private void crearTramosMultiples(Ruta ruta, RutaCreacionRequest request, double distanciaTotalKm) {
        // Obtener todos los depósitos disponibles
        DepositoResponse[] depositos = administracionClient.obtenerDepositos();
        System.out.println("=== RUTA SERVICE: Obtenidos " + depositos.length + " depósitos disponibles ===");
        
        if (depositos.length == 0) {
            System.out.println("=== RUTA SERVICE: Sin depósitos disponibles, creando tramo único ===");
            crearTramoUnico(ruta, request, distanciaTotalKm, "Estimado");
            return;
        }
        
        // Calcular cuántos tramos necesitamos aproximadamente
        int tramosNecesarios = (int) Math.ceil(distanciaTotalKm / MAX_KM_POR_TRAMO);
        System.out.println("=== RUTA SERVICE: Tramos necesarios aproximados: " + tramosNecesarios + " ===");
        
        // Buscar el mejor depósito intermedio (más cercano al punto medio de la ruta)
        DepositoResponse mejorDeposito = encontrarMejorDepositoIntermedio(
            request.getOrigenCoordenadas(), 
            request.getDestinoCoordenadas(), 
            depositos
        );
        
        if (mejorDeposito == null) {
            System.out.println("=== RUTA SERVICE: No se encontró depósito intermedio adecuado, creando tramo único ===");
            crearTramoUnico(ruta, request, distanciaTotalKm, "Estimado");
            return;
        }
        
        System.out.println("=== RUTA SERVICE: Usando depósito intermedio: " + mejorDeposito.getNombre() + " ===");
        
        // Crear primer tramo: Origen -> Depósito
        crearTramoConDestino(ruta, 1, Tramo.TipoTramo.TRANSPORTE, 
            request.getOrigenCoordenadas(), mejorDeposito.getCoordenadas(),
            request.getOrigenDescripcion(), mejorDeposito.getNombre(),
            request);
        
        // Crear segundo tramo: Depósito -> Destino
        crearTramoConDestino(ruta, 2, Tramo.TipoTramo.ENTREGA,
            mejorDeposito.getCoordenadas(), request.getDestinoCoordenadas(),
            mejorDeposito.getNombre(), request.getDestinoDescripcion(),
            request);
        
        // Actualizar la ruta
        ruta.setCantidadTramos(2);
        ruta.setCantidadDepositos(1);
        rutaRepository.save(ruta);
        
        System.out.println("=== RUTA SERVICE: Creados 2 tramos usando depósito intermedio ===");
    }
    
    /**
     * Encuentra el mejor depósito intermedio para dividir una ruta
     */
    private DepositoResponse encontrarMejorDepositoIntermedio(String origen, String destino, DepositoResponse[] depositos) {
        // Por simplicidad, elegimos el depósito que esté más cerca del punto medio geográfico
        String[] coordsOrigen = origen.split(",");
        String[] coordsDestino = destino.split(",");
        
        double latOrigen = Double.parseDouble(coordsOrigen[0]);
        double lngOrigen = Double.parseDouble(coordsOrigen[1]);
        double latDestino = Double.parseDouble(coordsDestino[0]);
        double lngDestino = Double.parseDouble(coordsDestino[1]);
        
        // Calcular punto medio
        double latMedio = (latOrigen + latDestino) / 2;
        double lngMedio = (lngOrigen + lngDestino) / 2;
        
        DepositoResponse mejorDeposito = null;
        double menorDistancia = Double.MAX_VALUE;
        
        for (DepositoResponse deposito : depositos) {
            double latDeposito = deposito.getLatitud().doubleValue();
            double lngDeposito = deposito.getLongitud().doubleValue();
            
            // Calcular distancia euclidiana simple al punto medio
            double distancia = Math.sqrt(
                Math.pow(latDeposito - latMedio, 2) + Math.pow(lngDeposito - lngMedio, 2)
            );
            
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                mejorDeposito = deposito;
            }
        }
        
        return mejorDeposito;
    }
    
    /**
     * Crea un tramo específico con origen y destino dados
     */
    private void crearTramoConDestino(Ruta ruta, int numero, Tramo.TipoTramo tipo, 
                                     String origenCoords, String destinoCoords, 
                                     String origenDesc, String destinoDesc,
                                     RutaCreacionRequest request) {
        try {
            // Calcular distancia específica para este tramo
            GeolocalizacionClient.DistanciaResponse distanciaTramo = geolocalizacionClient.calcularDistancia(
                origenCoords, destinoCoords
            );
            
            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setNumero(numero);
            tramo.setTipo(tipo);
            tramo.setEstado(Tramo.EstadoTramo.ESTIMADO); // Estado correcto según secuencia
            tramo.setOrigenCoordenadas(origenCoords);
            tramo.setDestinoCoordenadas(destinoCoords);
            tramo.setOrigenDescripcion(origenDesc);
            tramo.setDestinoDescripcion(destinoDesc);
            tramo.setDistanciaKm(distanciaTramo.getKilometros());
            tramo.setFechaCreacion(LocalDateTime.now());
            
            // Calcular costo proporcional
            Double costoAproximado = calcularCostoAproximado(request, distanciaTramo.getKilometros());
            tramo.setCostoAproximado(costoAproximado);
            
            // Guardar el tramo
            tramoService.save(tramo);
            
            System.out.println("=== RUTA SERVICE: Tramo " + numero + " creado - " + origenDesc + " -> " + destinoDesc + 
                             " (" + distanciaTramo.getKilometros() + "KM) ===");
            
        } catch (Exception e) {
            System.out.println("=== RUTA SERVICE: Error al crear tramo " + numero + ": " + e.getMessage() + " ===");
            // Crear tramo sin distancia calculada
            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setNumero(numero);
            tramo.setTipo(tipo);
            tramo.setEstado(Tramo.EstadoTramo.ESTIMADO);
            tramo.setOrigenCoordenadas(origenCoords);
            tramo.setDestinoCoordenadas(destinoCoords);
            tramo.setOrigenDescripcion(origenDesc);
            tramo.setDestinoDescripcion(destinoDesc);
            tramo.setDistanciaKm(0.0);
            tramo.setFechaCreacion(LocalDateTime.now());
            
            Double costoAproximado = calcularCostoAproximado(request, 0.0);
            tramo.setCostoAproximado(costoAproximado);
            tramoService.save(tramo);
        }
    }
    
    /**
     * Calcula el costo aproximado basado en peso, volumen y distancia del contenedor
     */
    private Double calcularCostoAproximado(RutaCreacionRequest request, double distanciaKm) {
        BigDecimal costoBase = BigDecimal.valueOf(1000); // Costo base
        BigDecimal costoPorPeso = request.getPesoContenedor() != null ? 
                request.getPesoContenedor().multiply(BigDecimal.valueOf(10)) : BigDecimal.ZERO;
        BigDecimal costoPorVolumen = request.getVolumenContenedor() != null ? 
                request.getVolumenContenedor().multiply(BigDecimal.valueOf(50)) : BigDecimal.ZERO;
        BigDecimal costoPorDistancia = BigDecimal.valueOf(distanciaKm * 2.5); // $2.5 por KM
        
        return costoBase.add(costoPorPeso).add(costoPorVolumen).add(costoPorDistancia).doubleValue();
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
