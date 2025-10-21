package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "THEMES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Theme {
    @Id
    @SequenceGenerator(name="seq_theme", sequenceName="SEQ_THEME_ID", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_theme")
    @Column(name = "ID_THEME")
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 120, unique = true)
    private String name;
}