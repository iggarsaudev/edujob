package com.edujob.backend.users.infrastructure;

import com.edujob.backend.config.JwtService;
import com.edujob.backend.users.application.*;
import com.edujob.backend.users.domain.User;
import io.swagger.v3.oas.annotations.Operation;
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
    private final OtpService otpService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.dni(), request.pin())
        );
        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar código OTP", description = "Envía un email con el código de 6 dígitos. Devuelve el código en la respuesta si el modo Demo está activo.")
    public ResponseEntity<OtpResponse> requestOtp(@RequestBody OtpRequest request) {
        OtpResponse response = otpService.generateAndSendOtp(request.dni());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Cambiar PIN", description = "Valida el código OTP y establece un nuevo PIN encriptado.")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        otpService.resetPin(request.otp(), request.newPin());
        return ResponseEntity.ok("Tu PIN se ha actualizado correctamente.");
    }
}