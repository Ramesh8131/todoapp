pipeline {
    agent any

    tools {
        maven 'Maven 3.8.6'  // Jenkins में Maven configure होना चाहिए
        jdk 'JDK 21'         // Jenkins में JDK configure होना चाहिए
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Ramesh8131/todoapp.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                bat 'java -jar target/*.jar'
            }
        }
    }
}
