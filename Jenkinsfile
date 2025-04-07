pipeline {
    agent { label 'my-node' }
    stages {
        // stage('Checkout') {
        //     steps {
        //         git branch: 'main', url: 'https://github.com/CreepyShell/devops-proj.git'
        //     }
        // }
        // stage('Build') {
        //     steps {
        //         bat 'mvn clean package'
        //     }
        // }

        // stage('Run Tests') {
        //     steps {
        //         bat 'echo testing'
        //         bat 'mvn test'
        //     }
        // }

        // stage('SonarQube Analysis') {
        //     environment {
        //         SONAR_TOKEN = credentials('sonarqube-token')
        //     }
        //     steps {
        //         bat 'echo code quality'
        //         bat 'mvn sonar:sonar -Dsonar.projectKey=AirplaneProject -Dsonar.projectName="AirplaneProject" -Dsonar.token=%SONAR_TOKEN%'
        //     }
        // }
        // stage('Docker Containerization') {
        //     steps {
        //         script {
        //             withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        //                 bat """
        //                     echo %DOCKER_PASS%>pass.txt
        //                     docker login -u %DOCKER_USER% --password-stdin < pass.txt
        //                     del pass.txt    
        //                 """
        //                 bat 'docker rmi -f %DOCKER_USER%/devops-app:latest'
        //                 bat 'docker build -t %DOCKER_USER%/devops-app .'
        //                 bat 'docker push %DOCKER_USER%/devops-app:latest'
        //                 bat 'docker ps'
        //             }
        //         }
        //     }
        // }
        stage('Deploy to EC2') {
            steps {
                script {
                    step([
                        $class: 'BapSshPublisherPlugin',
                        publishers: [
                            [
                                configName: 'ec2-dev-ops',
                                transfers: [],
                                usePromotionTimestamp: false,
                                useWorkspaceInPromotion: false,
                                verbose: true,
                                execs: [
                                    [
                                        $class: 'BapSshExec',
                                        command: '''
                                            docker rm -f devops-app || true
                                            docker pull dockeruser1980/devops-app:latest
                                            docker run -d --name devops-app -p 9090:8080 --restart unless-stopped dockeruser1980/devops-app:latest
                                        '''
                                    ]
                                ]
                            ]
                        ]
                    ])
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
