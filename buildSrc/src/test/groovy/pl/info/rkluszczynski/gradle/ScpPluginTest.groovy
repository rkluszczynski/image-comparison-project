package pl.info.rkluszczynski.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.assertTrue

@Ignore
class ScpPluginTest {
    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: pl.info.rkluszczynski.gradle.ScpPlugin

        assertTrue(project.tasks.scp instanceof ScpTask)
    }
}
