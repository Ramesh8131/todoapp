pipeline {
  agent any

  environment {
    DOCKER_USER = credentials('dockerhub-user')
    DOCKER_PASS = credentials('dockerhub-pass')
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
        bat 'docker build -t yourdockerhub/todoapp:1.0 .'
      }
    }

    stage('Docker Push') {
      steps {
        bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
        bat 'docker push yourdockerhub/todoapp:1.0'
      }
    }

    stage('Run Locally') {
      steps {
        bat 'docker stop todoapp || exit 0'
        bat 'docker rm todoapp || exit 0'
        bat 'docker run -d -p 8081:8081 --name todoapp yourdockerhub/todoapp:1.0'
      }
    }
  }
}
