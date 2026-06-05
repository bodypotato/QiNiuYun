# ─── 阶段一：编译 ───
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .
# 使用 example 模板创建 application.yaml（密钥通过环境变量注入）
RUN cp src/main/resources/application-example.yaml src/main/resources/application.yaml
RUN ./gradlew bootJar --no-daemon

# ─── 阶段二：运行 ───
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
