package utnfc.isi.back.sim.repository;

import jakarta.persistence.TypedQuery;
import utnfc.isi.back.sim.domain.Country;

import java.util.List;
import java.util.Optional;

public class JpaCountryRepository 
        extends JpaRepositoryBase<Country, Integer>
        implements CountryRepository {

    public JpaCountryRepository() { 
        super(Country.class); 
    }

    @Override
    public Optional<Country> findByCode(String code) {
        var em = em();
        try {
            TypedQuery<Country> q = em.createQuery(
                "select c from Country c where c.code = :code", Country.class);
            q.setParameter("code", code);
            var list = q.getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally { 
            em.close(); 
        }
    }

    @Override
    public List<Country> findAllOrderByName() {
        var em = em();
        try {
            return em.createQuery("select c from Country c order by c.name", Country.class)
                     .getResultList();
        } finally { 
            em.close(); 
        }
    }
}