Storyline - Schneller in die Cloud

Akt 1: Projektsetup

- Vorstellung der Protagonisten.
  Persona "Dev"
  Persona "Op"
- Vorstellung des Projekts
  (Du weißt schon, dass 2 von 3 Softwareprojekten scheitern? - Du meintest die, auf denen "Java Enterprise" steht?)
  (magisches Dreieck: Zeit und Budget sind fix, da können wir nur noch am Umfang/Qualität justieren)

  - Geschäftsidee: Outsourcing der Pizzalieferung. Microservices. REST API.
  (ggf. Benennung Product Owner aus dem Publikum)


Akt 2: Projektverlauf und 1. Go-Live

- Requirements vom PO
- MVP
- Operations-Failure?

Akt 3: Operations-Improvements

- Docker-Layer
  - `$ ./order/gradlew -p order buildDockerImage`
  - `$ docker history epizza/order:latest`
  - `$ ./order/gradlew -p order clean buildDockerImage`
  - `$ docker history epizza/order:latest`
- System Monitoring
  - actuator:
    - health check: `$ http 192.168.99.100:8082/system/health`
    - metrics: `$ http 192.168.99.100:8082/system/metrics`
- Centralized configuration management
  - config-server
- Log-Aggregation
  - logback.xml in config-repo
  - logging.config=http://${DOCKER_IP}:8888/all/default/dev/logback.xml in application.yml
  - SPRING_PROFILES_ACTIVE=logstash
- Distributed Tracing
- MySQL integration testing
