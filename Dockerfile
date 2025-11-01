FROM amazoncorretto:21

COPY build/libs/app.jar /usr/app/

WORKDIR /usr/app

ENTRYPOINT ["java","-jar","app.jar"]
