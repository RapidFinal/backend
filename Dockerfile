FROM maven:3-jdk-8
EXPOSE 8080
WORKDIR /webapp
ARG JAR_FILE
ADD pom.xml .
RUN mvn clean process-resources
RUN mvn -U dependency:resolve-plugins dependency:go-offline
ADD src .
RUN mvn package -Dmaven.test.skip=true
CMD java -jar target/${JAR_FILE}