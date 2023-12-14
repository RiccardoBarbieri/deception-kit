/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.deception.cli;


import com.deception.cli.docker.DockerConfig;
import com.deception.cli.error.ExecutionExceptionHandler;
import com.deception.cli.error.MockExecutionExceptionHandler;
import com.deception.cli.subcommand.GenerateSubcommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "deception-cli",
        abbreviateSynopsis = true,
        mixinStandardHelpOptions = true,
        version = "deception-cli 0.1",
        description = "CLI for DeceptionKit",
        subcommands = {GenerateSubcommand.class})
public class DeceptionCli implements Callable<Integer> {

    public Integer call() throws Exception {
        DockerConfig.checkAndRestartDaemon();



        return 0;
    }

    public static void main(String[] args) {
        String[] argsMock = {"generate", "-c", "id-provider", "-d", "src/main/resources/definition.yaml"};
        CommandLine cmd = new picocli
                .CommandLine(new DeceptionCli())
                .setExecutionExceptionHandler(new ExecutionExceptionHandler());
        cmd.setExecutionStrategy(new CommandLine.RunAll());
        int exitCode = cmd.execute(argsMock);
        System.exit(exitCode);
    }


}
