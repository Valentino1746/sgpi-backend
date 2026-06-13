package com.unpa.sgpi.sgpi_backend.dto;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String tipo,
        String claveTrabajador,
        String nombreCompleto,
        List<String> roles
) {
}
