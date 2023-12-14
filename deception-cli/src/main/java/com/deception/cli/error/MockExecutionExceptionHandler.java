package com.deception.cli.error;

import picocli.CommandLine;

public class MockExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {

        @Override
        public int handleExecutionException(Exception e, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {

            commandLine.getErr().println(commandLine.getColorScheme().errorText("______________TEST______________"));

            return -1;
        }
}
