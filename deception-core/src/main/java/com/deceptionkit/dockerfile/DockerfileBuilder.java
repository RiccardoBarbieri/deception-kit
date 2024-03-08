package com.deceptionkit.dockerfile;

import com.deceptionkit.dockerfile.commands.Command;
import com.deceptionkit.dockerfile.commands.CommandWithOptions;
import com.deceptionkit.dockerfile.options.CommandOptions;

import java.util.List;

public class DockerfileBuilder {

    List<String> lines;

    public DockerfileBuilder(List<String> lines) {
        this.lines = lines;
    }

    public DockerfileBuilder addCommand(CommandWithOptions command, CommandOptions options) {
        command.options(options);
        this.lines.add(command.build());
        return this;
    }

    public DockerfileBuilder addCommand(CommandWithOptions command) {
        this.lines.add(command.build());
        return this;
    }

    public DockerfileBuilder addCommand(Command command) {
        this.lines.add(command.build());
        return this;
    }

    public DockerfileBuilder addLine(String line) {
        lines.add(line);
        return this;
    }

    public DockerfileBuilder addLines(List<String> lines) {
        this.lines.addAll(lines);
        return this;
    }



}
