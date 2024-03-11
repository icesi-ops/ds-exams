# Utiliza una imagen base de nginx
FROM nginx:alpine

# Copia el archivo index.html al contenedor
COPY index.html /usr/share/nginx/html/index.html

# Expone el puerto 80
EXPOSE 80

# Inicia nginx
CMD ["nginx", "-g", "daemon off;"]
