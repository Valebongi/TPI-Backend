package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COUNTRIES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Country {
    @Id
    @SequenceGenerator(name="seq_country", sequenceName="SEQ_COUNTRY_ID", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_country")
    @Column(name = "ID_COUNTRY")
    private Integer id;

    @Column(name = "CODE", nullable = false, length = 3, unique = true)
    private String code;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}