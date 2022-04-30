# Comandos:
# 1. Despligue de Kafka: 
microk8s.kubectl replace -f kafka-deployment.yml
![image](https://user-images.githubusercontent.com/47835629/166124858-f87e54a3-a30e-46a4-9df6-f07605a1f22a.png)
# 2. Despliegue de las bases de datos:
 2.1 MondoDB (para app-transaction): microk8s.kubectl replace -f mongo.yml
 2.2 MySql (para app-pay): microk8s.kubectl replace -f mysql.yml
![image](https://user-images.githubusercontent.com/47835629/166124873-ed9f359d-bd93-4fbf-b18b-d3ee7bb141de.png)
![image](https://user-images.githubusercontent.com/47835629/166125035-c3544f4c-c45c-49ff-adae-9a325ba1a664.png)
# 3. Despliegue del app-config: 
microk8s.kubectl replace -f app-config.yml
![image](https://user-images.githubusercontent.com/47835629/166125041-346e3c59-973e-475b-851e-46fa11354b78.png)
# 4. Despliegue de los microservicios:
  4.1 App-Pay: microk8s.kubectl replace -f app-transaction.yml
  4.2 App-Transaction (La que el profesor me propuso): microk8s.kubectl replace -f app-transaction.yml
  
![image](https://user-images.githubusercontent.com/47835629/166125066-66859c6e-9452-4828-9957-675cc3e982f7.png)

