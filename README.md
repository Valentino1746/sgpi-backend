# SGPI Backend

API REST del SGPI construida con Spring Boot 3, Java 21 y MySQL 8.

## Requisitos

- Java 21
- Maven Wrapper incluido (`./mvnw`)
- MySQL 8

## Variables y configuracion

El backend lee estas propiedades desde variables de entorno:

- `SPRING_DATASOURCE_URL` (default: `jdbc:mysql://localhost:3306/sgpi`)
- `SPRING_DATASOURCE_USERNAME` (default: `sgpi`)
- `SPRING_DATASOURCE_PASSWORD` (default: `sgpi123`)
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`

## Correr con Docker Compose

Desde la raiz del proyecto:

```bash
docker compose up -d mysql backend --build
```

El backend quedara disponible en:

```text
http://localhost:8080
```

Endpoint de salud:

```text
http://localhost:8080/api/health
```

## Correr localmente sin Docker

1. Levanta MySQL 8 y crea la base `sgpi`.
2. Verifica que las credenciales coincidan con `application.properties` o exporta variables de entorno.
3. Desde este directorio ejecuta:

```bash
./mvnw spring-boot:run
```

## Ejecutar pruebas

Las pruebas usan H2 en memoria, asi que no requieren MySQL:

```bash
./mvnw test
```

## Usuarios semilla

Al iniciar la aplicacion se crean estos usuarios:

- `admin` / `Admin123!`
- `inv001` / `Investigador123!`
- `dir001` / `Director123!`
- `inv002` / `Investigador123!`

## Endpoints principales de esta epic

- `POST /api/auth/login`
- `GET /api/health`
- `GET /api/proyectos`
- `GET /api/admin/usuarios`
- `PUT /api/admin/usuarios/roles`
