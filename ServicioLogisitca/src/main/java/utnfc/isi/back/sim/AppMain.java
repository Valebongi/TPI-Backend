package utnfc.isi.back.sim;

import utnfc.isi.back.sim.csv.CsvLoader;
import utnfc.isi.back.sim.repository.JpaCountryRepository;
import utnfc.isi.back.sim.repository.JpaThemeRepository;
import utnfc.isi.back.sim.repository.JpaAgeGroupRepository;
import utnfc.isi.back.sim.repository.JpaLegoSetRepository;
import utnfc.isi.back.sim.service.ImportService;

import java.io.File;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

public class AppMain {
  public static void  main(String[] args) throws Exception {
    System.out.println("=== LEGO Sets Analysis System ===");
    
    // Ruta al archivo CSV de LEGO en resources
    String csvPath = "lego_sets_data.csv";
    var csvResource = AppMain.class.getClassLoader().getResource(csvPath);
    
    if (csvResource == null) {
      System.out.println("No se encontró el archivo en resources: " + csvPath);
      return;
    }
    
    try {
      // 1) Cargar CSV a memoria (DTOs)
      var rows = CsvLoader.readLegoSets(csvPath);
      System.out.println("Filas leídas del CSV: " + rows.size());

      // 2) Popular BD (solo países existentes en DDL)
      var svc = new ImportService();
      var cargados = svc.importar(rows);
      System.out.println("Resultado de la carga: " + cargados);

      // 3) Análisis de datos LEGO
      performLegoAnalysis();
      
    } catch (Exception e) {
      System.err.println("Error durante la ejecución: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Cerrar EntityManagerFactory para terminar limpiamente
      utnfc.isi.back.sim.infra.LocalEntityManagerProvider.close();
      System.out.println("\nSistema finalizado correctamente");
    }
  }
  

  
  private static void performLegoAnalysis() {
    System.out.println("\n=== ANÁLISIS DE DATOS LEGO ===");
    
    // Inicializar repositories
    var legoSetRepo = new JpaLegoSetRepository();
    var themeRepo = new JpaThemeRepository();
    var ageGroupRepo = new JpaAgeGroupRepository();
    var countryRepo = new JpaCountryRepository();

    // 1) Estadísticas básicas usando streams con Optional
    System.out.println("\n=== ESTADÍSTICAS GENERALES ===");
    Stream.of(
        "Total de Sets LEGO: " + legoSetRepo.count(),
        "Total de Temas: " + themeRepo.count(),
        "Total de Grupos de Edad: " + ageGroupRepo.count()
    ).forEach(System.out::println);

    // 2) Ranking de los 5 Países con streams
    System.out.println("\n=== TOP 5 PAÍSES - MEJOR RELACIÓN COSTO/VALORACIÓN ===");
    legoSetRepo.findTop5CountriesByCostRatingRatio()
        .stream()
        .forEach(r -> 
            System.out.printf("%s: $%.2f por estrella (promedio de %s sets)\n", r[0], r[1], r[2]));

    // 3) Sets para edad específica usando Optional y streams
    System.out.println("\n=== SETS PARA EDAD 3 - PRECIO < $10 - VALORACIÓN >= 4.8 ===");
    int targetAge = 3;
    BigDecimal maxPrice = new BigDecimal("10.00");
    BigDecimal minRating = new BigDecimal("4.8");
    
    Optional.of(legoSetRepo.findSetsByAgeAndPriceAndRating(targetAge, maxPrice, minRating))
        .filter(sets -> !sets.isEmpty())
        .ifPresentOrElse(
            sets -> sets.stream()
                .forEach(r -> 
                    System.out.printf("%s - %s: $%.2f (%.1f) [Edad: %s]\n", 
                        r[0], r[1], r[2], r[3], r[4])),
            () -> System.out.println("No se encontraron sets que cumplan los criterios especificados.")
        );
  }
}
 