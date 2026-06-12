package com.unpa.sgpi.sgpi_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para verificar la salud y disponibilidad de los servicios del Backend.
 * Proporciona endpoints de diagnóstico y validación inicial del entorno.
 *
 * @author Especialista en Docker DevOps / Backend Engineer
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    /**
     * Endpoint de salud del sistema.
     * Retorna un estado HTTP 200 (OK) y un mensaje con formato JSON.
     *
     * @return Un mapa con las propiedades 'status' y 'message'
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Hola Mundo");
        return ResponseEntity.ok(response);
    }
}
