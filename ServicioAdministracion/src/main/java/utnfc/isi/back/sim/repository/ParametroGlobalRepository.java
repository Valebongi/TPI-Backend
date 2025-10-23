package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utnfc.isi.back.sim.domain.ParametroGlobal;

import java.math.BigDecimal;
import java.util.Optional;

public interface ParametroGlobalRepository extends JpaRepository<ParametroGlobal, String> {
    
    @Query("SELECT p.valor FROM ParametroGlobal p WHERE p.clave = :clave AND p.activo = true")
    Optional<BigDecimal> findValorByClave(@Param("clave") String clave);
    
    Optional<ParametroGlobal> findByClave(String clave);
}