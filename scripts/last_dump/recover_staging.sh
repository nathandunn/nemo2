#!/bin/sh 

if [ $# -eq 1 ]; then 
DUMP_NAME=$1
echo "Using name $DUMP_NAME"
else
DUMP_NAME=`ls -rtc dump*.zip | tail -1`
echo "using dump_name $DUMP_NAME"
fi

dropdb nemo2_staging 
createdb nemo2_staging 
rm -f dump.sql 
unzip -p $DUMP_NAME > dump.sql 
psql -U ndunn nemo2_staging < dump.sql ; 
#psql -U ndunn nemo2_staging < fixes.sql ; 
#psql -U ndunn nemo_dev < fix_security.sql ; 


