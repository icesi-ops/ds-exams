# Primer parcial Sistemas Distribuidos

## Integrantes

**John Sebastian Urbano Lenis -----> A00292788**

**Jhan Carlos Diaz Vidal -----> A00310560**

**Mateo Matta Lopez -----> A00310540**

## Descripción general

Para desarrollar lo requerido en el parcial el equipo decidió utilizar una guìa proporcionada por okta, una plataforma de desarrollo, esta aplicación consta de un Front-End, que se ejecuta en node.js, el Back-End, que se ejecuta con Flask y una base de datos de MongoDB. Esta aplicaciòn permite hacer una busquedad de repositorios de www.github.com, para poder realizar esta busquedad es necesario que el usuario primero se autentique frente a la aplicación con una cuenta de Okta.    
```
Fuente:
Https://developer.okta.com/blog/2018/12/20/crud-app-with-python-flask-react
```
    
Luego de realizar diferentes adaptaciónes a la aplicación propuesta en el tutorial se procedió con la construcción de un vagrant file el cual contempla las siguientes máquinas con las siguientes direcciones y sistemas operativos:

```
   Web-1: Servidor web con front y back ejecutandose : 192.168.33.11  : Ubuntu 
   Web-2: Servidor web con front y back ejecutandose : 192.168.33.12  : CentOS 7
   Lb   : LoadBalancer conectado a Web-1 y Web-2     : 192.168.33.200 : CentOS 7
   Db   : Base de datos con MongoDB ejecutandose     : 192.168.33.14  : CentOS 7
```

Para aprovisionar todas esta máquinas se utilizó SaltStack y un conjunto de Scripts, dependiendo de la máquina. Cabe resaltar que la herramienta que se utilizó para el despliegue automatico de las máquinas fue Vagrant junto con VirtualBox. En cuanto al aprovisionamiento con SaltStack, optamos por la opción masterless, debido la poca complejidad de la infraestructura a aprovisionar, tratando de ahorrar la mayor cantidad de recursos posibles

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

Con el fin de llevar a cabo el despliegue de un balanceador de cargas, se tomó la decisión de usar una maquina virtual con imagen centos/7 de 512MB de RAM, 1 CPU y de nombre lb. Se compartieron por medio de vagrant desde la maquina que aprovisiona los archivos states, pillars y lb.sh que corresponde en orden al archivo del cuál asumirá un estado, el siguiente la ubicación de sus pilares y por ultimo un script ejecutable que corresponde a una configuración inicial. Los comandos de dicha configuración se presentan a continuación:

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

El resultado obtenido al ejecutar la infraestructura y el aprovisionamiento fue exitoso, como evidencia se presenta:



**Documentación del aprovisionamiento de los servidores web**
En el siguiente apartado, se mostrará entonces las configuraciones necesarias para llevar acabo la construccíón y ejecución del servidor web (posteriormente duplicado para usarse con el balanceador de carga). El despliegue de este servicio se realizará a través de una máquina virtual Ubuntu 18.04 de 512GB de RAM, 1 CP. Además del uso del framework React para realizar interfaces de web; Python para crear el REST-API, que tendrá el rol de back-end y Flask para comunicar el front-end con el back-end. Para ejecutar la operación de esta dos máquinas, se deben hacer los ajustes mostrados respectivamente:
    **Front-end**
    Primero, para esta parte de la aplicación web, se debe proceder primero a crear un script, el cual se ejecutará dentro de la máquina virtual para instalar todos los paquetes necesarios (princpalmente de Node.js) que sirven para correr la interfaz web. El archivo en cuestión será llamado "web.sh", y tendrá los siguientes comandos:

```
##!/bin/bash
#update
sudo apt update -y
#Install git
sudo apt install -y git 
# Clone our repository
cd /home/vagrant/ && sudo git clone --single-branch --branch IaC  https://github.com/SebastianUrbano/ds-exams.git
cd /home/vagrant
#Install Node and NPM
curl -sL https://rpm.nodesource.com/setup_10.x | sudo bash -
sudo apt install npm -y
sudo apt install nodejs -y
#Install React
# curl --silent --location https://dl.yarnpkg.com/rpm/yarn.repo | sudo tee /etc/yum.repos.d/yarn.repo
# sudo rpm --import https://dl.yarnpkg.com/rpm/pubkey.gpg
sudo apt install yarn -y
##ojo con lo que falta de react########
# Install SaltStack
sudo curl -L https://bootstrap.saltstack.com -o bootstrap_salt.sh
sudo sh bootstrap_salt.sh
#Put custom minion config in place (for enabling masterless mode)
sudo cp -r /home/vagrant/ds-exams/ConfigurationManagment/minion.d /etc/salt/
echo -e 'grains:\n roles:\n  - web' | sudo tee /etc/salt/minion.d/grains.conf
```
 **Back-end**
    Segundo, en esta parte de la aplicación web, se hace primero un script, que se ejecutará dentro de las máquinas virtuales para instalar todos los paquetes necesarios (principalmente de Python y Flask) que sirven para ejecutar el Back-end del aplicativo web, dentro del mismo llamado "web.sh", y tendrá los siguientes comandos:

