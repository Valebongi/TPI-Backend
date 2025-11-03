package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.sim.domain.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByKeycloakId(String keycloakId);
    
    List<Cliente> findByActivoTrue();
    
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Cliente> findByFiltro(@Param("filtro") String filtro);
    
    boolean existsByEmail(String email);
}