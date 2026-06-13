package com.unpa.sgpi.sgpi_backend.services;

import com.unpa.sgpi.sgpi_backend.dto.ProyectoResumenDTO;
import com.unpa.sgpi.sgpi_backend.entities.Proyecto;
import com.unpa.sgpi.sgpi_backend.entities.Usuario;
import com.unpa.sgpi.sgpi_backend.repositories.InstitutoRepository;
import com.unpa.sgpi.sgpi_backend.repositories.ProyectoRepository;
import com.unpa.sgpi.sgpi_backend.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Orquesta la visibilidad de proyectos según el rol activo del usuario autenticado.
 */
@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InstitutoRepository institutoRepository;

    public ProyectoService(
            ProyectoRepository proyectoRepository,
            UsuarioRepository usuarioRepository,
            InstitutoRepository institutoRepository
    ) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.institutoRepository = institutoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProyectoResumenDTO> listarProyectos(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByClaveTrabajador(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .collect(java.util.stream.Collectors.toSet());

        List<Proyecto> proyectos;
        if (roles.contains("ADMIN") || roles.contains("RECTORIA")) {
            proyectos = proyectoRepository.findAll();
        } else if (roles.contains("DIRECTOR")) {
            proyectos = institutoRepository.findByDirectorId(usuario.getId())
                    .map(instituto -> proyectoRepository.findByInstitutoId(instituto.getId()))
                    .orElse(List.of());
        } else {
            proyectos = proyectoRepository.findByResponsableId(usuario.getId());
        }

        return proyectos.stream()
                .map(proyecto -> new ProyectoResumenDTO(
                        proyecto.getId(),
                        proyecto.getTitulo(),
                        proyecto.getEstado(),
                        proyecto.getInstituto().getNombre()
                ))
                .toList();
    }
}
