package utnfc.isi.back.sim.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.opencsv.CSVParser;

public final class CsvLoader {
  private CsvLoader(){}

  public static List<CsvLegoSetRow> readLegoSets(String resourcePath) throws Exception {
    var inputStream = CsvLoader.class.getClassLoader().getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IllegalArgumentException("No se encontró el recurso: " + resourcePath);
    }
    
    try (var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return new CsvToBeanBuilder<CsvLegoSetRow>(reader)
          .withType(CsvLegoSetRow.class)
          .withSeparator(';')  // LEGO CSV usa punto y coma
          .withIgnoreQuotations(false)  // Permitir comillas para descripción
          .withIgnoreLeadingWhiteSpace(true)
          .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
          .build()
          .parse();
    }
  }
}
