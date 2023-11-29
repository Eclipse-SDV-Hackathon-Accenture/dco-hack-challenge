#!/bin/bash -e
help()
{
   # Display Help
   echo 
   printf -- '\033[31m \x1b[1m ~ ~ ~ ~ ~ Wrong Syntax ~ ~ ~ ~ ~ \033[0m\n'
   echo "Argument access-key or secrete-key is missing."
   echo
   printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ Help ~ ~ ~ ~ ~ \033[0m\n'
   echo "Syntax:"
   echo "sh 20-deploy-script.sh <access-key> <secrete-key> "
   echo
   echo "Description:"
   echo "access-key     Minio Access Key."
   echo "secrete-key    Minio Secret Key."
   echo
}

if [ -z "$1" ]
  then
    help
    exit 1;
fi

if [ -z "$2" ]
  then
    help
    exit 1;
fi

echo "APP_STORAGE_ACCESS_KEY: $1" >> minio/minio_keys.env
echo "APP_STORAGE_SECRET_KEY: $2" >> minio/minio_keys.env

cat minio/minio_keys.env

docker compose -f release-management-service/docker-compose.yml up -d
docker compose up -d
docker compose ls
docker compose ps
mvn --file ./release-management-service/app-database/pom.xml spring-boot:run

app=( postgres pgadmin developer-console-ui dco-gateway scenario-library-service tracks-management-service release-management-service)

arraylength=${#app[@]}

for (( i=0; i<${arraylength}; i++ ));
do
  if [ $( docker ps -a | grep ${app[i]} | wc -l ) -gt 0 ]; then
    printf -- '\033[32m \x1b[1m ~ ~ ~ ~ ~ '${app[i]}' is running ~ ~ ~ ~ ~ \033[0m\n\n\n'
  else
    printf -- '\033[31m \x1b[1m ~ ~ ~ ~ ~ '${app[i]}' container is missing ~ ~ ~ ~ ~ \033[0m\n\n\n'
  fi
done

printf -- '\033[32m \x1b[1m Below are important urls that you can use to access application, swagger and playground for REST APIs  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> Developer Console UI: http://localhost:3000  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> dco-gatway playground for REST APIs: http://localhost:8080/playground  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> release-management-service swagger for REST APIs: http://localhost:8083/openapi/swagger-ui/index.html  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> tracks-management-service swagger for REST APIs: http://localhost:8081/openapi/swagger-ui/index.html  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> scenario-library-service swagger for REST APIs: http://localhost:8082/openapi/swagger-ui/index.html  \033[0m\n\n'
printf -- '\033[32m \x1b[1m  -> pgadmin client for postgresql database: http://localhost:5050. The username is "admin@default.com" and password is "admin"  \033[0m\n\n'
printf -- '\033[32m \x1b[1m For more details, please refer the README_DCO.md file \033[0m\n\n'

#mvn --file ./release-management-service/app/bom.xml spring-boot:run > /dev/null 2>&1
