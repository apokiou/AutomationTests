pipeline {
    agent any

    tools {
        // These names must match tool installations configured in Jenkins
        // (Manage Jenkins > Tools). Adjust if your instance uses different labels.
        jdk 'jdk-17'
        maven 'maven-3.9'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B clean test'
            }
        }
    }

    post {
        always {
            // JUnit-style results produced by Surefire.
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

            // Allure report (requires the Allure Jenkins plugin).
            allure includeProperties: false,
                   jdk: '',
                   results: [[path: 'target/allure-results']]

            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
        }
    }
}
