package pl.info.rkluszczynski.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Test

import static org.junit.Assert.assertTrue

@Ignore
class ScpTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('scp', type: ScpTask)
        assertTrue(task instanceof ScpTask)
    }
}
