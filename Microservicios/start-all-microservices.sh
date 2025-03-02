#!/bin/bash

# Microservices names
MICROSERVICES=("microservicio-catalogo" "microservicio-circulacion" "microservicio-notificacion" "microservicio-usuarios")

# Iterar por cada servicio
for MICROSERVICE in "${MICROSERVICES[@]}"
do
    if [ -d "$MICROSERVICE" ]; then
        echo "Starting microservice $MICROSERVICE..."
        
        # Ejecutar cada microservicio en una terminal aparte
        gnome-terminal -- bash -c "cd $MICROSERVICE && ./mvnw spring-boot:run; exec bash"

        # Esperar 2 segundos entre cada inicio para no saturar la máquina
        sleep 2
    else
        echo "No se encontró la carpeta $MICROSERVICE"
    fi
done

echo "All microservices have been launched"
