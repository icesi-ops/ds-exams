# api_gateway/Dockerfile
FROM node:14

WORKDIR /app

COPY package*.json ./

RUN npm install -g express-gateway@1.18.2

COPY . .

CMD ["npm", "start"]
