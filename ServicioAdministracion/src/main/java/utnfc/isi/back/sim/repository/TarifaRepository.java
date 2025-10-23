package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.sim.domain.Tarifa;

public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
}
