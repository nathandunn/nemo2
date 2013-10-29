#!/bin/bash


grails clean 
grails war 

WEBAPPS=/usr/share/tomcat7/webapps/
sudo service tomcat stop 
sudo rm -rf $WEBAPPS/ROOT* 
# have to be root . . .not just sudo service 
sudo cp target/nemo-0.1.war /usr/share/tomcat7/webapps/ROOT.war
sudo service tomcat start 


#sudo rm -rf $CA
#if [ -d "$WEBAPPS" ]; then
#    # Control will enter here if $DIRECTORY exists.
#    sudo cp target/nemo-0.1.war $WEBAPPS/staging.war
#fi





