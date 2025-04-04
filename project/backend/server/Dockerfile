# 使用 Ubuntu 作为基础镜像
FROM ubuntu:20.04

# 安装 openjdk-21
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    apt-get clean;

# 设置 JAVA_HOME 环境变量
ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# 验证 Java 安装
RUN java -version

# 设置容器中的工作目录
WORKDIR /app

# 将可执行的 JAR 文件复制到容器中
COPY target/server-1.0-SNAPSHOT.jar /app/app.jar

# 暴露端口 9977
EXPOSE 9977

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
