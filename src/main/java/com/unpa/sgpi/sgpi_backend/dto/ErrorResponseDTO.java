package com.unpa.sgpi.sgpi_backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        int status,
        String mensaje,
        LocalDateTime timestamp,
        Map<String, String> errores
) {
}
