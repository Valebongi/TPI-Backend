package utnfc.isi.back.sim.repository;

import java.util.List;
import java.util.Optional;


public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();

    // Extensiones útiles
    long count();
    void delete(T entity);
    void deleteById(ID id);

    // Paginado simple (offset/limit)
    List<T> findAll(int offset, int limit);
}


