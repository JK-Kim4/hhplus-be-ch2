FROM gradle:8.3-jdk17 AS builder

# 전체 프로젝트 복사
COPY .. /app
WORKDIR /app

# gradlew에 권한 부여 (이거 빠지면 진짜 많이 실패)
RUN chmod +x ./gradlew

# 중간 쉘 삽입 (여기서 빌드 확인 가능)
RUN ./gradlew bootJar -x test --no-daemon --stacktrace --info

FROM eclipse-temurin:17-jre
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
