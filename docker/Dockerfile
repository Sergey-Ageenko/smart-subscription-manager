FROM eclipse-temurin:21-jdk-alpine AS builder

ARG MODULE

WORKDIR /build

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY auth-service/build.gradle auth-service/
COPY billing-service/build.gradle billing-service/
COPY core-service/build.gradle core-service/
COPY api-gateway/build.gradle api-gateway/
COPY config-server/build.gradle config-server/
COPY common/build.gradle common/
COPY event-schemas/build.gradle event-schemas/

RUN chmod +x gradlew

COPY . .

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :${MODULE}:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

ARG MODULE

WORKDIR /app

COPY --from=builder /build/${MODULE}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]