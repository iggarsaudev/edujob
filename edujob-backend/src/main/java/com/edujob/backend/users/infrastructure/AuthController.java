package com.edujob.backend.users.infrastructure;

import com.edujob.backend.config.JwtService;
import com.edujob.backend.users.application.AuthResponse;
import com.edujob.backend.users.application.LoginRequest;
import com.edujob.backend.users.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Spring Security comprueba automáticamente si el DNI existe y si el PIN (desencriptado) coincide
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.dni(), request.pin())
        );
        
        // 2. Si llega aquí, es que el login es correcto. Extraemos el usuario.
        User user = (User) auth.getPrincipal();
        
        // 3. Generamos el Token JWT
        String token = jwtService.generateToken(user);
        
        // 4. Se lo devolvemos al frontend (Angular)
        return ResponseEntity.ok(new AuthResponse(token));
    }
}