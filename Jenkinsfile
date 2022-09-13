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
                        values '17', '20', '21'
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