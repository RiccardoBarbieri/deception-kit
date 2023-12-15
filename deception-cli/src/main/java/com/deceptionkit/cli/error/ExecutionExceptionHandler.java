package com.deceptionkit.cli.error;

import picocli.CommandLine;

import java.util.Arrays;

public class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(Exception e, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {

        commandLine.getErr().println(commandLine.getColorScheme().errorText("_____________________________________________________"));
        commandLine.getErr().println(commandLine.getColorScheme().errorText(e.getMessage()));
//        commandLine.getErr().println(commandLine.getColorScheme().errorText(Arrays.toString(e.printStackTrace())));
        e.printStackTrace(commandLine.getErr());
        commandLine.getErr().println(commandLine.getColorScheme().errorText("_____________________________________________________"));

        return commandLine.getExitCodeExceptionMapper() != null
                    ? commandLine.getExitCodeExceptionMapper().getExitCode(e)
                    : commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
