FROM debian

RUN apt-get update && apt-get install -y \
    curl \
    gnupg2 \
    software-properties-common \
    unzip \
    apt-transport-https \
    ca-certificates \
    lsb-release \
    bash

RUN apt-get install -y openjdk-17-jdk

# Устанавливаем Maven
ENV MAVEN_VERSION=3.9.4
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm /tmp/maven.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

# Установка Docker CLI
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg \
    && echo \
    "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] \
    https://download.docker.com/linux/debian $(lsb_release -cs) stable" \
    > /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt-get install -y docker-ce-cli \
    && rm -rf /var/lib/apt/lists/*

# Проверка установок
RUN java -version && mvn -version && docker --version

RUN mkdir /.docker
RUN chmod 777 /.docker
RUN adduser admin
RUN groupadd -f docker && \
    usermod -aG docker admin

CMD ["bash"]
