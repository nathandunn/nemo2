#!/bin/sh 

dropdb nemo_dev 
createdb nemo_dev 
psql -U ndunn nemo_dev < instance_snapshot_reverse.sql ; 


