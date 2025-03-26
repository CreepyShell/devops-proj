pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/CreepyShell/devops-proj.git'
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
                timeout(time: 30, unit: 'SECONDS') {
                    bat 'cd target && dir'
                    bat 'java -jar target/SwingApp-1.0-SNAPSHOT.jar'
                }
            }
        }

    }
}
