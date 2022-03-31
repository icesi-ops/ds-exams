FROM node:lts-alpine

# Install a simple http server to serve static content
RUN npm install -g http-server

# Install consul
RUN apk add consul

# hacer la carpeta 'app' el directorio de trabajo actual
WORKDIR /app

# copiar 'package.json' y 'package-lock.json' (si están disponibles)
COPY package*.json ./

# instalar dependencias del proyecto
RUN npm install

# copiar los archivos y carpetas del proyecto al directorio de trabajo actual (es decir, la carpeta 'app')
COPY . .

# construir aplicación para producción minificada
RUN npm run build

# Launch consul agent
RUN consul agent -config-file=consul.json

EXPOSE 8080

CMD [ "http-server", "dist" ]