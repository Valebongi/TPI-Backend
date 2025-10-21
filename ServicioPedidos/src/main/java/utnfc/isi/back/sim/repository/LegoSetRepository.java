package utnfc.isi.back.sim.repository;

import utnfc.isi.back.sim.domain.LegoSet;
import utnfc.isi.back.sim.domain.Theme;
import utnfc.isi.back.sim.domain.AgeGroup;
import utnfc.isi.back.sim.domain.Country;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LegoSetRepository extends CrudRepository<LegoSet, Integer> {
    // Ranking de países por mejor relación costo/valoración (top 5)
    List<Object[]> findTop5CountriesByCostRatingRatio();
    
    // Sets para edad específica con precio máximo y valoración mínima
    List<Object[]> findSetsByAgeAndPriceAndRating(int targetAge, BigDecimal maxPrice, BigDecimal minRating);
}