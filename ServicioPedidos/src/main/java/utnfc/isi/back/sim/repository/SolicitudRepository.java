package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.sim.domain.Solicitud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    Optional<Solicitud> findByNumero(String numero);
    
    List<Solicitud> findByClienteId(Long clienteId);
    
    List<Solicitud> findByEstado(String estado);
    
    @Query("SELECT s FROM Solicitud s WHERE s.cliente.id = :clienteId AND s.estado = :estado")
    List<Solicitud> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, @Param("estado") String estado);
    
    @Query("SELECT s FROM Solicitud s WHERE s.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Solicitud> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                              @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT s FROM Solicitud s WHERE s.contenedor.id = :contenedorId")
    Optional<Solicitud> findByContenedorId(@Param("contenedorId") Long contenedorId);
    
    @Query("SELECT COUNT(s) FROM Solicitud s WHERE s.estado = :estado")
    Long countByEstado(@Param("estado") String estado);
    
    boolean existsByNumero(String numero);
}