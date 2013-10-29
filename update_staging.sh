#!/bin/bash

source ~/.bashrc
grails clean 
grails -Dgrails.env=staging war 
WEBAPPS=/var/lib/tomcat7/webapps/
sudo service tomcat7 stop 
sudo rm -fr $WEBAPPS/nemo*
sudo cp target/nemo-0.1.war /var/lib/tomcat7/webapps/nemo.war
sudo service tomcat7 start 
#WEBAPPS=/usr/share/tomcat7/webapps/
#sudo rm -rf $CA
#if [ -d "$WEBAPPS" ]; then
#    # Control will enter here if $DIRECTORY exists.
#    sudo cp target/nemo-0.1.war $WEBAPPS/staging.war
#fi





