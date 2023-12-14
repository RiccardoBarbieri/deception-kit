package com.deception.cli.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DockerConfig {

    public static boolean checkAndRestartDaemon() {

        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryPassword(System.getenv("DOCKER_REGISTRY_PASSWORD"))
                .withRegistryUsername("riccardoob")
                .build();
        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config).build()) {
            List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();

            System.out.println("__________________Checking container ");
            stopContainerIfRunning("deception-core", containers, dockerClient);
            deleteContainerIfExists("deception-core", containers, dockerClient);

            HostConfig hostConfig = new HostConfig();
            hostConfig.withPortBindings(new PortBinding(new Ports.Binding("localhost", "8015"), new ExposedPort(8015)));
            CreateContainerResponse containerResponse = dockerClient
                    .createContainerCmd("riccardoob/deception-core")
                    .withName("deception-core")
                    .withEnv("MOCKAROO_API_KEY=" + System.getenv("MOCKAROO_API_KEY"))
                    .withHostName("localhost")
                    .withHostConfig(hostConfig)
                    .exec();

            dockerClient.startContainerCmd(containerResponse.getId()).exec();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static void stopContainerIfRunning(String name, List<Container> containers, DockerClient dockerClient) {
        if (containers.stream().filter(c -> (c.getState()).contains("running")).anyMatch(c -> Arrays.asList(c.getNames()).contains("/" + name))) {
            dockerClient.stopContainerCmd(name).exec();
        }
    }

    private static void deleteContainerIfExists(String name, List<Container> containers, DockerClient dockerClient) {
        if (containers.stream().anyMatch(c -> Arrays.asList(c.getNames()).contains("/" + name))) {
            dockerClient.removeContainerCmd(name).exec();
        }
    }
}
