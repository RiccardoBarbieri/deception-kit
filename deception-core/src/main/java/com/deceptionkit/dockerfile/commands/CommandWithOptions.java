package com.deceptionkit.dockerfile.commands;

import com.deceptionkit.dockerfile.options.CommandOptions;

public abstract class CommandWithOptions extends Command {

    protected CommandOptions options;

    public void options(CommandOptions options) {
        this.options = options;
    }
}
