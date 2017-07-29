# donation-backend


Docker commands:
https://docs.docker.com/engine/swarm/swarm-tutorial/

docker exec -i -t mysql /bin/bash
docker run --name mysql2 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=donation  -p 33062:3306 -d mysql:latest