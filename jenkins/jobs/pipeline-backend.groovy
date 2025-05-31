pipelineJob('pipeline-backend') {
    triggers {
        scm('H/1 * * * *')
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('file:///repo')
                    }
                    branches('main', 'lab1', 'lab2', 'lab3', 'lab4')
                }
            }
            scriptPath('./backend/Jenkinsfile')
        }
    }
}
