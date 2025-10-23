package utnfc.isi.back.sim.infra;

import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Inicializa/recrea el esquema ejecutando sql/ddl_legos.sql con el runner oficial de H2.
 * Evita problemas de SPLIT manual por ';' (CRLF, espacios, comentarios, etc.).
 */
public final class DatabaseInitializer {

    // Debe coincidir EXACTO con persistence.xml y con lo que usa Hibernate:
    private static final String URL  = "jdbc:h2:mem:backdb;DB_CLOSE_DELAY=-1;MODE=LEGACY";
    private static final String USER = "sa";
    private static final String PASS = "";

    // Ruta del DDL dentro de resources para ServicioAdministracion:
    private static final String DDL_CLASSPATH = "/sql/ddl_admin.sql";

    private DatabaseInitializer() {}

    public static void recreateSchemaFromDdl() {
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS)) {
            var in = DatabaseInitializer.class.getResourceAsStream(DDL_CLASSPATH);
            if (in == null) {
                throw new IllegalStateException("No se encontró el recurso " + DDL_CLASSPATH +
                        " en el classpath (¿está en src/main/resources/sql/ddl_admin.sql?).");
            }
            try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                // Ejecuta TODO el script (soporta ;, CRLF, comentarios, etc.)
                RunScript.execute(cn, reader);
            }
                // Validación rápida: asegurar que las tablas principales existen
            try (var ps = cn.prepareStatement(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CAMIONES'")) {
                try (var rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        throw new IllegalStateException("La tabla CAMIONES no existe tras correr el DDL. " +
                                "Revisá el contenido del ddl_admin.sql.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando DDL con RunScript", e);
        }
    }
}
