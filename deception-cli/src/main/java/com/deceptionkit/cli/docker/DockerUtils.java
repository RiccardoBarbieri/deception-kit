package com.deceptionkit.cli.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DockerUtils {

    public static boolean checkAndRestartDaemon(boolean forceRestart) {

        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryPassword(System.getenv("DOCKER_REGISTRY_PASSWORD"))
                .withRegistryUsername("riccardoob")
                .build();
        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config).build()) {

            if (forceRestart) {
                stopContainerIfRunning("deception-core", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);
                deleteContainerIfExists("deception-core", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);
            }

            if (!isContainerRunning("deception-core", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient) &&
                    !doesContainerExist("deception-core", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient)) {

                HostConfig hostConfig = new HostConfig();

                hostConfig.withPortBindings(new PortBinding(new Ports.Binding("localhost", "8015"), new ExposedPort(8015)));

                hostConfig.withNetworkMode("bridge");

                CreateContainerResponse containerResponse = dockerClient
                        .createContainerCmd("riccardoob/deception-core")
                        .withName("deception-core")
                        .withEnv("MOCKAROO_API_KEY=" + System.getenv("MOCKAROO_API_KEY"))
                        .withHostConfig(hostConfig)
                        .exec();

                dockerClient.startContainerCmd(containerResponse.getId()).exec();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean createNewKeycloakDev() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config).build()) {
            stopContainerIfRunning("keycloak-dev", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);
            deleteContainerIfExists("keycloak-dev", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);

            HostConfig hostConfig = new HostConfig();

            ExposedPort exposedPort = new ExposedPort(8080, InternetProtocol.TCP);
            Ports portBindings = new Ports();
            portBindings.bind(exposedPort, Ports.Binding.bindPort(8100));

            hostConfig.withPortBindings(portBindings);

            hostConfig.withNetworkMode("bridge");

            CreateContainerResponse containerResponse = dockerClient
                    .createContainerCmd("quay.io/keycloak/keycloak:22.0.5")
                    .withName("keycloak-dev")
                    .withEnv("KEYCLOAK_ADMIN=admin", "KEYCLOAK_ADMIN_PASSWORD=admin")
                    .withCmd("start-dev")
                    .withHostConfig(hostConfig)
                    .exec();

            dockerClient.startContainerCmd(containerResponse.getId()).exec();

            Thread.sleep(18000);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean removeKeycloakDev() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config).build()) {
            stopContainerIfRunning("keycloak-dev", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);
            deleteContainerIfExists("keycloak-dev", dockerClient.listContainersCmd().withShowAll(true).exec(), dockerClient);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static boolean isContainerRunning(String name, List<Container> containers, DockerClient dockerClient) {
        return containers.stream().filter(c -> (c.getState()).contains("running")).anyMatch(c -> Arrays.asList(c.getNames()).contains("/" + name));
    }

    private static boolean doesContainerExist(String name, List<Container> containers, DockerClient dockerClient) {
        return containers.stream().anyMatch(c -> Arrays.asList(c.getNames()).contains("/" + name));
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

    public static boolean exportKeycloakConfig(String destination) {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config).build()) {
            String containerId = dockerClient.listContainersCmd().withShowAll(true).exec().stream()
                    .filter(c -> Arrays.asList(c.getNames()).contains("/keycloak-dev"))
                    .findFirst()
                    .get()
                    .getId();

            executeDockerCommand(dockerClient, containerId, "mkdir", "-p", "/opt/keycloak/export");

            executeDockerCommand(dockerClient, containerId, "/opt/keycloak/bin/kc.sh", "export", "--file", "/opt/keycloak/export/export.json");

            copyFile(dockerClient, containerId, "/opt/keycloak/export/export.json", destination);

        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static void executeDockerCommand(DockerClient dockerClient, String containerId, String... args) throws InterruptedException {
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd(args)
                .exec();

        ExecStartCmd execStartCmd = dockerClient.execStartCmd(execCreateCmdResponse.getId());
        execStartCmd.exec(new ResultCallback.Adapter<>() {
            @Override
            public void onNext(Frame object) {
            }
        }).awaitCompletion();
    }

    private static void copyFile(DockerClient dockerClient, String containerId, String source, String destination) throws InterruptedException, IOException {
        CopyArchiveFromContainerCmd copyCmd = dockerClient.copyArchiveFromContainerCmd(containerId, source);

        TarArchiveInputStream tarStream = new TarArchiveInputStream(copyCmd.exec());
        unTar(tarStream, new File(destination));
    }

    private static void unTar(TarArchiveInputStream tis, File destFile) throws IOException {
        TarArchiveEntry tarEntry = null;
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            if (tarEntry.isDirectory()) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
            } else {
                FileOutputStream fos = new FileOutputStream(destFile);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }

//    public static void createKeycloakDockerfile(String config) {
//        File dockerfile = new File("Dockerfile");
//        String baseImage = "quay.io/keycloak/keycloak:22.0.5";
//        try (FileOutputStream fos = new FileOutputStream(dockerfile)) {
//            fos.write(("FROM " + baseImage + " as builder\n" +
//                    "ENV KC_HEALTH_ENABLED=true\n" +
//                    "ENV KC_METRICS_ENABLED=true\n" +
//                    "ENV KC_HTTP_ENABLED=false\n" +
//                    "WORKDIR /opt/keycloak\n" +
//                    "RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname \"CN=server\" -alias server -ext \"SAN:c=DNS:localhost,IP:127.0.0.1\" -keystore conf/server.keystore\n" +
//                    "RUN /opt/keycloak/bin/kc.sh build\n" +
//
//                    "FROM quay.io/keycloak/keycloak:latest\n" +
//                    "COPY --from=builder /opt/keycloak/ /opt/keycloak/\n" +
//                    "COPY " + config + " /opt/keycloak/export.json\n" +
//                    "RUN [\"/opt/keycloak/bin/kc.sh\", \"import\", \"--file\", \"/opt/keycloak/export.json\"]\n" +
//                    "ENV KEYCLOAK_ADMIN=admin\n" +
//                    "ENV KEYCLOAK_ADMIN_PASSWORD=admin\n" +
//                    "ENV KC_HOSTNAME=localhost\n" +
//                    "ENTRYPOINT [\"/opt/keycloak/bin/kc.sh\", \"start\", \"--optimized\"]\n").getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
