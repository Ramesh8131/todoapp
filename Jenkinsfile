pipeline {
  agent any

  environment {
    DOCKER_CREDS = credentials('dockerhub-credentials') // only one credentials ID
  }

  stages {
    stage('Clone') {
      steps {
        git 'https://github.com/Ramesh8131/todoapp.git'
      }
    }

    stage('Build') {
      steps {
        bat 'mvn clean package'
      }
    }

    stage('Docker Build') {
      steps {
        bat 'docker build -t rameshkumar13111@gmail.com/myapp:1.0 .'
      }
    }

    stage('Docker Push') {
      steps {
        bat 'echo %DOCKER_CREDS_PSW% | docker login -u %DOCKER_CREDS_USR% --password-stdin'
        bat 'docker push rameshkumar13111@gmail.com/todoapp:1.0'
      }
    }

    stage('Run Locally') {
      steps {
        bat 'docker stop todoapp || exit 0'
        bat 'docker rm todoapp || exit 0'
        bat 'docker run -d -p 8081:8081 --name todoapp rameshkumar13111@gmail.com/todoapp:1.0'
      }
    }
  }
}