```
#Install Python
sudo apt install -y python3
sudo apt install -y python2
#Install pyenv and python2 por si las moscas :v
sudo apt install -y  gcc gcc-c++ make git patch openssl-devel zlib-devel readline-devel sqlite-devel bzip2-devel
git clone git://github.com/yyuu/pyenv.git ~/.pyenv
export PATH="$HOME/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
cat << PYENVCONF >> ~/.zshrc
export PATH="$HOME/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
PYENVCONF
pyenv install 2.7.15
#Install Pip
sudo apt install python3-pip -y
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
python get-pip.py
sudo apt install python3-pip
#Install pip environment
pip install pipenv
	#Hasta aqui instala pip y pipenv, para todos---------------
#Install pip packages
pipenv install flask==1.0.2
pipenv install marshmallow==2.16.3
pipenv install pyjwt==1.7.1
pipenv install flask_cors==3.0.7
sudo pipenv shell
```

Luego, se procede a configurar el archivo de Vagrant que realizará toda la configuración del aprovisionamiento de las máquinas virtuales Ubuntu. Además, se ajusta el aprovisionamiento con Saltstack para que comparta las carpetas con el contenido necesario para que las máquinas virtuales puedan implementar y ejecutar el back y front de la aplicacion; con el siguiente contenido:
	

```
Vagrant.configure("2") do |config|
    config.ssh.insert_key = false
    (1..2).each do |i|
     config.vm.define "web-#{i}" do |web|
       web.vm.box = "ubuntu/bionic64"
       web.vm.hostname = "web-#{i}"
       web.vm.network "private_network", ip: "192.168.33.1#{i}"
       web.vm.provider "virtualbox" do |vb|
        vb.customize ["modifyvm", :id, "--memory", "512", "--cpus", "1", "--name", "web-#{i}"]
       end
      #  web.vm.provision "file", source: "./Scripts/web.sh", destination: "/srv/sc.sh"
      
      ####### File Share #######
      web.vm.synced_folder '../ConfigurationManagment/states', '/srv/salt'
      web.vm.synced_folder '../ConfigurationManagment/pillars', '/srv/pillar'  
      web.vm.synced_folder './Scripts', '/srv/Scripts' 
      ##### APROVISIONAR INICIAL DE LOS SERVIDORES #####  
      web.vm.provision "shell", path: "./Scripts/web.sh"
      ##### APROVISIONAMIENTO DE LOS SERVIDORES ##### 
      web.vm.provision :salt do |salt|
        salt.masterless = true
        salt.run_highstate = true
        salt.verbose = true
      end
    end
   end
```

	Finalmente, se procede a crear un archivo .sls, el cual es un State del Saltstack que, ayudará a ejecutar los servicios ya creados de la aplicación web para ponerla en funcionamiento e instalar otras dependencias de Node.js necesarias. 




**Documentación del aprovisionamiento de la base de datos**

Con el fin de llevar a cabo el despliegue de una base de datos, se tomó la decisión de usar una maquina virtual con imagen centos/7 de 512MB de RAM, 1 CPU, el motor de base de datos mongodb y de nombre db. Se compartieron por medio de vagrant desde la maquina que aprovisiona los archivos states, pillars y db.sh que corresponde en orden al archivo del cuál asumirá un estado, el siguiente la ubicación de sus pilares y por ultimo un script ejecutable que corresponde a una configuración inicial. Los comandos de dicha configuración se presentan a continuación:

```
#!/bin/bash
sudo yum update -y
sudo yum install -y git 
# Clone our repository
sudo git clone https://github.com/SebastianUrbano/ds-exams.git
#Install Python
sudo yum install -y python3
#Install pyenv and python2 
sudo yum install -y  gcc gcc-c++ make git patch openssl-devel zlib-devel readline-devel sqlite-devel bzip2-devel
sudo yum install -y gcc python-devel
git clone git://github.com/yyuu/pyenv.git ~/.pyenv
export PATH="$HOME/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
cat << _PYENVCONF_ >> ~/.zshrc
export PATH="$HOME/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
_PYENVCONF_
pyenv install 2.7.15
# Install SaltStack
sudo curl -L https://bootstrap.saltstack.com -o bootstrap_salt.sh
sudo sh bootstrap_salt.sh
#Put custom minion config in place (for enabling masterless mode)
sudo cp -r /srv/ds-exams/ConfigurationManagment/minion.d /etc/salt/
echo -e 'grains:\n roles:\n  - db' | sudo tee /etc/salt/minion.d/grains.conf
# Doing provision with saltstack


```

