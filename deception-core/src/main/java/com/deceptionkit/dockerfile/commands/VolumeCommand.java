package com.deceptionkit.dockerfile.commands;

public class VolumeCommand extends Command {

    public static final String COMMAND = "VOLUME";

    private String volume;
    private boolean spaced = false;

    protected VolumeCommand() {
    }

    public VolumeCommand volume(String volume) {
        this.volume = volume;
        if (volume.contains(" ")) {
            spaced = true;
        }
        return this;
    }

    @Override
    public String build() {
        if (volume == null) {
            throw new IllegalArgumentException("VOLUME command must have a volume");
        }
        String line;
        if (spaced) {
            line = COMMAND + " " + "[\"" + volume + "\"]";
        }
        else {
            line = COMMAND + " " + volume;
        }
        return line;
    }
}
