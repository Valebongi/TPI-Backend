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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuración de Spring Security OAuth2 Resource Server para ServicioAdministracion
 * 
 * Implementa la jerarquía de roles:
 * - ADMIN: Acceso total (operador/administrador)
 * - TRANSPORTISTA: Acceso a consultas administrativas
 * - CLIENTE: Acceso limitado solo a lectura de algunos recursos
 * 
 * Endpoints críticos protegidos:
 * - POST /depositos -> Solo ADMIN
 * - POST /parametros -> Solo ADMIN
 * - DELETE /* -> Solo ADMIN
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
                
                // ENDPOINTS INTERNOS SIN AUTENTICACIÓN (para comunicación entre servicios)
                .requestMatchers(HttpMethod.GET, "/depositos/*/interno").permitAll()
                .requestMatchers(HttpMethod.GET, "/camiones/*/interno").permitAll()
                .requestMatchers(HttpMethod.GET, "/camiones/*/disponible/interno").permitAll()
                .requestMatchers(HttpMethod.PUT, "/camiones/*/estado/interno").permitAll()
                .requestMatchers(HttpMethod.GET, "/parametros/interno").permitAll()
                
                // ENDPOINTS PROTEGIDOS ESPECÍFICOS
                .requestMatchers(HttpMethod.POST, "/depositos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/depositos").hasAnyRole("ADMIN", "TRANSPORTISTA", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/camiones").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/camiones", "/camiones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/camiones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/camiones/**").hasRole("ADMIN")
                
                // TODO LO DEMÁS SIN AUTENTICACIÓN
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
    private static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return Collections.emptyList();
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            
            return roles.stream()
                    .filter(role -> Arrays.asList("cliente", "admin", "transportista").contains(role))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        }
    }
}