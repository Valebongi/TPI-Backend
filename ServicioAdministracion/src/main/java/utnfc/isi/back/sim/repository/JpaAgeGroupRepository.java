package utnfc.isi.back.sim.repository;

import jakarta.persistence.TypedQuery;
import utnfc.isi.back.sim.domain.AgeGroup;

import java.util.List;
import java.util.Optional;

public class JpaAgeGroupRepository 
        extends JpaRepositoryBase<AgeGroup, Integer>
        implements AgeGroupRepository {

    public JpaAgeGroupRepository() { 
        super(AgeGroup.class); 
    }

    @Override
    public Optional<AgeGroup> findByCode(String code) {
        var em = em();
        try {
            TypedQuery<AgeGroup> q = em.createQuery(
                "select ag from AgeGroup ag where ag.code = :code", AgeGroup.class);
            q.setParameter("code", code);
            var list = q.getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally { 
            em.close(); 
        }
    }

    @Override
    public List<AgeGroup> findAllOrderByCode() {
        var em = em();
        try {
            return em.createQuery("select ag from AgeGroup ag order by ag.code", AgeGroup.class)
                     .getResultList();
        } finally { 
            em.close(); 
        }
    }
}