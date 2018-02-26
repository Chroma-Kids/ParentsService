# ParentsService [![Build Status](https://travis-ci.org/Chroma-Kids/ParentsService.svg?branch=master)](https://travis-ci.org/Chroma-Kids/ParentsService)

## Run the environment
Prerequisites:
- Docker
- Docker compose
- Create `.env` file with the following variables:
```
MYSQL_ROOT_PASSWORD
   
PARENTS_DB_HOST
PARENTS_DB_PORT
PARENTS_DB_NAME
PARENTS_DB_USER
PARENTS_DB_PASSWORD

PARENTS_SERVICE_PORT
```
To run it:
`mvn clean package dockerfile:build -DskipTests`
`docker-compose up`

To stop it:
`docker-compose stop`

To remove it:
`docker-compose rm`