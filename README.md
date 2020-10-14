# Primer parcial Sistemas Distribuidos

## Integrantes

**John Sebastian Urbano Lenis -----> A00292788**

**Jhan Carlos Diaz Vidal -----> A00310560**

**Matea Matta Lopez -----> A00310540**

## Descripción general

Aquí va la descripción de Carlitos
WEB-1 192.168.33.11
WEB-2 192.168.33.12
LB 192.168.33.200
DB 192.168.33.14

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

Con el fin de llevar a cabo el despliegue de un balanceador de cargas, se tomó la decisión de usar una maquina virtual con imagen centos/7 de 512MB de RAM, 1 CPU y de nombre lb, se compartieron por medio de vagrant desde la maquina que aprovisiona los archivos states, pillars y lb.sh que corresponde en orden al archivo del cuál asumirá un estado, el siguiente la ubicación de sus pilares y por ultimo un script ejecutable que corresponde a una configuración inicial. Los comandos de dicha configuración se presentan a continuación:

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
Lo anterior, realiza un update para actualizar todos los archivos del sistema CENTOS7, clona el repositorio con el fin de obtener su configuración a aplicar, instala el servicio HAproxy e instala saltstack para posteriormente ser aplicado el aprovisionamiento de su estado, pasado por medio de un grain, desde el Vagrantfile. Este ultimo se muestra a continuación:

```
    config.vm.define "lb" do |lb|
     lb.vm.box = "centos/7"
     lb.vm.hostname = "lb"
     lb.vm.network "private_network", ip: "192.168.33.200"
     lb.vm.provider "virtualbox" do |vb|
      vb.customize ["modifyvm", :id, "--memory", "512", "--cpus", "1", "--name", "lb"]
     end
    ####### File Share #######
    lb.vm.synced_folder '../ConfigurationManagment/states', '/srv/salt'
    lb.vm.synced_folder '../ConfigurationManagment/pillars', '/srv/pillar'  
    lb.vm.synced_folder './Scripts', '/srv/Scripts' 
    ##### APROVISIONAR INICIAL DEL LB #####  
    lb.vm.provision "shell", path: "./Scripts/lb.sh"
    ##### APROVISIONAMIENTO DEL LB##### 
    lb.vm.provision :salt do |salt|
      salt.masterless = true
      salt.run_highstate = true
      salt.verbose = true
    end
  end
 ```

En este, se puede observar que primero aprovisiona por medio del shell el script inicial y por medio de saltstack el resto del sistema y sus configuraciones. El aprovisionamiento que realiza por medio de saltstack se presenta a continuación:

```
install_haproxy:
  pkg.installed:
    - pkgs:
      - haproxy

/etc/haproxy/haproxy.cfg:
  file.append:
    - text: |
        
        frontend main
            bind *:8080
            default_backend nodes
        
        backend nodes
            balance roundrobin
            server web-1 192.168.33.11:8080 check
            server web-2 192.168.33.12:8080 check

run_haproxy:
  cmd.run:
    - name: sudo systemctl restart haproxy

```
El archivo sls de aprovisionamiento contiene primero la verificación de la instalación de haproxy, el indexado de la configuración necesaria para que el balanceador apunte a los hots disponibles y el inicio del servicio.
