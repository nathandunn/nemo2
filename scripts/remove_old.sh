#!/bin/bash

find /pgsql/backups/data -mtime +90 -exec rm -f {} \;
find /pgsql/backups/database -mtime +90 -exec rm -f {} \;

