package epizza.gradle

import org.asciidoctor.gradle.AsciidoctorPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.StandardOutputListener
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.tasks.Jar
import org.gradle.tooling.BuildException

class RestDocsConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply(plugin: AsciidoctorPlugin)
        project.apply(plugin: MavenPublishPlugin)

        // api docs
        def snippetsDir = project.file('build/generated-snippets')

        project.tasks.test {
            systemProperty 'org.springframework.restdocs.outputDir', snippetsDir
            outputs.dir snippetsDir
        }

        project.tasks.asciidoctor {
            sources {
                include '*.adoc'
            }

            attributes 'snippets': snippetsDir
            attributes 'version': project.version

            inputs.dir snippetsDir
            dependsOn project.tasks.check

            // include checking //
            ext.capturedOutput = [ ]
            def listener = { ext.capturedOutput << it } as StandardOutputListener

            logging.addStandardErrorListener(listener)
            logging.addStandardOutputListener(listener)

            doLast {
                logging.removeStandardOutputListener(listener)
                logging.removeStandardErrorListener(listener)
                ext.capturedOutput.join('').with { output ->
                    if (output =~ /include file not found:/) {
                        throw new RuntimeException("Include file(s) not found.\n" + output)
                    }
                }
            }
        }

        project.task('restdocsJar', type: Jar) {
            description = 'Generates the jar file containing the HTML files of your REST API documentation.'
            group = 'Documentation'
            classifier = 'restdocs'
            dependsOn project.tasks.asciidoctor
            from ("${project.tasks.asciidoctor.outputDir}/html5") {
                into 'static/docs'
            }
        }

        project.task('wiremockJar', type: Jar) {
            description = 'Generates the jar file containing the wiremock stubs for your REST API.'
            group = 'Build'
            classifier = 'wiremock'
            dependsOn project.tasks.test
            from (snippetsDir) {
                include '**/wiremock-stub.json'
                into "wiremock/${project.name}/mappings"
            }
        }

        // make sure we produce asciidoc. But no need to auto-publish them yet.
        project.tasks.publish.dependsOn(project.tasks.asciidoctor)

        project.tasks.build.dependsOn(project.tasks.asciidoctor)

        project.publishing {
            publications {
                wiremockJar(MavenPublication) {
                    from project.components.java
                    artifact project.tasks.wiremockJar {
                        classifier "wiremock"
                    }
                }
            }
        }
    }
}
