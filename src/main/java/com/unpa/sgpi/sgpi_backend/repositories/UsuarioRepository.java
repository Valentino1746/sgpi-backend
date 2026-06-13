package com.unpa.sgpi.sgpi_backend.repositories;

import com.unpa.sgpi.sgpi_backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByClaveTrabajador(String claveTrabajador);

    List<Usuario> findAllByActivoTrue();
}
