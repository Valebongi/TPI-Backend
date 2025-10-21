package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "LEGO_SETS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LegoSet {
    @Id
    @SequenceGenerator(name="seq_lego_set", sequenceName="SEQ_LEGO_SET_ID", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lego_set")
    @Column(name = "ID_SET")
    private Integer id;

    @Column(name = "PROD_ID", nullable = false)
    private Integer prodId;

    @Column(name = "SET_NAME", nullable = false, length = 200)
    private String setName;

    @Column(name = "PROD_DESC", length = 2048)
    private String prodDesc;

    @Column(name = "REVIEW_DIFFICULTY", length = 32)
    private String reviewDifficulty;

    @Column(name = "PIECE_COUNT")
    private Integer pieceCount;

    @Column(name = "STAR_RATING", precision = 3, scale = 1)
    private BigDecimal starRating;

    @Column(name = "LIST_PRICE", precision = 10, scale = 2)
    private BigDecimal listPrice;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "THEME_ID", nullable = false)
    private Theme theme;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "AGE_GROUP_ID", nullable = false)
    private AgeGroup ageGroup;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID", nullable = false)
    private Country country;
}