package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AGE_GROUPS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgeGroup {
    @Id
    @SequenceGenerator(name="seq_age_group", sequenceName="SEQ_AGE_GROUP_ID", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_age_group")
    @Column(name = "ID_AGE_GROUP")
    private Integer id;

    @Column(name = "CODE", nullable = false, length = 16, unique = true)
    private String code;

    @Column(name = "MIN_AGE", nullable = false)
    private Integer minAge;

    @Column(name = "MAX_AGE", nullable = true)  // NULL = open upper bound
    private Integer maxAge;

    /**
     * Verifica si la edad dada pertenece al rango representado por este AgeGroup.
     * 
     * @param age La edad a verificar
     * @return true si la edad está en el rango, false en caso contrario
     */
    public boolean matchesAge(int age) {
        if (age < minAge) {
            return false;
        }
        
        // Si MAX_AGE es null, interpretar como sin tope superior (age >= MIN_AGE)
        if (maxAge == null) {
            return true;
        }
        
        // Si MIN_AGE == MAX_AGE, interpretar como valor exacto
        // En los demás casos, MIN_AGE <= age <= MAX_AGE
        return age <= maxAge;
    }

    /**
     * Parsea un código de edad y crea un AgeGroup con los rangos correspondientes.
     * Formatos soportados:
     * - [num] (ej. "12") → min = max = 12
     * - [num-num] (ej. "6-12") → min = 6, max = 12  
     * - [num+] (ej. "13+") → min = 13, max = null
     */
    public static AgeGroup fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de edad no puede estar vacío");
        }

        code = code.trim();
        
        // Formato: num+ (ej. "13+")
        if (code.endsWith("+")) {
            String numPart = code.substring(0, code.length() - 1);
            int minAge = Integer.parseInt(numPart);
            return AgeGroup.builder()
                    .code(code)
                    .minAge(minAge)
                    .maxAge(null)  
                    .build();
        }
        
        // Formato: num-num (ej. "6-12")
        if (code.contains("-")) {
            String[] parts = code.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato inválido para rango de edad: " + code);
            }
            int minAge = Integer.parseInt(parts[0].trim());
            int maxAge = Integer.parseInt(parts[1].trim());
            return AgeGroup.builder()
                    .code(code)
                    .minAge(minAge)
                    .maxAge(maxAge)
                    .build();
        }
        
        // Formato: num (ej. "12")
        try {
            int age = Integer.parseInt(code);
            return AgeGroup.builder()
                    .code(code)
                    .minAge(age)
                    .maxAge(age)  
                    .build();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato inválido para edad: " + code);
        }
    }
}