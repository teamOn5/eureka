FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew bootJar -x test

EXPOSE 8000

CMD ["java", "-jar", "build/libs/gateway-custom.jar"]