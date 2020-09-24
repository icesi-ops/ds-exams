##!/bin/bash
#update
sudo yum update -y
#Install git
sudo yum install -y git 
# Clone our repository
sudo git clone https://github.com/SebastianUrbano/ds-exams.git
#Install Python
yum install -y python3
#Install pyenv and python2 por si las moscas :v
yum install -y  gcc gcc-c++ make git patch openssl-devel zlib-devel readline-devel sqlite-devel bzip2-devel
git clone git://github.com/yyuu/pyenv.git ~/.pyenv
echo 'export PATH="$HOME/.pyenv/bin:$PATH"' >> .bashrc
echo 'eval "$(pyenv init -)"' >> .bashrc
pyenv install 2.7.10
#Install Pip
yum install python3-pip -y
#Install Node and NPM
curl -sL https://rpm.nodesource.com/setup_10.x | sudo bash -
sudo yum install nodejs -y
#Install React
curl --silent --location https://dl.yarnpkg.com/rpm/yarn.repo | sudo tee /etc/yum.repos.d/yarn.repo
sudo rpm --import https://dl.yarnpkg.com/rpm/pubkey.gpg
sudo yum install yarn -y
##ojo con lo que falta de react########
# Install SaltStack
sudo curl -L https://bootstrap.saltstack.com -o bootstrap_salt.sh
sudo sh bootstrap_salt.sh
#Put custom minion config in place (for enabling masterless mode)
sudo cp -r /srv/ds-exams/ConfigurationManagment/minion.d /etc/salt/
echo -e 'grains:\n roles:\n  - web' | sudo tee /etc/salt/minion.d/grains.conf
# Doing provision with saltstack
sudo salt-call state.apply
