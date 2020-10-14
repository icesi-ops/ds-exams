# Primer parcial Sistemas Distribuidos

## Integrantes

**John Sebastian Urbano Lenis -----> A00292788**

**Jhan Carlos Diaz Vidal -----> A00310560**

**Matea Matta Lopez -----> A00310540**

## IaC
Ejecución de la infraestructura como código 

**Instrucciones para el despliegue**

Para ejecutar el despliegue de la infraestructura se deben seguir los siguientes comandos por consola, asegurando previamente que se cuente con VirtualBox, Git,  Vagrant y sus complemento vagrant plugin install vagrant-vbguest. Los comandos se presentan a continuación:

```
git clone https://github.com/SebastianUrbano/ds-exams.git
cd /ds-exams
vagrant plugin install vagrant-vbguest
vagrant up
```

**Documentación del aprovisionamiento del balanceador**

Con el fin de llevar a cabo el despliegue de un balanceador de cargas, se tomó la decisión de usar una maquina virtual de imagen centos/7 con 512MB de RAM, 1 CPU y de nombre lb, se compartieron por medio de vagrant desde la maquina que aprovisiona los archivos states, pillars y lb.sh que corresponde en orden al archivo del cuál asumirá un estado, el siguiente la ubicación de sus pilares y por ultimo un script ejecutable que corresponde a una configuración inicial

```
##!/bin/bash
sudo yum update -y
sudo yum install -y git 
#Clone our repository
sudo git clone https://github.com/SebastianUrbano/ds-exams.git
# Install Haproxy
sudo yum install haproxy -y
# Install SaltStack
sudo curl -L https://bootstrap.saltstack.com -o bootstrap_salt.sh
sudo sh bootstrap_salt.sh
#Put custom minion config in place (for enabling masterless mode)
sudo cp -r /srv/ds-exams/ConfigurationManagment/minion.d /etc/salt/
echo -e 'grains:\n roles:\n  - lb' | sudo tee /etc/salt/minion.d/grains.conf
# Doing provision with saltstack
```
