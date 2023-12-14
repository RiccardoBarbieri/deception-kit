package com.deception.cli.subcommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.*;

@Command(name = "generate",
        description = "Generate a new deception component",
        abbreviateSynopsis = true,
        version = "deception-cli 0.1",
        mixinStandardHelpOptions = true)
public class GenerateSubcommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--component"},
            description = "The component to generate",
            required = true)
    private String component;

    @CommandLine.Option(names = {"-d", "--definition"},
            description = "The definition of the component",
            required = true)
    private File definitionFile;

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(definitionFile));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + definitionFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error reading file: " + definitionFile.getAbsolutePath());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing file: " + definitionFile.getAbsolutePath());
            }
        }

    }
}
