/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.deceptionkit.cli;


import com.deceptionkit.cli.config.ConfigLoader;
import com.deceptionkit.cli.error.ExecutionExceptionHandler;
import com.deceptionkit.cli.subcommand.GenerateSubcommand;
import com.deceptionkit.cli.utils.PingUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "deception-cli",
        abbreviateSynopsis = true,
        mixinStandardHelpOptions = true,
        version = "deception-cli 0.2",
        description = "CLI for DeceptionKit",
        subcommands = {GenerateSubcommand.class})
public class DeceptionCli implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-f", "--force-restart"},
            description = "Force restart of the deception-core container",
            defaultValue = "false")
    private boolean forceRestart;

    @CommandLine.Option(names = {"-c", "--config"},
            description = "Path to the configuration file",
            required = false,
            defaultValue = "${env:DEFAULT_CONFIG}")
    private String configPath;

    public static void main(String[] args) {
        System.out.println("DECEPTION-KIT");

        CommandLine cmd = new picocli
                .CommandLine(new DeceptionCli())
                .setExecutionExceptionHandler(new ExecutionExceptionHandler());
        cmd.setExecutionStrategy(new CommandLine.RunLast());

        int exitCode = cmd.execute(args);

        System.exit(exitCode);
    }

    public Integer call() throws Exception {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        configLoader.setConfigPath(configPath);
        if (!PingUtils.pingDeceptionCore()) {
            System.out.println("Deception Core is not running");
            System.exit(1);
        }

        spec.commandLine().usage(System.err);
        return 0;
    }


}
