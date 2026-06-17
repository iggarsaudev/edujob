package com.edujob.backend.users.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Pruebas del Sistema", description = "Endpoints internos para validar la configuración de seguridad")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("¡Hola! Si ves esto, es que tu Token JWT es válido y tienes acceso a la zona privada de EduJob.");
    }
}