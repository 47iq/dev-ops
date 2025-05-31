FROM jenkins/jenkins:lts

USER root

# Установка Docker CLI
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Создание группы docker и добавление пользователя jenkins в неё
RUN groupadd -f docker && \
    usermod -aG docker jenkins

RUN apt-get update && apt-get install -y openjdk-17-jdk

# Устанавливаем Maven
ENV MAVEN_VERSION=3.9.4
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm /tmp/maven.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

RUN apt-get update && apt-get install -y npm

RUN jenkins-plugin-cli --plugins \
    configuration-as-code \
    workflow-aggregator \
    git \
    docker-workflow \
    github \
    github-branch-source \
    job-dsl

RUN git config --global --add safe.directory /repo/.git

USER jenkins
