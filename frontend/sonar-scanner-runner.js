const scanner = require('sonarqube-scanner').default || require('sonarqube-scanner');

scanner(
    {
        serverUrl: process.env.SONAR_HOST_URL || 'http://localhost:9000',
        options: {
            'sonar.projectKey': 'devops-frontend',
            'sonar.projectName': 'devops-frontend',
            'sonar.sources': 'src',
            'sonar.inclusions': 'src/**/*.js',
            'sonar.test.inclusions': 'test/**/*.test.js',
            'sonar.javascript.lcov.reportPaths': 'coverage/lcov.info',
            'sonar.login': process.env.SONAR_AUTH_TOKEN || '',
        },
    },
    () => process.exit()
);
