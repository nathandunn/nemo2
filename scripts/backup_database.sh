#!/bin/bash

DUMPFILE=dump`date +%F`
DUMPDIRECTORY=/home/users/ndunn/DATABASE_BACKUPS

cd $DUMPDIRECTORY
pg_dump nemo2_production > $DUMPFILE.sql 
zip $DUMPFILE.zip $DUMPFILE.sql 
rm -f $DUMPFILE.sql 

