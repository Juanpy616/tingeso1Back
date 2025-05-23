pipeline {
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage('Build maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Juanpy616/tingeso1Back']])
                bat 'mvn clean package'
            }
        }
        stage('Unit Tests') {
            steps {
                // Run Maven 'test' phase. It compiles the test sources and runs the unit tests
                bat 'mvn test' // Use 'bat' for Windows agents or 'sh' for Unix/Linux agents
            }
        }
        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t juanpy616/tgs1back:latest .'
                }
            }
        }
        stage('Push image to Docker Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dockerhubpassword', variable: 'dockerpass')]) {
                        bat 'docker login -u juanpy616 -p %dockerpass%'
                    }
                }
                bat 'docker push juanpy616/tgs1back:latest'
            }
        }
    }
}