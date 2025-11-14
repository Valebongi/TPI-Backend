package utnfc.isi.back.sim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuración de Spring Security para ServicioLogistica
 * 
 * Estrategia de seguridad para logística:
 * - ADMIN: Acceso completo (crear, leer, modificar, eliminar rutas y tramos)
 * - TRANSPORTISTA: Operaciones logísticas (leer rutas/tramos, iniciar/finalizar tramos asignados)
 * - CLIENTE: Solo lectura de sus propias rutas (limitado)
 * 
 * Endpoints críticos que requieren roles específicos:
 * - POST /rutas: ADMIN + TRANSPORTISTA (crear rutas es operativo)
 * - DELETE /rutas: Solo ADMIN
 * - PUT /tramos/{id}/asignar: ADMIN + TRANSPORTISTA (asignación de camiones)
 * - PUT /tramos/{id}/iniciar: TRANSPORTISTA (iniciar viaje)
 * - PUT /tramos/{id}/finalizar: TRANSPORTISTA (finalizar viaje)
 * - GET: Acceso según contexto (todos los roles autenticados para consultas básicas)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers(HttpMethod.GET, "/", "/actuator/health").permitAll()
                
                // ENDPOINTS PROTEGIDOS ESPECÍFICOS
                .requestMatchers(HttpMethod.POST, "/rutas/desde-solicitud/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tramos/*/asignar").hasRole("ADMIN")
                
                // ENDPOINTS INTERNOS SIN AUTENTICACIÓN (para comunicación entre servicios)
                .requestMatchers(HttpMethod.POST, "/tramos/automaticos/interno").permitAll()
                .requestMatchers(HttpMethod.GET, "/tramos/*/interno").permitAll()
                .requestMatchers(HttpMethod.POST, "/rutas/tentativas/interno").permitAll()
                
                // TODO LO DEMÁS SIN AUTENTICACIÓN (temporalmente)
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable());
            
        return http.build();
    }

    /**
     * Convierte los roles del JWT de Keycloak en autoridades de Spring Security
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }

    /**
     * Extractor de roles desde el claim "realm_access" de Keycloak
     */
    public static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            
            if (realmAccess == null) {
                return List.of();
            }
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            
            if (roles == null) {
                return List.of();
            }
            
            // Convertir roles de Keycloak a autoridades de Spring Security
            return roles.stream()
                    .filter(role -> List.of("cliente", "admin", "transportista").contains(role))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        }
    }
}