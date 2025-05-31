FROM jenkins/jenkins:lts

RUN jenkins-plugin-cli --plugins \
    configuration-as-code \
    workflow-aggregator \
    git \
    docker-workflow \
    github \
    github-branch-source \
    job-dsl

RUN git config --global --add safe.directory /repo/.git
