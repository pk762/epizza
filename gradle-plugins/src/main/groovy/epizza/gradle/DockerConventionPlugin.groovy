package epizza.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.Copy

import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage

import org.gradle.api.tasks.Sync
import org.apache.tools.ant.filters.ReplaceTokens

class DockerConventionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply(plugin: PublishingPlugin)
        project.apply(plugin: DockerRemoteApiPlugin)
        project.apply(plugin: JavaPlugin)

        def dockerImageTag = "latest"
        def dockerImageName = "epizza/" + project.name

        project.extensions.docker.with {
            if (System.env.DOCKER_HOST) {
                url = "$System.env.DOCKER_HOST".replace("tcp","https")
                if (System.env.DOCKER_CERT_PATH) {
                    certPath = new File(System.env.DOCKER_CERT_PATH)
                }
            } else {
                url = 'unix:///var/run/docker.sock'
            }
            registryCredentials {
                if (System.env.DOCKER_REGISTRY_URL) {
                    url = System.env.DOCKER_REGISTRY_URL
                }
                if (System.env.DOCKER_REGISTRY_USERNAME) {
                    username = System.env.DOCKER_REGISTRY_USERNAME
                }
                if (System.env.DOCKER_REGISTRY_PASSWORD) {
                   password  = System.env.DOCKER_REGISTRY_PASSWORD
                }
                if (System.env.DOCKER_REGISTRY_EMAIL) {
                    email = System.env.DOCKER_REGISTRY_EMAIL
                }
            }
        }

        project.task('copyDockerfile', type:Copy) {
            group = DockerRemoteApiPlugin.DEFAULT_TASK_GROUP
            description = 'Copies the Dockerfile into its target directory.'
            from 'Dockerfile'
            into 'build/docker'
        }

        project.task('assembleDockerImageContents') {
            group = DockerRemoteApiPlugin.DEFAULT_TASK_GROUP
            description = 'Assembles docker image contents'
            it.dependsOn(project.tasks.copyDockerfile)
        }

        project.task('buildDockerImage', type:DockerBuildImage) {
            description = 'Builds a new docker image.'
            dependsOn project.tasks.assembleDockerImageContents
            inputDir = project.file('build/docker')
            tag = "$dockerImageName:$dockerImageTag"
        }

        project.task('removeDockerImage', type:DockerRemoveImage) {
            description = 'Removes the docker image from the local filesystem.'
            imageId = "$dockerImageName:$dockerImageTag"
        }

        project.task('pushDockerImage', type:DockerPushImage) {
            description = 'Pushes the docker image to your docker repository.'
            dependsOn project.tasks.buildDockerImage
            imageName = dockerImageName
            tag = dockerImageTag
        }

        project.tasks.publish.dependsOn(project.tasks.pushDockerImage)

        //if(project.plugins.hasPlugin('spring-boot')) {

            project.task('explodeFatJarToDocker', type:Sync, dependsOn:['bootRepackage']) {
                // TODO avoid duplicating repackaged jar name here.
                // see https://github.com/spring-projects/spring-boot/issues/5861
                def fatJar = project.zipTree('build/libs/app.jar')
                from(fatJar)
                into('build/docker/app')
                doLast {
                    project.fileTree('build/docker/app/BOOT-INF/lib').files.each { File file ->
                        // https://jira.spring.io/browse/SPR-12862
                        file.setLastModified(10_000L)
                    }
                }
            }

            project.tasks.assembleDockerImageContents.dependsOn(project.tasks.explodeFatJarToDocker)

            project.afterEvaluate {
                project.tasks.bootRun {
                    systemProperties = System.properties
                }

                project.tasks.bootRepackage {
                    outputFile = project.file('build/libs/app.jar')
                }

                project.extensions.eclipse.project {
                     // Spring STS
                    natures += 'org.springframework.ide.eclipse.core.springnature'
                    buildCommand 'org.springframework.ide.eclipse.core.springbuilder'
                }

                def replaceAction = {
                        filter(ReplaceTokens, tokens:
                            [
                            'project.artifactId' : project.group + ":" + project.name,
                            'project.name': project.name,
                            'project.description' : project.description ?: "",
                            'project.version' : "${project.version}".toString()
                            ]
                        )
                }

                project.tasks.processResources {
                    filesMatching('/application.*', replaceAction)
                    filesMatching('/banner.txt', replaceAction)
                }
            }
        //}
    }
}
