#!/bin/bash
while ! exec 6<>/dev/tcp/mysql/${PARENTS_DB_PORT}; do
    echo "Trying to connect to MySQL at ${PARENTS_DB_PORT}..."
    sleep 10
done

java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app.jar