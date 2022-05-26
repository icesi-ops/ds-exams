## Instructions
Using microk8s w/ metallb, helm3 addons enabled. On Linux/Ubuntu 20.04 


# Configure 
Configure microk8s to work using metallb (loadbalancer) using the guide: https://microk8s.io/docs/addon-metallb

Using the commands:

microk8s enable metallb helm3

# building the docker image (if needed -> already uploaded to DockerHub)

Command:
docker build -t pcspam/projectapp:latest .

# Deploying the app in kubernetes using helm

from root project directory.

helm install projectapp projectapp/ --values projectapp/values.yaml

Now all the pods should be running.

# Checking functionality

Execute the following commands.

kubectl get svc

This will list off all the services running in the kubernetes cluster.

copy/paste on the browser the field corresponding to the loadbalancer's external ip.

on the browser type <loadbalancer-external-ip>:8000

## Evidences

  ![](https://github.com/Legendary-Overlord/ds-exams/blob/master/evidences/helm%20status.png)
  
  ![](https://github.com/Legendary-Overlord/ds-exams/tree/master/evidences/kubeall.png)
  
  ![](https://github.com/Legendary-Overlord/ds-exams/tree/master/evidences/browser.png)
