package com.unpa.sgpi.sgpi_backend.dto;

import java.util.List;

public record UsuarioDTO(
        Long id,
        String claveTrabajador,
        String nombreCompleto,
        String correoInstitucional,
        Boolean activo,
        List<String> roles
) {
}
