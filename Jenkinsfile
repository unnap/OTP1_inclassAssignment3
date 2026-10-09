pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    environment {
        DOCKERHUB_REPO = 'tunnap/otp1'
        DOCKERHUB_CREDENTIALS_ID = 'dockerID'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Unit Tests') {
            steps {
                bat 'mvn -B clean verify'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/site/jacoco/**',
                    allowEmptyArchive: false
                )
            }
        }

        stage('Build Docker Image') {
            steps {
                bat '''
@echo off
docker build -t %DOCKERHUB_REPO%:%BUILD_NUMBER% .
docker tag %DOCKERHUB_REPO%:%BUILD_NUMBER% %DOCKERHUB_REPO%:latest
'''
            }
        }

        stage('Run Docker Image') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'mariadb-test-password',
                        variable: 'TEST_DB_PASSWORD'
                    )
                ]) {
                    script {

                        def result = bat(
                            returnStatus: true,
                            script: '''
@echo off
set "DB_PASSWORD=%TEST_DB_PASSWORD%"
set "DB_ROOT_PASSWORD=%TEST_DB_PASSWORD%"
set "DOCKER_IMAGE=%DOCKERHUB_REPO%:%BUILD_NUMBER%"

docker-compose -p otp1ci%BUILD_NUMBER% up -d --wait database

if errorlevel 1 goto cleanup

docker-compose -p otp1ci%BUILD_NUMBER% run --rm --no-deps app --check-db

:cleanup
set "RESULT=%ERRORLEVEL%"

docker-compose -p otp1ci%BUILD_NUMBER% down -v

exit /b %RESULT%
'''
                        )

                        if (result != 0) {
                            error(
                                'Docker database smoke test failed'
                            )
                        }
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {

                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        DOCKERHUB_CREDENTIALS_ID
                    ) {

                        docker.image(
                            "${DOCKERHUB_REPO}:${BUILD_NUMBER}"
                        ).push()

                        docker.image(
                            "${DOCKERHUB_REPO}:latest"
                        ).push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build, tests, Docker check and push succeeded.'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
    }
}