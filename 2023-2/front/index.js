const express = require('express');
const Consul = require('consul');
const path = require('path');

const app = express();
const port = 8080;

// Cambia estas variables según la ubicación y el puerto de tu servicio Consul
const consulHost = 'consul'; // Nombre del contenedor Consul
const consulPort = 8500;

// Conexión a Consul
const consul = new Consul({
  host: consulHost,
  port: consulPort,
});

// Endpoint para la página principal
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

// Endpoint para registrar el servicio en Consul
app.get('/register', async (req, res) => {
  const details = {
    name: 'frontend-service',
    tags: ['frontend'],
    address: 'localhost', // Puedes cambiar esto según tus necesidades
    port: port,
    check: {
      http: `http://frontend:${port}/health`,
      interval: '10s',
      timeout: '2s',
    },
  };

  try {
    await consul.agent.service.register(details);
    res.send('Service registered in Consul');
  } catch (error) {
    res.status(500).send(`Error registering service: ${error.message}`);
  }
});

// Endpoint de salud
app.get('/health', (req, res) => {
  res.send('OK');
});

app.listen(port, () => {
  console.log(`Server listening at http://frontend:${port}`);
});

