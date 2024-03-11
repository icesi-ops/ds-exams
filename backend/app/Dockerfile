# Usar una imagen base de Java más reciente
FROM openjdk:11-jdk-slim

# Instalar samba-client
RUN apt-get update && apt-get install -y samba-client

# Crear directorio para la aplicación
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY target/*.jar app-1.0-SNAPSHOT.jar

# Exponer el puerto en el que se ejecuta la aplicación
EXPOSE 4567

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app-1.0-SNAPSHOT.jar"]
