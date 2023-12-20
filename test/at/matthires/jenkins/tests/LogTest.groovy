package at.matthires.jenkins.tests


import com.lesfurets.jenkins.unit.MethodCall
import org.junit.jupiter.api.Test

import java.util.function.Function

import static org.assertj.core.api.Assertions.assertThat

class LogTest extends BaseDeclarativePipelineTest {

    @Test
    void testLogInfo() {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        log.info('my message')
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()

        assertJobStatusSuccess()
        assertThat(helper.callStack)
                .filteredOn { c -> c.getMethodName() ==~ /echo/ }
                .map({ c -> c.argsToString() } as Function<MethodCall, ?>)
                .containsExactly("INFO: my message")
    }

    @Test
    void testLogWarning() {

        runInlineScript("""
                    pipeline {
                        stages {
                            stage('test') {
                                steps {
                                    script {
                                        log.warning('my message')
                                    }
                                } 
                            }
                        }
                    } """)

        printCallStack()

        assertJobStatusSuccess()
        assertThat(helper.callStack)
                .filteredOn { c -> c.getMethodName() ==~ /echo/ }
                .map({ c -> c.argsToString() } as Function<MethodCall, ?>)
                .containsExactly("WARNING: my message")
    }
}
