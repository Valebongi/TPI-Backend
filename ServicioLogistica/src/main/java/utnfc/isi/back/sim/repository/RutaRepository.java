package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.sim.domain.Ruta;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Ruta
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    
    /**
     * Encuentra una ruta por ID de solicitud
     */
    Optional<Ruta> findBySolicitudId(Long solicitudId);
    
    /**
     * Encuentra rutas por estado
     */
    List<Ruta> findByEstado(Ruta.EstadoRuta estado);
    
    /**
     * Encuentra rutas activas (no completadas ni canceladas)
     */
    @Query("SELECT r FROM Ruta r WHERE r.estado IN ('PLANIFICADA', 'EN_PROGRESO')")
    List<Ruta> findRutasActivas();
    
    /**
     * Cuenta el número de rutas por estado
     */
    long countByEstado(Ruta.EstadoRuta estado);
    
    /**
     * Encuentra rutas con costo total aproximado mayor a un valor
     */
    List<Ruta> findByCostoTotalAproximadoGreaterThan(Double costo);
}