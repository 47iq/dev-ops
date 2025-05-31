pipelineJob('pipeline-frontend') {
    triggers {
        scm('H/1 * * * *')
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/47iq/dev-ops.git')
                    }
                    branches('main', 'lab1', 'lab2', 'lab3', 'lab4')
                }
            }
            scriptPath('./frontend/Jenkinsfile')
        }
    }
}
