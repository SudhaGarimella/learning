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

        stage ('Build Stage') {

           steps {
                bat "mvn install"
        }
        }


      
    }
}