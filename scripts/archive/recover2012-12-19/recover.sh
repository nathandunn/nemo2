#!/bin/sh 

dropdb nemo2_dev 
createdb nemo2_dev 
dropdb nemo2_staging 
createdb nemo2_staging 
dropdb nemo2_test
createdb nemo2_test
unzip -f dump.zip 
psql -U ndunn nemo2_dev < dump.sql ; 
psql -U ndunn nemo2_dev < fixes.sql ; 
psql -U ndunn nemo2_test < dump.sql ; 
psql -U ndunn nemo2_test < fixes.sql ; 
psql -U ndunn nemo2_staging < dump.sql ; 
psql -U ndunn nemo2_staging < fixes.sql ; 
#psql -U ndunn nemo_dev < fix_security.sql ; 


