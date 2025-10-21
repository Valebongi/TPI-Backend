package utnfc.isi.back.sim.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CsvLegoSetRow {

    @CsvBindByName(column = "prod_id")
    private BigDecimal prodIdDecimal;  // El CSV tiene decimales

    @CsvBindByName(column = "set_name")
    private String setName;

    @CsvBindByName(column = "prod_desc")
    private String prodDesc;

    @CsvBindByName(column = "theme_name")
    private String theme;

    @CsvBindByName(column = "ages")
    private String ageGroup;

    @CsvBindByName(column = "piece_count")
    private BigDecimal pieceCountDecimal;  // El CSV tiene decimales

    @CsvBindByName(column = "list_price")
    private BigDecimal listPrice;

    @CsvBindByName(column = "star_rating")
    private BigDecimal starRating;

    @CsvBindByName(column = "review_difficulty")
    private String reviewDifficulty;

    @CsvBindByName(column = "country")
    private String country;

    @CsvBindByName(column = "num_reviews")
    private BigDecimal numReviews;

    @CsvBindByName(column = "play_star_rating")
    private BigDecimal playStarRating;  // No se usa pero debe estar para validación

    @CsvBindByName(column = "val_star_rating")
    private BigDecimal valStarRating;   // No se usa pero debe estar para validación

    // Métodos helper para conversión
    public Integer getProdId() {
        return prodIdDecimal != null ? prodIdDecimal.intValue() : null;
    }

    public Integer getPieceCount() {
        return pieceCountDecimal != null ? pieceCountDecimal.intValue() : null;
    }
}