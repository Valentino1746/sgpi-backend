package com.unpa.sgpi.sgpi_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "La clave de trabajador es obligatoria")
        String claveTrabajador,
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
