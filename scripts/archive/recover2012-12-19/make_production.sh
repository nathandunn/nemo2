#!/bin/sh 

#./recover.sh 

dropdb nemo2_production ; 
createdb nemo2_production ; 

psql -U ndunn nemo2_production < dump.sql ; 
psql -U ndunn nemo2_production < fixes.sql ; 

#echo 'create database nemo2_production with template nemo2_dev owner ndunn ; ' | psql -Undunn  nemo2_dev ;


#psql -U ndunn nemo2_dev < dump.sql ; 
#psql -U ndunn nemo2_dev < fixes.sql ; 
#psql -U ndunn nemo2_test < dump.sql ; 
#psql -U ndunn nemo2_test < fixes.sql ; 
#psql -U ndunn nemo_dev < fix_security.sql ; 


