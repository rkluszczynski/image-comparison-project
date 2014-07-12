package pl.info.rkluszczynski.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ScpPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('scp', type: ScpTask)
        project.extensions.create('scp', ScpPluginExtension)
    }
}
