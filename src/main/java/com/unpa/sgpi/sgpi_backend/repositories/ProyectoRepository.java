package com.unpa.sgpi.sgpi_backend.repositories;

import com.unpa.sgpi.sgpi_backend.entities.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByInstitutoId(Long institutoId);

    List<Proyecto> findByResponsableId(Long responsableId);
}
