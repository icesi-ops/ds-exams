FROM dperson/samba:latest

# Copia tu archivo de configuración de Samba personalizado
COPY smb.conf /etc/samba/smb.conf

# Crea el directorio de datos si no existe
RUN mkdir -p /home/steb/distribuidos/data && \
    # Añade un nuevo usuario de sistema 'usuario_especifico'
    useradd user_samba && \
    # Establece la contraseña del usuario (cambia 'tu_contraseña' por la contraseña real)
    echo "user_samba:pass123." | chpasswd && \
    # Añade el usuario a Samba y establece su contraseña
    (echo "pass123."; echo "pass123.") | smbpasswd -a user_samba

# Expone los puertos para los servicios de Samba
EXPOSE 139 445

# Comando para ejecutar Samba
CMD ["smbd", "--foreground", "--no-process-group"]
