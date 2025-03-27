pipeline {
    agent { label 'my-node' }
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
                    // bat 'java -jar target/SwingApp-1.0-SNAPSHOT.jar'
                }
            }
        }
        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonarqube-token')
            }
            steps {
                bat 'mvn sonar:sonar -Dsonar.projectKey=AirplaneProject -Dsonar.projectName="AirplaneProject" -Dsonar.token=sqa_52e2908219756112ba6d598d3317573615df8101'
            }
        }

    }
}
