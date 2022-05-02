Lets start with the evidence of all the pods required for the deployment running:

The first step for the deployment of the application was to modify the applications configuration files that are found in the base repositorie (https://github.com/icesi-ops/training_microservices/tree/master/pay-app-spring-microservices):
  *We needed to remove all the lines related with consul
  *We needed to change all the address structure to match kubernetes format, using the envioromental variable that are created
  
  ![imagen](https://user-images.githubusercontent.com/44851531/166123570-46398acf-81e9-4c7a-ad14-450e2a65dece.png)
  
  ![imagen](https://user-images.githubusercontent.com/44851531/166123580-994b9bbd-c74d-4e5d-ae37-022999b872a5.png)
  
When all the above changes are made we need to make the docker images and push them to Dockerhub:

  docker build -t nelsonq2424/app-config .
  docker push nelsonq2424/app-config
 
When we have all the required images of the containers in the Dockerhub repository we can start to write the .yml files for the deployment
Once the files are created we can start to deploy them.

First we need to create the namespace for the deployment:

    kubectl create -f .\ns.yml


Then we modify the context

    kubectl config set-context --current --namespace=parcial2

The order is important in this particular applicaction given the fact that the app-pay and app-invoice microservices depend on the app-config and Kafka microservice we should start
with the deployment of app-config.


  # App-config
  
     kubectl create -f .\app-config-deploy-service.yml
  
  We use the next commands to check that the deployment was succesfull
  
    kubectl get pods
    kubectl logs app-config-deploy-78cf4b8ddb-6qxgf

![imagen](https://user-images.githubusercontent.com/44851531/166124470-9516ef2a-7cf4-4790-8c40-4a2a253d3f6e.png)

![imagen](https://user-images.githubusercontent.com/44851531/166124492-69a87140-d994-4c34-8bc5-5a0d3cdfd090.png)

  # Kafka
  
    kubectl create -f .\kafka-deployment.yml
  
  The container when running shows a list of enviromental variables
  ![imagen](https://user-images.githubusercontent.com/44851531/166124637-b6e8c3cb-240f-4acc-b542-c6c25a72aa93.png)
  
  ![imagen](https://user-images.githubusercontent.com/44851531/166124626-de93a9b1-9592-4f65-99b5-f5542028c026.png)

Now we need to create the databases for the app-pay and app-invoice microservices
  # App-pay DB
  
    kubectl create -f .\app-pay-db-deploy.yml
  
  ![imagen](https://user-images.githubusercontent.com/44851531/166124742-99dbf87e-4c03-4766-a6ef-805c377daa1c.png)
  
  ![imagen](https://user-images.githubusercontent.com/44851531/166124784-69781949-496b-4259-b7d4-6bab637a531e.png)

  # App-invoice DB
  
    kubectl create -f .\app-invoice-db-deploy.yml

   ![imagen](https://user-images.githubusercontent.com/44851531/166124850-08c00ae9-d6c6-481d-a999-7389f67350c2.png)

Now we deploy the app-pay and app-invoice apps:

  # App-pay
  
    kubectl create -f .\app-pay-deploy.yml
    
   ![imagen](https://user-images.githubusercontent.com/44851531/166124881-93ce9af1-ff58-478b-96f8-865a8a4a4a5b.png)
   
   ![imagen](https://user-images.githubusercontent.com/44851531/166125298-c703390b-9013-4589-b2b7-bcea6124fcc3.png)


  # App-invoice
    
    kubectl create -f .\app-invoice-deploy.yml
    
    ![imagen](https://user-images.githubusercontent.com/44851531/166125257-9321b3fb-aab0-46b4-a1f9-390fd5032509.png)


    
    
  
