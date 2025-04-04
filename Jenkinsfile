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

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonarqube-token')
            }
            steps {
                bat 'mvn sonar:sonar -Dsonar.projectKey=AirplaneProject -Dsonar.projectName="AirplaneProject" -Dsonar.token=%SONAR_TOKEN%'
            }
        }
        stage('Deploy Application') {
            steps {
                timeout(time: 30, unit: 'SECONDS') {
                    bat 'cd target && dir'
                    // bat 'java -jar target/SwingApp-1.0-SNAPSHOT.jar 8089sdf'
                }
            }
        }
    }
    post {
        failure {
            emailext (
                subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "The build has failed.\nCheck logs here: ${env.BUILD_URL}",
                to: "danil.t404@gmail.com"
            )
        }
        success {
            emailext (
                subject: "Build Successful: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "The build completed successfully",
                to: "danil.t404@gmail.com"
            )
        }
    }

}
