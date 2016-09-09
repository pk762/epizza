package epizza.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.gradle.plugins.ide.idea.IdeaPlugin

class JavaConventionPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.apply(plugin: JavaPlugin)
        project.apply(plugin: EclipsePlugin)
        project.apply(plugin: IdeaPlugin)

        configureIntTest(project)
        configureJavaCompiler(project)
    }

    private void configureIntTest(Project project) {
        project.sourceSets {
            inttest {
                compileClasspath += main.output
                runtimeClasspath += main.output
            }
        }

        project.configurations {
            inttestCompile.extendsFrom testCompile
            inttestRuntime.extendsFrom testRuntime
        }

        project.task('intTest',type: Test) {
            description = "Runs local integration tests"
            group = Test.TASK_GROUP
            testClassesDir = project.sourceSets.inttest.output.classesDir
            classpath = project.sourceSets.inttest.runtimeClasspath
        }

        project.tasks.check.dependsOn(project.tasks.intTest)
        project.tasks.intTest.shouldRunAfter(project.tasks.test)

        project.tasks.withType(Test) {
            reports.html.destination = "${project.reporting.baseDir}/${name}"
        }

        project.extensions.findByType(EclipseModel).with { eclipseModel ->
            eclipseModel.classpath.plusConfigurations.add(project.configurations.inttestCompile)
            eclipseModel.classpath.plusConfigurations.add(project.configurations.inttestRuntime)
        }

    }

    private void configureJavaCompiler(Project project) {
        project.sourceCompatibility = JavaVersion.VERSION_1_8
        project.targetCompatibility = JavaVersion.VERSION_1_8

        project.tasks.withType(JavaCompile) {
            options.fork = true
            options.compilerArgs << '-parameters'
        }
    }
}
