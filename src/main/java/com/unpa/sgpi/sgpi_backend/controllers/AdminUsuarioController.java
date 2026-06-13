package com.unpa.sgpi.sgpi_backend.controllers;

import com.unpa.sgpi.sgpi_backend.dto.AsignarRolDTO;
import com.unpa.sgpi.sgpi_backend.dto.UsuarioDTO;
import com.unpa.sgpi.sgpi_backend.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Permite al administrador consultar usuarios y administrar roles.
 */
@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PutMapping("/roles")
    public ResponseEntity<UsuarioDTO> asignarRoles(@Valid @RequestBody AsignarRolDTO dto) {
        return ResponseEntity.ok(usuarioService.asignarRoles(dto));
    }
}