Lo anterior, realiza un update para actualizar todos los archivos del sistema CENTOS7, clona el repositorio con el fin de obtener su configuración a aplicar, instala el motor de base de datos mongodb, la instalación de python3 y python2, instalación de pyenv e instala saltstack para posteriormente ser aplicado el aprovisionamiento de su estado, pasado por medio de un grain, desde el Vagrantfile. Este ultimo se muestra a continuación:

```
   config.vm.define "db" do |db|
    db.vm.box = "centos/7"
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "192.168.33.14"
    db.vm.provider "virtualbox" do |vb|
     vb.customize ["modifyvm", :id, "--memory", "512", "--cpus", "1", "--name", "db"]
    end
    ####### File Share #######
    db.vm.synced_folder '../ConfigurationManagment/states', '/srv/salt'
    db.vm.synced_folder '../ConfigurationManagment/pillars', '/srv/pillar'  
    db.vm.synced_folder './Scripts', '/srv/Scripts' 
    ##### APROVISIONAR INICIALMENTE LA BASE DE DATOS ##### 
    db.vm.provision "shell", path: "./Scripts/db.sh"
    ##### APROVISIONAMIENTO DE LA BASE DE DATOS ##### 
    db.vm.provision :salt do |salt|
     salt.masterless = true
     salt.run_highstate = true
     salt.verbose = true
    end
    db.vm.provision "shell", inline: "echo Iam DB server"
   end
 
```

En este, se puede observar que primero aprovisiona por medio del shell el script inicial y por medio de saltstack el resto del sistema y sus configuraciones. El aprovisionamiento que realiza por medio de saltstack se presenta a continuación:

```
Correr_db:
  cmd.run:
    - name: sudo systemctl start mongod

```
El archivo sls de aprovisionamiento contiene el inicio del servicio, anteriormente el sls tenía mayor responsabilidad pero al no funcionar se le quitaron, era el siguiente.

init.sls de db
```
# This setup for mongodb assumes that the replica set can be determined from
# the id of the minion
include:
include:
    - nodejs
  - python.pymongo


install_npm_dependencies:
mongodb:
    npm.bootstrap:
  pkg:
      - name: /srv/ds-exams/aik-app-api
    - installed
  service:
    - running
    - require:
      - file: mongo-data
      - file: /usr/libexec/mongo/repset_init.js
      - file: /etc/mongodb.conf
      - file: /var/log/mongodb
  user:
    - present
    - uid: 70002
    - gid: 70002
    - require: 
      - group: mongodb
  group:
    - present
    - gid: 70002
  mongo-dirs:
    cmd.run:
  file:
      - name: "node /srv/ds-exams/Front-end/server.js"
    - directory
    - user: mongodb
    - group: mongodb
    - mode: 777
    - makedirs: True
    - names:
      - /var/log/mongodb
      - /usr/libexec/mongo
    - require:
      - user: mongodb
      - group: mongodb


run_back:
/etc/mongodb.conf:
    cmd.run:
  file:
      - name: "node /srv/ds-exams/Back-end/server.js"
    - managed
    - user: mongodb
    - group: mongodb
    - mode: 644
    - source: salt://srv/Scripts/ds-exams/ConfigurationManagment/states/IZI-db/mongodb.conf
    - template: jinja
    - require:
      - pkg: mongodb

/usr/libexec/mongo/repset_init.js:
  file:
    - managed
    - source: salt://srv/Scripts/ds-exams/ConfigurationManagment/states/IZI-db/repset.js
    - template: jinja
    - require:
      - file: mongo-dirs

/usr/libexec/mongo/check_mongo_status.sh:
  file:
    - managed
    - source: salt://mongodb/check_mongo_status.sh
    - mode: 755
    - require:
      - file: mongo-dirs

mongo --quiet /usr/libexec/mongo/repset_init.js:
  cmd:
    - run
    - unless: /usr/libexec/mongo/check_mongo_status.sh
    - user: root
    - group: root
    - require:
      - service: mongodb
      - file: /usr/libexec/mongo/check_mongo_status.sh
```

mongodb.conf
```
logpath=/var/log/mongodb/mongodb.log

logappend=true

port = 27017

replSet={{grains['host'].split('-')[1]}}


```

repset.js
```
rs.initiate({
    _id : "{{grains['host'].split('-')[1]}}",
    members : [
        { _id : 0, host : "mongorep1-{{grains['host'].split('-')[1]}}.{{grains['domain']}}" },
        { _id : 1, host : "mongorep2-{{grains['host'].split('-')[1]}}.{{grains['domain']}}" },
        { _id : 2, host : "mongorep3-{{grains['host'].split('-')[1]}}.{{grains['domain']}}" },
    ]
})
```

El resultado obtenido al ejecutar la infraestructura y el aprovisionamiento fue exitoso, como evidencia se presenta:

**Documentación de las tareas de integración**



**Documentación de problemas encontrados**

