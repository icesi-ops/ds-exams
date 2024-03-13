# Utiliza una imagen base de Consul
FROM consul:1.10.6

# Exponer puertos necesarios para Consul
EXPOSE 8300 8301 8301/udp 8302 8302/udp 8500 8600 8600/udp

# Configura el agente de Consul para unirse a un clúster (ajusta según sea necesario)
CMD ["agent", "-dev", "-client=0.0.0.0"]
