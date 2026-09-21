pipeline {
    agent any
    tools {
    maven "Maven3"
    }

    environment{
    PATH = "C:\\Users\\stres\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
    DOCKERHUB_CREDENTIALS_ID = "dockerID"
    DOCKERHUB_REPO = "tunnap/otp1"
    DOCKER_IMAGE_TAG = "latest"
    }

    stages {
        stage("check"){
            steps{
                git "https://github.com/unnap/OTP1_inclassAssignment3"
            }
        }
        stage("build"){
            steps{
                bat "mvn clean install"
            }
        }
        stage("test"){
            steps{
                bat "mvn test"
            }
        }
        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
                }
            }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        stage("jacoco"){
            steps{
                jacoco()
            }
        }
    stage("Build docker image"){
        steps{
            script{
                docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
            }
        }
     }

    stage("Push docker image to docker hub"){
        steps{
            script{
                docker.withRegistry("https://index.docker.io/v1/",DOCKERHUB_CREDENTIALS_ID){
                docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                }
            }
        }
    }

  }
}