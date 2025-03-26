pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CreepyShell/devops-com.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Run Application') {
            steps {
                bat 'cd target'
                bat 'java -jar SwingApp-1.0-SNAPSHOT.jar'
            }
        }
    }
}
