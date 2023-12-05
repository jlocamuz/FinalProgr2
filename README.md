# generadorOrdenes

### first docker-compose.yml --> sin base de datos

```
version: '3.8'

services:
  jhipster:
    image: jhipster/jhipster
    container_name: jhipster
    ports:
      - "8080:8080"
      - "9000:9000"
      - "3001:3001"
    volumes:
      - ~/jhipster:/home/jhipster/app
      - ~/.m2:/home/jhipster/.m2
    restart: always
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_DEVTOOLS_ADDITIONAL_OPTIONS=--spring-boot.run.fork=false # Disable fork for DevTools
    tty: true
```
