#!/usr/local/env bash

. env.sh

heroku  config:set MONGODB_USER=${MONGODB_USER} --app ucsb-cs56-pconrad-08-28-18
heroku  config:set MONGODB_PASS=${MONGODB_PASS}	--app ucsb-cs56-pconrad-08-28-18
heroku  config:set MONGODB_NAME=${MONGODB_NAME}	--app ucsb-cs56-pconrad-08-28-18
heroku  config:set MONGODB_HOST=${MONGODB_HOST}	--app ucsb-cs56-pconrad-08-28-18
heroku  config:set MONGODB_PORT=${MONGODB_PORT}	--app ucsb-cs56-pconrad-08-28-18
