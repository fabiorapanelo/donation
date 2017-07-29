FROM openjdk:8
EXPOSE 8081
ADD target/*.jar /donation-backend.jar
ADD wait-for-it.sh /wait-for-it.sh
RUN bash -c 'chmod 777 /wait-for-it.sh'
CMD ["java","-Dspring.profiles.active=container","-jar","/donation-backend.jar"]