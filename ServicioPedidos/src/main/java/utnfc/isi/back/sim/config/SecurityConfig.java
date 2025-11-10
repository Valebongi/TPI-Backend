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
                
                // Configurar autorización por endpoints según estrategia de 3 roles
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()  // Home endpoint
                        
                        // Endpoints de clientes - ADMIN gestiona, CLIENTE ve su perfil
                        .requestMatchers(HttpMethod.POST, "/clientes").hasRole("ADMIN")  // Solo ADMIN crea clientes
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")  // ADMIN o propio cliente
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")  // Solo ADMIN elimina
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")  // ADMIN ve todos, CLIENTE el suyo
                        
                        // Endpoints de contenedores - ADMIN gestiona, CLIENTE sus contenedores
                        .requestMatchers(HttpMethod.POST, "/contenedores").hasAnyRole("ADMIN", "CLIENTE")  // ADMIN y CLIENTE pueden crear
                        .requestMatchers(HttpMethod.PUT, "/contenedores/**").hasRole("ADMIN")  // Solo ADMIN modifica
                        .requestMatchers(HttpMethod.DELETE, "/contenedores/**").hasRole("ADMIN")  // Solo ADMIN elimina
                        .requestMatchers(HttpMethod.PATCH, "/contenedores/**/estado").hasAnyRole("ADMIN", "TRANSPORTISTA")  // Estado lo cambia admin/transportista
                        .requestMatchers(HttpMethod.GET, "/contenedores/**").hasAnyRole("ADMIN", "CLIENTE", "TRANSPORTISTA")  // Todos pueden consultar
                        
                        // Endpoints adicionales de clientes
                        .requestMatchers(HttpMethod.PATCH, "/clientes/**/desactivar").hasRole("ADMIN")  // Solo admin desactiva
                        
                        // Endpoints de solicitudes según roles y lógica de negocio
                        .requestMatchers(HttpMethod.POST, "/solicitudes").hasAnyRole("CLIENTE", "ADMIN")  // Cliente crea, admin también
                        .requestMatchers(HttpMethod.PUT, "/solicitudes/**/estado").hasAnyRole("ADMIN", "TRANSPORTISTA")  // Admin y transportista cambian estado
                        .requestMatchers(HttpMethod.PUT, "/solicitudes/**").hasRole("ADMIN")  // Solo admin modifica otros campos
                        .requestMatchers(HttpMethod.DELETE, "/solicitudes/**").hasRole("ADMIN")  // Solo admin elimina
                        .requestMatchers(HttpMethod.GET, "/solicitudes/**").hasAnyRole("ADMIN", "CLIENTE", "TRANSPORTISTA")  // Todos pueden consultar
                        
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
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