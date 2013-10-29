#!/bin/sh 

dropdb nemo_dev 
createdb nemo_dev 
psql -U ndunn nemo_dev < pro_dump_trimmed.sql ; 
psql -U ndunn nemo_dev < update_schema_script.sql ; 


