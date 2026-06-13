package com.unpa.sgpi.sgpi_backend.services;

import com.unpa.sgpi.sgpi_backend.dto.LoginRequestDTO;
import com.unpa.sgpi.sgpi_backend.dto.LoginResponseDTO;
import com.unpa.sgpi.sgpi_backend.entities.Usuario;
import com.unpa.sgpi.sgpi_backend.repositories.UsuarioRepository;
import com.unpa.sgpi.sgpi_backend.security.CustomUserDetailsService;
import com.unpa.sgpi.sgpi_backend.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Resuelve el flujo de autenticación y emisión de JWT del sistema.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioRepository usuarioRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtTokenProvider jwtTokenProvider,
            UsuarioRepository usuarioRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.claveTrabajador(), request.password())
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.claveTrabajador());
        Usuario usuario = usuarioRepository.findByClaveTrabajador(request.claveTrabajador())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        String token = jwtTokenProvider.generateToken(userDetails);
        return new LoginResponseDTO(
                token,
                "Bearer",
                usuario.getClaveTrabajador(),
                usuario.getNombreCompleto(),
                usuario.getRoles().stream().map(role -> role.getNombre()).sorted().toList()
        );
    }
}
