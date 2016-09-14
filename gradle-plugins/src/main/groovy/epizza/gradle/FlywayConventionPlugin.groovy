package epizza.gradle

import java.util.concurrent.ThreadLocalRandom;

import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.FlywayPlugin
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerLogsContainer
import com.bmuschko.gradle.docker.tasks.container.DockerRemoveContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage

class FlywayConventionPlugin implements Plugin<Project> {

    private static final String PULL_IMAGE_TASK = 'pullMysqlImage'
    private static final String CREATE_CONTAINER_TASK = 'createMysqlContainer'
    private static final String START_CONTAINER_TASK = 'startMysqlContainer'
    private static final String STOP_CONTAINER_TASK = 'stopMysqlContainer'
    private static final String WAIT_FOR_CONTAINER_READY_TASK = 'waitForMysqlContainerReady'
    private static final String REMOVE_CONTAINER_TASK = 'removeMysqlContainer'

    @Override
    void apply(Project project) {
        project.apply(plugin: FlywayPlugin)
        project.apply(plugin: DockerConventionPlugin)
        project.extensions.create("mysql", DockerMysqlExtension)
        addDockerTasks(project)
        addFlywayDependencies(project)
        preconfigureFlywayExtension(project)
    }

    private void addFlywayDependencies(Project project) {
        project.tasks.flywayMigrate.doFirst {
            if(!project.extensions.mysql.ready) {
                throw new GradleException('MySQL is not ready.')
            }
        }

        project.tasks.flywayMigrate.dependsOn(project.tasks.waitForMysqlContainerReady)
        project.tasks.flywayMigrate.finalizedBy(project.tasks.stopMysqlContainer)
        project.tasks.flywayMigrate.finalizedBy(project.tasks.removeMysqlContainer)
    }

    private void preconfigureFlywayExtension(Project project) {
        project.extensions.configure(FlywayExtension) { ext ->
            def url = URI.create(System.env.DOCKER_HOST ?: 'http://localhost')
            ext.driver = 'com.mysql.jdbc.Driver'
            ext.url = "jdbc:mysql://${url.host}:${project.extensions.mysql.hostPort}" +
                 '/inttest?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&requireSSL=false'
            ext.user = 'root'
            ext.password = ''
        }
    }

    private void addDockerTasks(Project project) {
        project.task(PULL_IMAGE_TASK, type:DockerPullImage) {
            description = 'Pulls the official MySQL docker image'
            repository = 'mysql'
            tag = '5.7'
        }

        project.task(CREATE_CONTAINER_TASK, type:DockerCreateContainer, dependsOn: PULL_IMAGE_TASK) {
            def mysqlHostPort = project.extensions.mysql.hostPort
            description = 'Creates the mysql docker container'
            targetImageId { project.tasks.pullMysqlImage.repository + ':' + project.tasks.pullMysqlImage.tag }
            env = [ 'MYSQL_ALLOW_EMPTY_PASSWORD=yes', 'MYSQL_DATABASE=inttest' ]
            portBindings = [ mysqlHostPort + ':3306']
            containerName = 'ng_mysql_inttest_' + mysqlHostPort
            doLast {
                println "Starting MySQL docker container with host port: ${mysqlHostPort}"
            }
        }

        project.task(START_CONTAINER_TASK, type:DockerStartContainer, dependsOn:CREATE_CONTAINER_TASK) {
            description = 'Starts the mysql docker container'
            targetContainerId { project.tasks.createMysqlContainer.containerId }
            doLast {
                println 'Initial sleeping (15s)...'
                Thread.sleep(15_000L)
            }
        }

        project.task(STOP_CONTAINER_TASK, type: DockerStopContainer) {
            description = 'Stops the mysql docker container'
            targetContainerId { project.tasks.createMysqlContainer.containerId }
        }

        project.task(REMOVE_CONTAINER_TASK, type: DockerRemoveContainer) {
            description = 'Removes the mysql docker container'
            targetContainerId { project.tasks.createMysqlContainer.containerId }
            mustRunAfter(STOP_CONTAINER_TASK)
            onlyIf {
                project.extensions.mysql.ready
            }
        }

        project.task(WAIT_FOR_CONTAINER_READY_TASK, type:DefaultTask) {
            description = 'Waits for mysql being ready to serve connections'
        }

        // workaround because polling is not properly implemented
        // https://github.com/bmuschko/gradle-docker-plugin/pull/234
        20.times { i ->
            def iTask = "retrieveMysqlLogs$i"
            project.task(iTask, type:DockerLogsContainer, dependsOn:START_CONTAINER_TASK) {
                onlyIf {
                    !project.extensions.mysql.ready
                }
                description = 'Checks docker logs for MySQL readiness ' + i
                sink = new StringWriter()
                targetContainerId { project.tasks.createMysqlContainer.containerId }
                tailCount = 10
                doLast {
                    if (sink.toString().contains('Ready for start up')) {
                        project.extensions.mysql.ready = true
                        println 'MySQL ready.'
                    } else {
                        println 'MySQL not ready yet. Sleeping (3s)...'
                        Thread.sleep(3000L)
                    }
                }
            }

            project.tasks.getByPath(WAIT_FOR_CONTAINER_READY_TASK).dependsOn(iTask)
        }
    }

}

class DockerMysqlExtension {
    def hostPort = 33060 + ThreadLocalRandom.current().nextInt(2000)
    def ready = false
}

