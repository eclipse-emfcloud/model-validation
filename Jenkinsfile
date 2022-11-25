pipeline {
    agent any

    tools {
        maven 'apache-maven-latest'
        jdk 'openjdk-jdk11-latest'
    }

    environment {
        EMAIL_TO = "sgraband+eclipseci@eclipsesource.com, ndoschek+eclipseci@eclipsesource.com, eneufeld+eclipseci@eclipsesource.com"
    }

    stages {
        stage ('Build: Plain Maven (M2)') {
            steps {
                sh 'mvn clean verify -Pm2 -B'
            }
        }
        
        stage ('Build: Eclipse-based (P2)') {
            steps {
                sh 'mvn clean verify -Pp2 -B'
            }
        }

        stage('Deploy') {
            when { branch 'master' }
            steps {
            	parallel(
            	    p2: {
            	        build job: 'deploy-emfcloud-model-validation-p2', wait: false
            	    },
            	    m2: {
            	        build job: 'deploy-emfcloud-model-validation-m2', wait: false
            	    }
            	)
            }
        }
    }

    post {
        failure {
            script {
                if (env.BRANCH_NAME == 'master') {
                    echo "Build result FAILURE: Send email notification to ${EMAIL_TO}"
                    emailext attachLog: true,
                    body: 'Job: ${JOB_NAME}<br>Build Number: ${BUILD_NUMBER}<br>Build URL: ${BUILD_URL}',
                    mimeType: 'text/html', subject: 'Build ${JOB_NAME} (#${BUILD_NUMBER}) FAILURE', to: "${EMAIL_TO}"
                }
            }
        }
        unstable {
            script {
                if (env.BRANCH_NAME == 'master') {
                    echo "Build result UNSTABLE: Send email notification to ${EMAIL_TO}"
                    emailext attachLog: true,
                    body: 'Job: ${JOB_NAME}<br>Build Number: ${BUILD_NUMBER}<br>Build URL: ${BUILD_URL}',
                    mimeType: 'text/html', subject: 'Build ${JOB_NAME} (#${BUILD_NUMBER}) UNSTABLE', to: "${EMAIL_TO}"
                }
            }
        }
        fixed {
            script {
                if (env.BRANCH_NAME == 'master') {
                    echo "Build back to normal: Send email notification to ${EMAIL_TO}"
                    emailext attachLog: false,
                    body: 'Job: ${JOB_NAME}<br>Build Number: ${BUILD_NUMBER}<br>Build URL: ${BUILD_URL}',
                    mimeType: 'text/html', subject: 'Build ${JOB_NAME} back to normal (#${BUILD_NUMBER})', to: "${EMAIL_TO}"
                }
            }
        }
    }
}