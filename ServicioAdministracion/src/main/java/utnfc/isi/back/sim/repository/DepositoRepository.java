package utnfc.isi.back.sim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.sim.domain.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Integer> {
}
