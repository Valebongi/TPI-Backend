package utnfc.isi.back.sim.infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class LocalEntityManagerProvider {
    private static final String PU = "pu-backend";
    private static final EntityManagerFactory EMF;

    static {
        // recrea el esquema explícitamente desde el ddl_legos.sql (como en los ejemplos JDBC/JPA)
        DatabaseInitializer.recreateSchemaFromDdl();
        EMF = Persistence.createEntityManagerFactory(PU);
    }

    private LocalEntityManagerProvider(){}

    public static EntityManager em() {
        return EMF.createEntityManager();
    }

    public static void close() {
        EMF.close();
    }
}

