package com.unpa.sgpi.sgpi_backend.repositories;

import com.unpa.sgpi.sgpi_backend.entities.Instituto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutoRepository extends JpaRepository<Instituto, Long> {

    Optional<Instituto> findByDirectorId(Long directorId);
}
