#!/bin/sh 

dropdb nemo_dev 
createdb nemo_dev 
psql -U ndunn nemo_dev < inferred_dump.sql ; 
psql -U ndunn nemo_dev < fix_security.sql ; 


