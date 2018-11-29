package com.bluelinelabs.conductor.lint;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;

public class ControllerDetectorTest {

    private static final String CONSTRUCTOR_ERROR =
            "src/test/SampleController.java:2: Error: This Controller needs to have either a public default constructor or a public single-argument constructor that takes a Bundle. (test.SampleController) [ValidController]\n"
            + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
            + "^\n"
            + "1 errors, 0 warnings\n";
    private static final String CLASS_ERROR =
            "src/test/SampleController.java:2: Error: This Controller class should be public (test.SampleController) [ValidController]\n"
                    + "private class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                    + "^\n"
                    + "1 errors, 0 warnings\n";

    @Test
    public void testWithNoConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithEmptyConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "    public SampleController() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithInvalidConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "    public SampleController(int number) { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(CONSTRUCTOR_ERROR);
    }

    @Test
    public void testWithEmptyAndInvalidConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "    public SampleController() { }\n"
                + "    public SampleController(int number) { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithPrivateConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "    private SampleController() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(CONSTRUCTOR_ERROR);
    }

    @Test
    public void testWithPrivateClass() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "private class SampleController extends com.bluelinelabs.conductor.Controller {\n"
                + "    public SampleController() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(CLASS_ERROR);
    }

}
