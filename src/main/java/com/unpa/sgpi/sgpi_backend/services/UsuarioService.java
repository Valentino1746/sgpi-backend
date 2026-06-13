package com.unpa.sgpi.sgpi_backend.services;

import com.unpa.sgpi.sgpi_backend.dto.AsignarRolDTO;
import com.unpa.sgpi.sgpi_backend.dto.UsuarioDTO;
import com.unpa.sgpi.sgpi_backend.entities.Rol;
import com.unpa.sgpi.sgpi_backend.entities.Usuario;
import com.unpa.sgpi.sgpi_backend.repositories.RolRepository;
import com.unpa.sgpi.sgpi_backend.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Administra consultas y asignación de roles para usuarios del SGPI.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAllByActivoTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public UsuarioDTO asignarRoles(AsignarRolDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Set<Rol> roles = new LinkedHashSet<>();
        for (String nombreRol : dto.roles()) {
            Rol rol = rolRepository.findByNombre(nombreRol)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + nombreRol));
            roles.add(rol);
        }
        usuario.setRoles(roles);
        return toDto(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorClave(String clave) {
        Usuario usuario = usuarioRepository.findByClaveTrabajador(clave)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return toDto(usuario);
    }

    private UsuarioDTO toDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getClaveTrabajador(),
                usuario.getNombreCompleto(),
                usuario.getCorreoInstitucional(),
                usuario.getActivo(),
                usuario.getRoles().stream().map(Rol::getNombre).sorted().toList()
        );
    }
}
