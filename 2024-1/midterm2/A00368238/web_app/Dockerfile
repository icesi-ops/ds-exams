# Usa una imagen base con Python instalado
FROM python:3.9-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo de código Python al directorio de trabajo
COPY app.py .

# Instala Flask, el módulo necesario para ejecutar el servidor web
RUN pip install Flask

# Expone el puerto 5000 para que el servidor pueda ser accesible desde fuera del contenedor
EXPOSE 5000

# Define el comando a ejecutar cuando el contenedor se inicie
CMD ["python", "app.py"]
