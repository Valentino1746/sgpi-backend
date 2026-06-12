# AGENTS.md - Contexto de Agente de Backend (sgpi-backend)

Este archivo proporciona el contexto y los estándares del proyecto de backend para los agentes de desarrollo de IA.

## 1. Vista General y Arquitectura
- **Proyecto:** Backend del Sistema Integral de Gestión de Proyectos de Investigación (SGPI) de la UNPA.
- **Pila Tecnológica:** Java 21, Spring Boot 3 (Spring Data JPA, Spring Security), MySQL 8.
- **Estilo Arquitectónico:** Clean Architecture y principios SOLID. Separación estricta de controladores, servicios, repositorios y entidades.

## 2. Base de Datos y Persistencia
- **Regla de Normalización:** Tercera Forma Normal (3FN).
- **Control de Cambios:** Si modificas el esquema, genera y verifica las entidades de Hibernate/JPA correspondientes.
- **Borrado Lógico:** Usar soft deletes (preservar histórico en las tablas en lugar de eliminación física) para auditoría.

## 3. Estándares de Codificación
- **Validación de Entradas:** Uso estricto de anotaciones de Jakarta Validation en los DTOs.
- **Documentación:** JavaDocs requeridos en todas las clases de servicio y controladores principales.
- **Seguridad:** Autenticación y autorización basada en JWT.

## 4. Pruebas y QA
- **Pruebas Unitarias:** Implementar pruebas JUnit robustas enfocadas en casos límite, estados nulos y condiciones de carrera.

## 5. Reglas de Git y Colaboración
- **Ramas:** Crear ramas de características `feature/epic-X-descripcion` (o `fix/...`) que deriven de `develop`. La planeación, ejecución y creación de Pull Requests se gestionan a nivel de Épica completa.
- **Commits:** Mensajes bajo Conventional Commits (ej. `feat:`, `fix:`, `docs:`, `test:`).Commits atomicos
- **PRs:** Todas las solicitudes de Pull Request deben apuntar a la rama `develop`. El agente `Pull Request Reviewer` evaluará los cambios con comentarios inline según severidad (`[CRITICAL]`, `[MAJOR]`, `[MINOR]`).
