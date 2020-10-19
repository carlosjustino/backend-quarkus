**Projeto do Justino**


## Banco de dados

docker run -it --name postgres-proj -e "POSTGRES_PASSWORD=projeto7" -p 5432:5432 -d postgres
psql -U postgres
create database projeto;


---


## Backend

acessar a pasta src/backend
executar
   ./mvnw compile quarkus:dev
   
   
   