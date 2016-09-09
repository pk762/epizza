# inspired by https://spring.io/guides/gs/spring-boot-docker/
FROM jensfischerhh/docker-java:8u45-jre

ENTRYPOINT ["java","-cp","/app","org.springframework.boot.loader.JarLauncher"]

# dependencies
ADD app/BOOT-INF/lib /app/BOOT-INF/lib
# spring boot loader
ADD app/org /app/org
# meta-inf
ADD app/META-INF /app/META-INF
# our application
ADD app/BOOT-INF/classes /app/BOOT-INF/classes