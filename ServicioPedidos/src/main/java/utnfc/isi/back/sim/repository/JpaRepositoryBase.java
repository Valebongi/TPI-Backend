package utnfc.isi.back.sim.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utnfc.isi.back.sim.infra.LocalEntityManagerProvider;
import utnfc.isi.back.sim.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class JpaRepositoryBase<T, ID> implements CrudRepository<T, ID> {

    private final Class<T> entityClass;

    protected JpaRepositoryBase(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager em() {
        return LocalEntityManagerProvider.em();
    }

    // ---------- Helpers de transacción ----------
    protected <R> R tx(Function<EntityManager, R> work) {
        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    protected void txVoid(java.util.function.Consumer<EntityManager> work) {
        tx(em -> { work.accept(em); return null; });
    }

    // ---------- Impl CRUD genérico ----------
    @Override
    public T save(T entity) {
        return tx(em -> {
            if (isNew(entity)) {
                em.persist(entity);
                return entity;
            } else {
                return em.merge(entity);
            }
        });
    }

    @Override
    public Optional<T> findById(ID id) {
        EntityManager em = em();
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = em();
        try {
            TypedQuery<T> q = em.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = em();
        try {
            TypedQuery<Long> q = em.createQuery("select count(e) from " + entityClass.getSimpleName() + " e", Long.class);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T entity) {
        txVoid(em -> {
            T managed = entity;
            if (!em.contains(entity)) {
                // asegurar estado managed
                Object id = getId(entity);
                if (id != null) {
                    managed = em.find(entityClass, id);
                }
            }
            if (managed != null) em.remove(managed);
        });
    }

    @Override
    public void deleteById(ID id) {
        txVoid(em -> {
            T managed = em.find(entityClass, id);
            if (managed != null) em.remove(managed);
        });
    }

    @Override
    public List<T> findAll(int offset, int limit) {
        EntityManager em = em();
        try {
            TypedQuery<T> q = em.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass);
            if (offset > 0) q.setFirstResult(offset);
            if (limit > 0) q.setMaxResults(limit);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // ---------- Hooks para “isNew()” y obtener ID ----------
    // Por defecto intenta leer por reflexión un getter llamado getId()
    protected boolean isNew(T entity) {
        Object id = getId(entity);
        return id == null;
    }

    @SuppressWarnings("unchecked")
    protected Object getId(T entity) {
        try {
            var m = entity.getClass().getMethod("getId");
            return m.invoke(entity);
        } catch (Exception e) {
            // Si no hay getId(), asumimos “new”
            return null;
        }
    }
}
