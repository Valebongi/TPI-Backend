package utnfc.isi.back.sim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Clase principal del Microservicio de Logística
 * 
 * Este microservicio se encarga de:
 * - Gestión de rutas y tramos de transporte
 * - Asignación de camiones a tramos
 * - Seguimiento del estado de los envíos
 * - Cálculo de costos y tiempos de transporte
 */
@SpringBootApplication
@EnableFeignClients
public class ServicioLogisticaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioLogisticaApplication.class, args);
    }
}