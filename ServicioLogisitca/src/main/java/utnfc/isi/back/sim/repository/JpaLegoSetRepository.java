package utnfc.isi.back.sim.repository;

import jakarta.persistence.TypedQuery;
import utnfc.isi.back.sim.domain.LegoSet;
import utnfc.isi.back.sim.domain.Theme;
import utnfc.isi.back.sim.domain.AgeGroup;
import utnfc.isi.back.sim.domain.Country;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class JpaLegoSetRepository 
        extends JpaRepositoryBase<LegoSet, Integer>
        implements LegoSetRepository {

    public JpaLegoSetRepository() { 
        super(LegoSet.class); 
    }

    @Override
    public List<Object[]> findTop5CountriesByCostRatingRatio() {
        var em = em();
        try {
            return em.createQuery(
                "select c.name, avg(ls.listPrice / ls.starRating), count(ls) " +
                "from LegoSet ls join ls.country c " +
                "where ls.listPrice is not null and ls.starRating is not null and ls.starRating > 0 " +
                "group by c.name " +
                "having count(ls) > 0 " +
                "order by avg(ls.listPrice / ls.starRating) asc", Object[].class)
                .setMaxResults(5)
                .getResultList();
        } finally { 
            em.close(); 
        }
    }

    @Override
    public List<Object[]> findSetsByAgeAndPriceAndRating(int targetAge, BigDecimal maxPrice, BigDecimal minRating) {
        var em = em();
        try {
            // Primero obtenemos todos los sets que cumplen precio y rating
            var candidateSets = em.createQuery(
                "select ls.setName, t.name, ls.listPrice, ls.starRating, ag.code, ls " +
                "from LegoSet ls join ls.theme t left join ls.ageGroup ag " +
                "where ls.listPrice <= :maxPrice and ls.starRating >= :minRating " +
                "and ls.listPrice is not null and ls.starRating is not null " +
                "order by ls.listPrice desc", Object[].class)
                .setParameter("maxPrice", maxPrice)
                .setParameter("minRating", minRating)
                .getResultList();

            // Filtrar por edad usando el método matchesAge de AgeGroup
            return candidateSets.stream()
                .filter(row -> {
                    LegoSet legoSet = (LegoSet) row[5]; // El objeto LegoSet completo
                    if (legoSet.getAgeGroup() == null) {
                        return false; // No tiene rango etario, no se considera
                    }
                    return legoSet.getAgeGroup().matchesAge(targetAge);
                })
                .map(row -> new Object[]{row[0], row[1], row[2], row[3], row[4]}) // Sin el LegoSet
                .distinct() // Sets distintos
                .toList();
        } finally { 
            em.close(); 
        }
    }
}