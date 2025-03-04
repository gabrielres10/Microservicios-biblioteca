## Integrantes
- Gabriel Restrepo Giraldo
- Luis Botero Roba
- Juan González Torres
---
# Proyecto Biblioteca

Este proyecto consiste en un sistema de gestión de biblioteca que cuenta con dos arquitecturas:
- **Monolito**: Versión inicial con toda la lógica de negocio centralizada. Por el momento no presenta modificaciones importantes. Proporciona el funcionamiento más básico de una biblioteca utilizando un monolito.
- **Microservicios**: Versión modular basada en principios de Domain-Driven Design (DDD).

## Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Arquitectura](#arquitectura)
   - [Monolito](#monolito)
   - [Microservicios](#microservicios)
3. [Instalación y Ejecución](#instalación-y-ejecución)
4. [Servicios](#servicios)
5. [Comunicación entre Microservicios](#comunicación-entre-microservicios)
6. [Tecnologías](#tecnologías)
7. [Base de Datos](#base-de-datos)

---

## Descripción General
El sistema permite gestionar préstamos, devoluciones, inventario de libros, gestión de correos de usuarios, con una arquitectura que facilita la escalabilidad y el mantenimiento.

## Arquitectura

### Monolito
La primera versión del proyecto contiene toda la lógica de negocio centralizada en una única aplicación, con acceso directo a la base de datos en H2.

### Microservicios
El sistema fue dividido en varios microservicios independientes:
- Gestión de Catálogo
- Gestión de Notificación
- Gestión de Circulación
- Gestión de Usuarios

Cada microservicio expone una API REST y utiliza bases de datos independientes.

## Instalación y Ejecución

### Requisitos
- JDK 17+
- Maven
- Docker (Se necesita para gestionar la autenticación con Keycloak)

### Ejecución
#### Monolito
```bash
cd Monolito
./mvnw spring-boot:run
```

#### Microservicios
Ejecutar cada microservicio de manera independiente:
```bash
cd Microservicios
chmod +x start-all-microservices.sh
./start-all-microservices.sh
```

## Servicios

| Servicio                | Descripción                 | Puerto |
|-------------------------|-----------------------------|-------|
| Gestión de Usuarios     | Actualización de usuarios         | 8081  |
| Gestión de Catálogo       | Consulta y actualización de libros           | 8082  |
| Gestión de Circulación    | Préstamos y devoluciones    | 8083  |
| Gestión de Notificaciones      | Envío de notificaciones      | 8084  |

## Comunicación entre Microservicios
Por el momento, la comunicación se realiza mediante **Feign** con clientes Feign.

## Tecnologías
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- Docker
- Keycloak

## Base de Datos
Cada microservicio cuenta con su propia base de datos H2, garantizando la independencia de datos.
