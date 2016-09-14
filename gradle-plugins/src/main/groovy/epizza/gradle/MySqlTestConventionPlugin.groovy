package epizza.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;

class MySqlTestConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply(plugin: FlywayConventionPlugin)
        addLocalMySqlIntTestTasks(project)
    }

    private addLocalMySqlIntTestTasks(Project project) {
        def Task intTestTask = project.tasks.findByPath('intTest')
        if (intTestTask != null && project.hasProperty('useMysql')) {
            intTestTask.configure({
                environment 'SPRING_DATASOURCE_URL', project.extensions.flyway.url
                environment 'SPRING_DATASOURCE_PLATFORM', 'mysql'
                environment 'SPRING_DATASOURCE_USERNAME', 'root'
                environment 'SPRING_JPA_GENERATE_DDL', 'false'
                environment 'SPRING_JPA_SHOW_SQL', 'true'
                environment 'FLYWAY_ENABLED', 'false'
            })
            intTestTask.dependsOn(project.tasks.flywayMigrate)
            project.tasks.stopMysqlContainer.mustRunAfter(intTestTask)
            project.tasks.removeMysqlContainer.mustRunAfter(intTestTask)
            intTestTask.finalizedBy(project.tasks.stopMysqlContainer)
            intTestTask.finalizedBy(project.tasks.removeMysqlContainer)
        }
    }

}
