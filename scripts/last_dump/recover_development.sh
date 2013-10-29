#!/bin/sh 

if [ $# -eq 1 ]; then 
DUMP_NAME=$1
echo "Using name $DUMP_NAME"
else
DUMP_NAME=`ls -rtc dump*.zip | tail -1`
echo "using dump_name $DUMP_NAME"
fi

dropdb nemo2_dev 
createdb nemo2_dev 
rm -f dump.sql 
unzip -p $DUMP_NAME > dump.sql 
psql -U ndunn nemo2_dev < dump.sql ; 
#psql -U ndunn nemo2_dev < fixes.sql ; 
#psql -U ndunn nemo_dev < fix_security.sql ; 


