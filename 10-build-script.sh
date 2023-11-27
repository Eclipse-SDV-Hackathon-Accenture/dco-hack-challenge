#!/bin/bash -e

banner_head()
{
  echo "+------------------------------------------+"
  printf "| %-40s |\n" "`date`"
  echo "|                                          |"
  printf "\033[35m|`tput bold``tput rev` %+30s           `tput sgr0`\033[0m|\n" "$@"
  echo "+------------------------------------------+"
}

banner_head Developer-Console

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ Installing all dependencies/library ~ ~ ~ ~ ~ \033[0m\n'
yarn --cwd ./developer-console-ui/app install
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ Installation is completed ~ ~ ~ ~ ~ \033[0m\n\n'

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ dco-gateway build start ~ ~ ~ ~ ~ \033[0m\n'
yarn --cwd ./developer-console-ui/app build
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ developer-console-ui build successful ~ ~ ~ ~ ~ \033[0m\n\n'

sleep 3

docker compose up -d postgres
docker compose -f release-management-service/docker-compose.yml up -d redis

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ release-management-service build start ~ ~ ~ ~ ~ \033[0m\n'
mvn clean verify  \
--batch-mode \
--file release-management-service/pom.xml \
--settings release-management-service/settings.xml
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ release-management-service build successful ~ ~ ~ ~ ~ \033[0m\n\n'

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ dco-gateway build start ~ ~ ~ ~ ~ \033[0m\n'
mvn clean verify \
--batch-mode \
--file dco-gateway/pom.xml \
--settings dco-gateway/settings.xml
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ dco-gateway build successful ~ ~ ~ ~ ~ \033[0m\n\n'

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ scenario-library-service build start ~ ~ ~ ~ ~ \033[0m\n'
mvn clean verify \
--batch-mode \
--file scenario-library-service/pom.xml \
--settings scenario-library-service/settings.xml
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ scenario-library-service build successful~ ~ ~ ~ ~ \033[0m\n\n'

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ tracks-management-service build start ~ ~ ~ ~ ~ \033[0m\n'
mvn clean verify \
--batch-mode \
--file tracks-management-service/pom.xml \
--settings tracks-management-service/settings.xml
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ tracks-management-service build successful ~ ~ ~ ~ ~ \033[0m\n\n\n'

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ sim-runner build start ~ ~ ~ ~ ~ \033[0m\n'
mvn clean verify \
--file sumo/pom.xml
printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ sim-runner build successful ~ ~ ~ ~ ~ \033[0m\n\n'

sleep 3

printf -- '\033[35m \x1b[1m ~ ~ ~ ~ ~ Start Minio ~ ~ ~ ~ ~ \033[0m\n'
[ -e minio/minio_keys.env ] && rm -rf minio/minio_keys.env

touch minio/minio_keys.env

docker compose up -d minio
docker compose ls
docker compose ps
if [ $( docker ps -a | grep minio | wc -l ) -gt 0 ]; then
  printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ minio is running ~ ~ ~ ~ ~ \033[0m\n\n'
  printf -- '\033[32m \x1b[1m You can access minio s3 using http://localhost:9001 url. 
  The admin user is "minioadmin" and default password is "minioadmin"  \033[0m\n\n'

  tput rev
  printf -- '\033[35m \x1b[1m Create 'dco-scenario-library-service' bucket and Access key, Secret Key in minio and run deploy script as per the documentation. \033[0m\n\n'
  tput sgr0
else
  printf -- '\033[31m \x1b[1m ~ ~ ~ ~ ~ minio container is missing ~ ~ ~ ~ ~ \033[0m\n'
fi

printf -- '\033[32m \x1b[1m For deployment, continue from step 3 from Build and Deploy section once you are done with Minio configuration  \033[0m\n\n'



 
