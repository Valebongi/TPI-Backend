package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.sim.domain.Tramo;

import java.util.List;

/**
 * Repositorio JPA para la entidad Tramo
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {
    
    /**
     * Encuentra todos los tramos de una ruta específica
     */
    List<Tramo> findByRutaIdOrderById(Long rutaId);
    
    /**
     * Encuentra todos los tramos de una ruta específica ordenados por número
     */
    List<Tramo> findByRutaIdOrderByNumero(Long rutaId);
    
    /**
     * Encuentra todos los tramos de una ruta específica
     */
    List<Tramo> findByRutaId(Long rutaId);
    
    /**
     * Encuentra tramos por estado
     */
    List<Tramo> findByEstado(Tramo.EstadoTramo estado);
    
    /**
     * Encuentra tramos asignados a un camión específico
     */
    List<Tramo> findByCamionId(Long camionId);
    
    /**
     * Encuentra tramos asignados a un camión y con estado específico
     */
    List<Tramo> findByCamionIdAndEstado(Long camionId, Tramo.EstadoTramo estado);
    
    /**
     * Encuentra tramos por tipo
     */
    List<Tramo> findByTipo(Tramo.TipoTramo tipo);
    
    /**
     * Encuentra tramos en progreso para un camión
     */
    @Query("SELECT t FROM Tramo t WHERE t.camionId = :camionId AND t.estado = 'EN_PROGRESO'")
    List<Tramo> findTramosEnProgresoPorCamion(@Param("camionId") Long camionId);
    
    /**
     * Cuenta tramos completados por camión
     */
    long countByCamionIdAndEstado(Long camionId, Tramo.EstadoTramo estado);
    
    /**
     * Encuentra tramos pendientes de asignación (estado ESTIMADO)
     */
    @Query("SELECT t FROM Tramo t WHERE t.camionId IS NULL AND t.estado = 'ESTIMADO'")
    List<Tramo> findTramosPendientesDeAsignacion();
}