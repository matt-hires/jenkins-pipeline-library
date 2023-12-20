package at.matthires.jenkins.tests


import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class VersionTest extends BaseDeclarativePipelineTest {

    @ParameterizedTest
    @CsvSource(value = ['1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT',
                        '1.1.1-SNAPSHOT,1.1.2',
                        '1.2.1,1.2.2-SNAPSHOT',
                        '1,1.0.1',
                        '1.0,1.0.1',
                        '1.1,1.1.1',
                        '1.2.2,1.2.3'
    ])
    void testIsPatchVersionUpgrade(String versionA, String versionB) {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(!version.isPatchVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be a patch version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }

    @ParameterizedTest
    @CsvSource(value = [
            '1.0.0-SNAPSHOT, 1.1.0-SNAPSHOT',
            '1.0.0-SNAPSHOT, 2.0.0-SNAPSHOT',
            '1.1.1-SNAPSHOT,1.2.0',
            '1.2.1,1.0.1-SNAPSHOT',
            '2.0.0,1.0.0-SNAPSHOT',
            '3.0.0,2.1.0',
            '1,2',
            '1.0,2.1',
            '20,11',
    ])
    void testNoPatchVersionUpgrade(String versionA, String versionB) {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(version.isPatchVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be no patch version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }

    @ParameterizedTest
    @CsvSource(value = ['1.0.0-SNAPSHOT, 1.1.0-SNAPSHOT',
            '1.1.1-SNAPSHOT,1.2.0',
            '1.2.1,1.3.2-SNAPSHOT',
            '1,1.1',
            '1.0,1.1',
            '1.1,1.2.1',
            '1.2.3,1.3'
    ])
    void testIsMinorVersionUpgrade(String versionA, String versionB) {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(!version.isMinorVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be no minor version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }

    @ParameterizedTest
    @CsvSource(value = ['1.0.0-SNAPSHOT, 1.0.1-SNAPSHOT',
            '1.0.0-SNAPSHOT,2.1.0',
            '1.2.1,1.1.2-SNAPSHOT',
            '1,1.0.1',
            '1.0,1.0.1',
            '1,2.0.0',
            '1.2.3,2.2.3'
    ])
    void testNoMinorVersionUpgrade(String versionA, String versionB) {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(version.isMinorVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be no minor version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }

    @ParameterizedTest
    @CsvSource(value = ['1.0.0-SNAPSHOT, 2.0.0-SNAPSHOT',
            '1.0.0-SNAPSHOT,2.1.0',
            '1.2.1,2.2.2-SNAPSHOT',
            '1,2.0.1',
            '1.0,2.0.1',
            '53,54',
            '1.2.3,2.3.3'
    ])
    void testMajorVersionUpgrade(String versionA, String versionB) {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(!version.isMajorVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be major version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }

    @ParameterizedTest
    @CsvSource(value = ['1.0.0-SNAPSHOT, 1.0.0-SNAPSHOT',
            '1.0.0-SNAPSHOT,1.1.0',
            '1.2.1,1.2.2-SNAPSHOT',
            '1,1.0.1',
            '1.0,1.0.1',
            '54,54-SNAPSHOT',
            '2.2.3,1.3.3'
    ])
    void testNoMajorVersionUpgrade(String versionA, String versionB) {
        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        if(version.isMajorVersionUpgrade('$versionA', '$versionB')) {
                                            error('Should be no major version upgrade')
                                        }
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()
        assertJobStatusSuccess()
    }
}
