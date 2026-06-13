package com.unpa.sgpi.sgpi_backend.controllers;

import com.unpa.sgpi.sgpi_backend.dto.ProyectoResumenDTO;
import com.unpa.sgpi.sgpi_backend.services.ProyectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Expone la consulta de proyectos con filtrado por rol e instituto.
 */
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResumenDTO>> listarProyectos(Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarProyectos(authentication));
    }
}
