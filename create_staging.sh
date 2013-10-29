#!/bin/bash

sudo service tomcat7 stop ; 
source ~/.bashrc
cd scripts/last_dump ; 
./recover_staging.sh 
cd ../.. ;
grails clean 
grails -Dgrails.env=staging war 
sudo cp target/nemo-0.1.war /var/lib/tomcat7/webapps/nemo.war
sudo service tomcat7 start ; 
#WEBAPPS=/usr/share/tomcat7/webapps/
#sudo rm -rf $CA
#if [ -d "$WEBAPPS" ]; then
#    # Control will enter here if $DIRECTORY exists.
#    sudo cp target/nemo-0.1.war $WEBAPPS/staging.war
#fi





