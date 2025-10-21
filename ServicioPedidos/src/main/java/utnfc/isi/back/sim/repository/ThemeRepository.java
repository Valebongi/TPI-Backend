package utnfc.isi.back.sim.repository;

import utnfc.isi.back.sim.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends CrudRepository<Theme, Integer> {
    Optional<Theme> findByName(String name);
    List<Theme> findAllOrderByName();
}