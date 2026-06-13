package com.unpa.sgpi.sgpi_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AsignarRolDTO(
        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,
        @NotEmpty(message = "Debe asignarse al menos un rol")
        List<String> roles
) {
}
