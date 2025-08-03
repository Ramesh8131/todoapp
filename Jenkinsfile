pipeline {
  agent any

  tools {
    jdk 'JDK17'
    maven 'Maven 3.8.6'
  }

  // environment {
  //   DOCKER_CREDS = credentials('dockerhub-credentials')
  // }

  stages {
    stage('Clone') {
      steps {
        git 'https://github.com/Ramesh8131/todoapp.git'
      }
    }

    stage('Build') {
      steps {
        bat 'mvn clean package -DskipTests'
      }
    }
    stage('Start Containers') {
      steps {
        bat 'docker-compose up --build'
      }
    }

    // stage('Docker Build') {
    //   steps {
    //     bat 'docker build -t %DOCKER_CREDS_USR%/todoapp:1.0 .'
    //   }
    // }

    // stage('Docker Push') {
    //   steps {
    //     bat 'echo %DOCKER_CREDS_PSW% | docker login -u %DOCKER_CREDS_USR% --password-stdin'
    //     bat 'docker push %DOCKER_CREDS_USR%/todoapp:1.0'
    //   }
    // }

       stage('Docker Login and Push') {
          steps {
              withCredentials([usernamePassword(credentialsId: 'DOCKER_CREDS', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                  bat '''
                      echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                      docker push %DOCKER_USERNAME%/todoapp:latest
                  '''
              }
          }
        }

    // stage('Run Locally') {
    //   steps {
    //     bat 'docker stop todoapp || exit 0'
    //     bat 'docker rm todoapp || exit 0'
    //     bat 'docker run -d -p 8081:8081 --name todoapp %DOCKER_CREDS_USR%/todoapp:1.0'
    //   }
    //}
  }
}
