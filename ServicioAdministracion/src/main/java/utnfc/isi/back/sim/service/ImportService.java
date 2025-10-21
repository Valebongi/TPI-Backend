package utnfc.isi.back.sim.service;

import utnfc.isi.back.sim.csv.CsvLegoSetRow;
import utnfc.isi.back.sim.domain.Country;
import utnfc.isi.back.sim.domain.Theme;
import utnfc.isi.back.sim.domain.AgeGroup;
import utnfc.isi.back.sim.domain.LegoSet;
import utnfc.isi.back.sim.infra.LocalEntityManagerProvider;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;

public class ImportService {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class ImportResult {
        private int filasLeidas;
        private int filasValidas;
        private int countriesInsertados;
        private int themesInsertados;
        private int ageGroupsInsertados;
        private int legoSetsInsertados;
    }

    public ImportResult importar(List<CsvLegoSetRow> rows) {
        Objects.requireNonNull(rows, "rows");

        // 0) Obtener países válidos de la base de datos
        Set<String> validCountryCodes = getValidCountryCodes();

        // 1) Acumulación en memoria (solo países existentes)
        Map<String, Country> existingCountries = new LinkedHashMap<>();
        Map<String, Theme> themes = new LinkedHashMap<>();
        Map<String, AgeGroup> ageGroups = new LinkedHashMap<>();
        List<PendingLegoSet> pendientes = new ArrayList<>();

        int validas = 0;
        for (var r : rows) {
            Integer prodId = r.getProdId();
            String setName = sanitizeString(r.getSetName());
            String prodDesc = sanitizeString(r.getProdDesc());
            String reviewDifficulty = sanitizeString(r.getReviewDifficulty());
            Integer pieceCount = r.getPieceCount();
            BigDecimal starRating = r.getStarRating();
            BigDecimal listPrice = r.getListPrice();
            String countryCode = sanitizeString(r.getCountry());
            String themeName = sanitizeString(r.getTheme());
            String ageGroupCode = sanitizeString(r.getAgeGroup());

            // VALIDACIÓN ESTRICTA: todos los campos deben estar completos
            if (!isValidRow(r)) {
                continue;
            }
            
            // Validación de país: solo procesar si el país está en el DDL
            if (!isBlank(countryCode) && !validCountryCodes.contains(countryCode.toUpperCase())) {
                continue; // Saltear silenciosamente
            }
            
            validas++;

            // Solo registrar temas y grupos de edad (países ya existen)
            if (!isBlank(themeName)) {
                themes.computeIfAbsent(themeName, name -> Theme.builder()
                        .name(name)
                        .build());
            }

            if (!isBlank(ageGroupCode)) {
                ageGroups.computeIfAbsent(ageGroupCode, code -> AgeGroup.fromCode(code));
            }

            pendientes.add(PendingLegoSet.builder()
                    .prodId(prodId)
                    .setName(setName.trim())
                    .prodDesc(prodDesc != null ? prodDesc.trim() : "")
                    .reviewDifficulty(reviewDifficulty)
                    .pieceCount(pieceCount)
                    .starRating(starRating)
                    .listPrice(listPrice)
                    .countryCode(countryCode)
                    .themeName(themeName)
                    .ageGroupCode(ageGroupCode)
                    .build());
        }

        // 2) Persistencia en una sola transacción
        int insThemes = 0;
        int insAgeGroups = 0;
        int insLegoSets = 0;

        EntityManager em = LocalEntityManagerProvider.em();
        try {
            em.getTransaction().begin();

            // Buscar países existentes y persistir themes/ageGroups
            loadExistingCountries(em, pendientes, existingCountries);
            insThemes = persistOrFindThemes(em, themes);
            insAgeGroups = persistOrFindAgeGroups(em, ageGroups);

            // Persistir LegoSets con referencias a las entidades existentes
            for (var p : pendientes) {
                Country country = !isBlank(p.getCountryCode()) ? 
                    existingCountries.get(p.getCountryCode()) : null;
                Theme theme = !isBlank(p.getThemeName()) ? 
                    themes.get(p.getThemeName()) : null;
                AgeGroup ageGroup = !isBlank(p.getAgeGroupCode()) ? 
                    ageGroups.get(p.getAgeGroupCode()) : null;

                LegoSet legoSet = LegoSet.builder()
                        .prodId(p.getProdId())
                        .setName(p.getSetName())
                        .prodDesc(p.getProdDesc())
                        .reviewDifficulty(p.getReviewDifficulty())
                        .pieceCount(p.getPieceCount())
                        .starRating(p.getStarRating())
                        .listPrice(p.getListPrice())
                        .country(country)
                        .theme(theme)
                        .ageGroup(ageGroup)
                        .build();
                em.persist(legoSet);
                insLegoSets++;
            }

            em.getTransaction().commit();
            System.out.println("Importación exitosa: " + insLegoSets + " sets LEGO importados");
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Error durante la importación: " + ex.getMessage());
            throw ex;
        } finally {
            em.close();
        }

        return ImportResult.builder()
                .filasLeidas(rows.size())
                .filasValidas(validas)
                .countriesInsertados(0) // Ya no se insertan países
                .themesInsertados(insThemes)
                .ageGroupsInsertados(insAgeGroups)
                .legoSetsInsertados(insLegoSets)
                .build();
    }

