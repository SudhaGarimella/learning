pipeline {
    agent any
	tools {
        maven 'maven_3_5_0' 
    }

    stages {
        stage ('Compile Stage') {

            steps {
                bat "mvn clean compile" 
        }
        }

        stage ('Testing Stage') {

           steps {
                bat "mvn test"
        }
        }


        stage ('Deployment Stage') {
             steps {
                bat "mvn deploy"
        }
        }
    }
}