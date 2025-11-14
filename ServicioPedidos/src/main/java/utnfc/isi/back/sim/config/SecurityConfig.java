package utnfc.isi.back.sim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuración de seguridad para el Servicio de Pedidos
 * Configura Spring Security como Resource Server para validar tokens JWT de Keycloak
 * 
 * Implementa jerarquía de roles:
 * - ADMIN: Acceso total (operador/administrador)
 * - TRANSPORTISTA: Acceso a consultas y actualizaciones específicas
 * - CLIENTE: Acceso limitado a sus propios recursos
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF para APIs REST
                .csrf(AbstractHttpConfigurer::disable)
                
                // Configuración simplificada de autorización - Solo endpoints específicos protegidos
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        
                        // ENDPOINTS PROTEGIDOS ESPECÍFICOS
                        .requestMatchers(HttpMethod.POST, "/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/clientes").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/contenedores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contenedores").hasAnyRole("ADMIN", "TRANSPORTISTA", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/solicitudes").hasRole("CLIENTE")
                        
                        // TODO LO DEMÁS SIN AUTENTICACIÓN
                        .anyRequest().permitAll()
                )
                
                // Configurar Resource Server JWT
                .oauth2ResourceServer(oauth2 -> 
                        oauth2.jwt(jwt -> 
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Convertidor para extraer los roles de Keycloak desde el token JWT
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Extraer roles del realm_access
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) {
                return List.of();
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) {
                return List.of();
            }

            return roles.stream()
                    .filter(role -> role.equals("cliente") || role.equals("admin") || role.equals("transportista")) // 3 roles
                    .map(role -> "ROLE_" + role.toUpperCase()) // Convertir a mayúsculas para Spring Security
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}