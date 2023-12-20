package at.matthires.jenkins.tests

import org.junit.jupiter.api.Test

class RequireParamTest extends BaseDeclarativePipelineTest {

    private static final String JENKINSFILE = 'requireParams-jenkinsfile.groovy'

    @Test
    void testShouldReportError() {

        runScript(JENKINSFILE)

        assertJobStatusFailure()

        printCallStack()
    }

    @Test
    void testShouldSucceed() {
        binding.setVariable("params", ["MANDATORY_PARAM": 'false'])

        runScript(JENKINSFILE)

        assertJobStatusSuccess()

        printCallStack()
    }
}