    // helpers
    private static void loadExistingCountries(EntityManager em, List<PendingLegoSet> pendientes, Map<String, Country> existingCountries) {
        Set<String> countryCodes = new HashSet<>();
        for (var p : pendientes) {
            if (!isBlank(p.getCountryCode())) {
                countryCodes.add(p.getCountryCode().toUpperCase());
            }
        }
        
        for (String code : countryCodes) {
            var existing = em.createQuery("SELECT c FROM Country c WHERE c.code = :code", Country.class)
                    .setParameter("code", code)
                    .getResultList();
            if (!existing.isEmpty()) {
                existingCountries.put(code, existing.get(0));
            }
        }
    }
    
    private static int persistOrFindThemes(EntityManager em, Map<String, Theme> themes) {
        int inserted = 0;
        for (var entry : themes.entrySet()) {
            String name = entry.getKey();
            Theme theme = entry.getValue();
            
            // Buscar si ya existe
            var existing = em.createQuery("SELECT t FROM Theme t WHERE t.name = :name", Theme.class)
                    .setParameter("name", name)
                    .getResultList();
            
            if (existing.isEmpty()) {
                em.persist(theme);
                inserted++;
            } else {
                // Reemplazar con la entidad existente
                themes.put(name, existing.get(0));
            }
        }
        em.flush();
        return inserted;
    }
    
    private static int persistOrFindAgeGroups(EntityManager em, Map<String, AgeGroup> ageGroups) {
        int inserted = 0;
        for (var entry : ageGroups.entrySet()) {
            String code = entry.getKey();
            AgeGroup ageGroup = entry.getValue();
            
            // Buscar si ya existe
            var existing = em.createQuery("SELECT a FROM AgeGroup a WHERE a.code = :code", AgeGroup.class)
                    .setParameter("code", code)
                    .getResultList();
            
            if (existing.isEmpty()) {
                em.persist(ageGroup);
                inserted++;
            } else {
                // Reemplazar con la entidad existente
                ageGroups.put(code, existing.get(0));
            }
        }
        em.flush();
        return inserted;
    }

    private static String sanitizeString(String raw) {
        if (raw == null) return null;
        var t = raw.trim();
        if (t.isEmpty()) return null;
        return t;
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /**
     * Validación estricta: todos los campos del CSV deben estar completos.
     * Incluso si es un campo que no se utiliza, si está vacío, la línea se ignora.
     */
    private static boolean isValidRow(CsvLegoSetRow r) {
        // Verificar que ningún campo sea nulo o vacío
        return r.getProdIdDecimal() != null &&
               !isBlank(r.getSetName()) &&
               !isBlank(r.getProdDesc()) &&
               !isBlank(r.getTheme()) &&
               !isBlank(r.getAgeGroup()) &&
               r.getPieceCountDecimal() != null &&
               r.getListPrice() != null &&
               r.getStarRating() != null &&
               !isBlank(r.getReviewDifficulty()) &&
               !isBlank(r.getCountry()) &&
               r.getNumReviews() != null &&
               r.getPlayStarRating() != null &&  // No se usa pero debe estar
               r.getValStarRating() != null;     // No se usa pero debe estar
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class PendingLegoSet {
        private Integer prodId;
        private String setName;
        private String prodDesc;
        private String reviewDifficulty;
        private Integer pieceCount;
        private BigDecimal starRating;
        private BigDecimal listPrice;
        private String countryCode;
        private String themeName;
        private String ageGroupCode;
    }
    
    /**
     * Obtiene los códigos de países válidos que están pre-cargados en la base de datos.
     */
    private Set<String> getValidCountryCodes() {
        EntityManager em = LocalEntityManagerProvider.em();
        try {
            var result = em.createQuery("SELECT c.code FROM Country c", String.class)
                    .getResultList();
            return new HashSet<>(result);
        } finally {
            em.close();
        }
    }
    

}


