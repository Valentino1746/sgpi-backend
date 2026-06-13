package com.unpa.sgpi.sgpi_backend.config;

import com.unpa.sgpi.sgpi_backend.entities.Instituto;
import com.unpa.sgpi.sgpi_backend.entities.Proyecto;
import com.unpa.sgpi.sgpi_backend.entities.Rol;
import com.unpa.sgpi.sgpi_backend.entities.Usuario;
import com.unpa.sgpi.sgpi_backend.repositories.InstitutoRepository;
import com.unpa.sgpi.sgpi_backend.repositories.ProyectoRepository;
import com.unpa.sgpi.sgpi_backend.repositories.RolRepository;
import com.unpa.sgpi.sgpi_backend.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashSet;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            InstitutoRepository institutoRepository,
            ProyectoRepository proyectoRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (rolRepository.count() > 0 || usuarioRepository.count() > 0) {
                LOGGER.info("Seed omitido: la base de datos ya contiene información.");
                return;
            }

            Rol adminRol = rolRepository.save(new Rol("ADMIN", "Administrador del sistema"));
            Rol investigadorRol = rolRepository.save(new Rol("INVESTIGADOR", "Investigador responsable"));
            Rol directorRol = rolRepository.save(new Rol("DIRECTOR", "Director de instituto"));
            Rol rectoriaRol = rolRepository.save(new Rol("RECTORIA", "Rectoría y vice-rectoría académica"));
            LOGGER.info("Roles base creados.");

            Usuario admin = createUser(
                    "admin",
                    "Admin SGPI",
                    "admin@unpa.edu.mx",
                    "admin.personal@correo.com",
                    passwordEncoder.encode("Admin123!"),
                    List.of(adminRol, rectoriaRol)
            );
            Usuario investigador = createUser(
                    "inv001",
                    "Dra. Elena Hernández",
                    "elena.hernandez@unpa.edu.mx",
                    "elena.h@gmail.com",
                    passwordEncoder.encode("Investigador123!"),
                    List.of(investigadorRol)
            );
            Usuario director = createUser(
                    "dir001",
                    "Dr. Manuel Bautista",
                    "manuel.bautista@unpa.edu.mx",
                    "manuel.b@gmail.com",
                    passwordEncoder.encode("Director123!"),
                    List.of(directorRol)
            );
            Usuario investigadorDos = createUser(
                    "inv002",
                    "Mtro. Carlos Juárez",
                    "carlos.juarez@unpa.edu.mx",
                    "cjuarez@gmail.com",
                    passwordEncoder.encode("Investigador123!"),
                    List.of(investigadorRol)
            );

            admin = usuarioRepository.save(admin);
            investigador = usuarioRepository.save(investigador);
            director = usuarioRepository.save(director);
            investigadorDos = usuarioRepository.save(investigadorDos);
            LOGGER.info("Usuarios semilla creados.");

            Instituto biotecnologia = new Instituto();
            biotecnologia.setCodigo("05");
            biotecnologia.setIniciales("IB");
            biotecnologia.setNombre("Instituto de Biotecnología");
            biotecnologia.setCampus("Loma Bonita");
            biotecnologia.setDirector(director);

            Instituto ingenieria = new Instituto();
            ingenieria.setCodigo("03");
            ingenieria.setIniciales("II");
            ingenieria.setNombre("Instituto de Ingeniería");
            ingenieria.setCampus("Tuxtepec");

            biotecnologia = institutoRepository.save(biotecnologia);
            ingenieria = institutoRepository.save(ingenieria);
            LOGGER.info("Institutos base creados.");

            proyectoRepository.saveAll(List.of(
                    new Proyecto("Biofertilizantes para cultivos regionales", "EN_REVISION", biotecnologia, investigador),
                    new Proyecto("Sensores IoT para laboratorios", "APROBADO", biotecnologia, director),
                    new Proyecto("Plataforma de manufactura inteligente", "CAPTURA", ingenieria, investigadorDos)
            ));
            LOGGER.info("Proyectos demo creados.");
        };
    }

    private Usuario createUser(
            String clave,
            String nombre,
            String correoInstitucional,
            String correoPersonal,
            String passwordHash,
            List<Rol> roles
    ) {
        Usuario usuario = new Usuario();
        usuario.setClaveTrabajador(clave);
        usuario.setNombreCompleto(nombre);
        usuario.setCorreoInstitucional(correoInstitucional);
        usuario.setCorreoPersonal(correoPersonal);
        usuario.setPasswordHash(passwordHash);
        usuario.setActivo(true);
        usuario.setRoles(new LinkedHashSet<>(roles));
        return usuario;
    }
}
