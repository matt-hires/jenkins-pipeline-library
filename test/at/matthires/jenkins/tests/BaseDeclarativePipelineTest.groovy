package at.matthires.jenkins.tests

import at.matthires.jenkins.tests.utils.TestConstants
import com.lesfurets.jenkins.unit.RegressionTest
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration
import com.lesfurets.jenkins.unit.global.lib.ProjectSource
import org.junit.jupiter.api.BeforeEach

/**
 * Integration test base class for Declarative Pipelines
 *
 * These tests can be used to verify if there is any regression in terms of the call stack.
 * (see https://github.com/jenkinsci/JenkinsPipelineUnit#compare-the-callstack-with-a-previous-implementation)
 *
 * To update the stored CallStack file in test/resources/callstacks/*.txt, the respective
 * test must be executed with <code>-Dpipeline.stack.write=true</code>.
 */
class BaseDeclarativePipelineTest extends DeclarativePipelineTest implements RegressionTest {

    BaseDeclarativePipelineTest() {
        this.callStackPath = TestConstants.CALLSTACK_PATH
    }

    @BeforeEach
    @Override
    void setUp() throws Exception {
        baseScriptRoot = ''
        scriptRoots = [TestConstants.SCRIPTS_ROOT]
        scriptExtension = 'groovy'
        super.setUp()

        def library = LibraryConfiguration.library().name(TestConstants.LIBRARY_NAME)
                .defaultVersion('default')
                .allowOverride(true)
                .implicit(true)
                .targetPath('.')
                .retriever(ProjectSource.projectSource())
                .build()
        helper.registerSharedLibrary(library)
    }

}
