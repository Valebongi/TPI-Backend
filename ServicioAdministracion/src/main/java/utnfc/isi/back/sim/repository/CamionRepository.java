package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.sim.domain.Camion;

public interface CamionRepository extends JpaRepository<Camion, Integer> {
    Camion findByDominio(String dominio);
}
