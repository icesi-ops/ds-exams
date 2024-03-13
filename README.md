# Parcial 1 - Sistemas distribuidos
- Por: Juan Esteban Amilcar y Samuel Guerrero

## Parametros para parcial
1. Java para el Backend
2. MySQL para la base de datos

## Como ejecutar
## 1. Instalación de Docker Compose en WSL

1. **Instalar Docker en Windows**: Asegúrate de tener Docker instalado y funcionando en tu sistema Windows. Puedes descargar e instalar Docker Desktop desde el sitio web oficial de Docker.

2. **Habilitar WSL**: Asegúrate de tener WSL habilitado en tu sistema Windows. Puedes habilitarlo desde la configuración de Windows.

3. **Instalar una Distribución de Linux en WSL**: Después de habilitar WSL, necesitarás instalar una distribución de Linux en WSL. Puedes hacerlo desde la Microsoft Store o utilizando el comando `wsl --install`.

4. **Abrir la Distribución de Linux**: Abre la distribución de Linux instalada en WSL desde el menú de inicio o ejecutando el comando `wsl` desde PowerShell o la línea de comandos de Windows.

5. **Actualizar el Sistema de Linux**: Ejecuta los siguientes comandos en la distribución de Linux para asegurarte de que todos los paquetes estén actualizados:
   ```bash
   sudo apt update
   sudo apt upgrade
6. **Instalar Docker Compose**: Una vez en la distribución de Linux, puedes instalar Docker Compose ejecutando los siguientes comandos:
   ```bash
   sudo apt install curl
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh
   sudo usermod -aG docker $USER
7. **Verificar la Instalación**: Después de instalar Docker, verifica que esté funcionando correctamente ejecutando el siguiente comando:
   ```bash
   docker --version
   docker-compose --version

## 2. Descargar proyecto git
``git clone https://github.com/Amilcar-Steban/ds-exams.git``
## 3. Ejecutar proyecto
1. Abre WSL y dirigte al directorio del proyecto recien descargado
2. Dentro del directorio vas a ejecutar el siguiente comando
   ```bash
   docker compose up --build -d
3. Despues vas a verificar que el stack este corrinedo dentro del docker desktop

# Documentacion
## Documentación breve de un servidor Samba en Docker

## Introducción
Samba es una implementación libre del protocolo de archivos compartidos de Windows que permite a los sistemas Unix/Linux compartir archivos e impresoras con los sistemas Windows. En este documento, se proporciona una guía para configurar un servidor Samba utilizando Docker.

## Dockerfile del servidor Samba
   ```Dockerfile
   FROM dperson/samba:latest

   #Copia el archivo de configuración personalizado de Samba
   COPY smb.conf /etc/samba/smb.conf

   #Crea el directorio de datos si no existe
   RUN mkdir -p /home/steb/distribuidos/data

   #Expone los puertos para los servicios de Samba
   EXPOSE 139 445

   #Comando para ejecutar Samba
   CMD ["smbd", "--foreground", "--no-process-group"]
```
## Configuracion Samba
1. El archivo `smb.conf` contiene la configuración global de Samba, así como la definición de compartición de recursos.
2. Se define un recurso compartido llamado storage_samba que permite a los usuarios autenticados acceder y escribir en el directorio /home/steb/distribuidos/data/.

