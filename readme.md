






docker  compose file :
docker run --name techapp -p 8080:8080 --rm --net=work_nappnet --link mysql-cont techtest




docker compose content:
version: '3.3'
services:
  db:
    image: mysql
    container_name: mysql-cont
    restart: always
    environment:
      MYSQL_DATABASE: 'testdb' 
      MYSQL_USER: 'user' 
      MYSQL_PASSWORD: '1234' 
      MYSQL_ROOT_PASSWORD: 'r1234'
    ports: 
      - '3307:3306'
    expose: 
      - '3307' 
    volumes:
      - mysqldocker:/var/lib/mysql
    networks:
      - nappnet      
volumes:
  mysqldocker:

networks:
  nappnet: 
    driver: bridge