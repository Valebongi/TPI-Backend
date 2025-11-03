package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.sim.domain.Contenedor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    
    Optional<Contenedor> findByCodigo(String codigo);
    
    List<Contenedor> findByClienteId(Long clienteId);
    
    List<Contenedor> findByEstado(String estado);
    
    @Query("SELECT c FROM Contenedor c WHERE c.cliente.id = :clienteId AND c.estado = :estado")
    List<Contenedor> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, @Param("estado") String estado);
    
    @Query("SELECT c FROM Contenedor c WHERE " +
           "LOWER(c.codigo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Contenedor> findByFiltro(@Param("filtro") String filtro);
    
    boolean existsByCodigo(String codigo);
}