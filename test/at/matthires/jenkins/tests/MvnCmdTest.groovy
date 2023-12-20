package at.matthires.jenkins.tests


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MvnCmdTest extends BaseDeclarativePipelineTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp()

        helper.registerAllowedMethod('artifactsPublisher', [LinkedHashMap])
        helper.registerAllowedMethod('junitPublisher', [LinkedHashMap])
        helper.registerAllowedMethod('findbugsPublisher', [LinkedHashMap])
        helper.registerAllowedMethod('jacocoPublisher', [LinkedHashMap])
        helper.registerAllowedMethod('openTasksPublisher', [LinkedHashMap])
        helper.registerAllowedMethod('jgivenPublisher', [LinkedHashMap])

        binding.setVariable('JAVA_TOOL_OPTIONS', '')
    }

    @Test
    void testCallWithStringParam() {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        mvnCmd('clean install')
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()

        testNonRegression('testCallWithStringParam')
    }

    @Test
    void testCallWithMapParams() {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        mvnCmd(cmdLine: 'clean install')
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()

        testNonRegression('testCallWithMapParam')
    }

    @Test
    void testCallWithMapParamsDisablePublishers() {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        mvnCmd(
                                            cmdLine: 'clean install',
                                            disablePublishers: true,
                                            returnStatus: true,
                                            returnStdout: false
                                        )
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()

        testNonRegression('testCallWithMapParamsDisablePublishers')
    }

    @Test
    void testGetLatestArtifactReleaseVersion() {

        helper.registerAllowedMethod('readJSON', [LinkedHashMap]) {
            return ['items': [
                    ['maven2': ['version': '1.0.0']]
            ]]
        }

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        def version = mvnCmd.getLatestArtifactReleaseVersion(
                                            nexusUrl: 'https://nexus.host.at', 
                                            groupId: 'myGroupId', 
                                            artifactId: 'myArtifactId'
                                        )
                                        
                                        if (version != '1.0.0') {
                                            error('Version not set correctly')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()

        testNonRegression('testGetLatestArtifactReleaseVersion')
    }

    @Test
    void testReadProperty() {

        helper.addShMock('my-artifact-id', 0)

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        def value = mvnCmd.readProperty('project.artifactId')
                                        
                                        if (value != 'my-artifact-id') {
                                            error("artifactId not set correctly, was: '\${value}'")
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()

        testNonRegression('testReadProperty')
    }
}
