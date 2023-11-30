# Utiliza una imagen base de Python
FROM python:3.9

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo main.py y requirements.txt al directorio /app
COPY main.py requierements.txt /app/

# Instala las dependencias definidas en requirements.txt
RUN pip install --no-cache-dir -r requierements.txt python-consul

# Expone el puerto en el que se ejecutará la aplicación FastAPI
EXPOSE 8000

# Comando para ejecutar la aplicación FastAPI
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]

