#!/bin/bash
docker compose down --remove-orphans
# shellcheck disable=SC2164
cd minio
docker compose down --remove-orphans
# shellcheck disable=SC2164
cd ../release-management-service
docker compose down --remove-orphans


app=( developer-console-ui dco-gateway scenario-library-service tracks-management-service postgres dpage/pgadmin4 minio release-management-service)
image=( developer-console-ui:1.0 dco-gateway:1.0 scenario-library-service:1.0 tracks-management-service:1.0 postgres:1.0 dpage/pgadmin4:latest minio:1.0 release-management-service:1.0)

arraylength=${#app[@]}

for (( i=0; i<${arraylength}; i++ ));
do
  if [ "$( docker images | grep "${app[i]}" | wc -l )" -gt 0 ]; then
    docker rmi ${image[i]}
    printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ Deleted '${image[i]}' image from local  ~ ~ ~ ~ ~ \033[0m\n'
  else
    printf -- '\033[31m \x1b[1m ~ ~ ~ ~ ~ No '${image[i]}' image available on local ~ ~ ~ ~ ~ \033[0m\n'
  fi
done
