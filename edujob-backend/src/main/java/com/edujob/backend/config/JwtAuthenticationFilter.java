package com.edujob.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Buscamos la cabecera "Authorization" en la petición
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userDni;

        // 2. Si no hay cabecera o no empieza por "Bearer ", lo dejamos pasar.
        // (Spring Security lo bloqueará más adelante si la ruta era privada).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (quitando los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);
        
        // 4. Extraemos el DNI del token usando nuestro JwtService
        userDni = jwtService.extractUsername(jwt);

        // 5. Si el DNI existe y el usuario aún no está autenticado en este hilo...
        if (userDni != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Buscamos el usuario en la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userDni);

            // Validamos que el token sea correcto y no esté caducado
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Creamos el objeto de autenticación oficial de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                // Le añadimos detalles de la petición HTTP (ej: la IP)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Guardamos la autenticación en el Contexto de Seguridad (¡El usuario está dentro!)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Pasamos al siguiente filtro de la cadena
        filterChain.doFilter(request, response);
    }
}