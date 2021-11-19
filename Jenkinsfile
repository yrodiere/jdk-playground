pipeline {
    agent none
    stages {
        stage('Build and test') {
            matrix {
                agent {
                    label 'Fedora'
                }
                axes {
                    axis {
                        name 'JDK_VERSION'
                        values '8', '11', '17', '18'
                    }
                }
				tools {
					maven 'Apache Maven 3.8'
					jdk "OpenJDK ${JDK_VERSION} Latest"
				}
                stages {
                    stage('Build and test') {
                        steps {
                            sh """ \
                                mvn clean install
                            """
                        }
                    }
                }
            }
        }
    }
}