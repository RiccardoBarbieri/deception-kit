package com.deceptionkit.dockerfile.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {

    public static AddCommand add() {
        return new AddCommand();
    }

    public static AddCommand add(String source, String destination) {
        return new AddCommand().add(source, destination);
    }

    public static ArgCommand arg() {
        return new ArgCommand();
    }

    public static ArgCommand arg(String name, String defaultValue) {
        return new ArgCommand().arg(name, defaultValue);
    }

    public static CmdCommand cmd() {
        return new CmdCommand();
    }

    public static CmdCommand cmd(String executable, String... args) {
        return new CmdCommand().cmd(executable, new ArrayList<>(List.of(args)));
    }

    public static CopyCommand copy() {
        return new CopyCommand();
    }

    public static EntrypointCommand entrypoint() {
        return new EntrypointCommand();
    }

    public static EnvCommand env() {
        return new EnvCommand();
    }

    public static ExposeCommand expose() {
        return new ExposeCommand();
    }

    public static FromCommand from() {
        return new FromCommand();
    }

    public static HealthcheckCommand healthcheck() {
        return new HealthcheckCommand();
    }

    public static LabelCommand label() {
        return new LabelCommand();
    }

    @Deprecated
    public static MaintainerCommand maintainer() {
        return new MaintainerCommand();
    }

    public static OnbuildCommand onbuild() {
        return new OnbuildCommand();
    }

    public static RunCommand run() {
        return new RunCommand();
    }

    public static ShellCommand shell() {
        return new ShellCommand();
    }

    public static StopsignalCommand stopsignal() {
        return new StopsignalCommand();
    }

    public static UserCommand user() {
        return new UserCommand();
    }

    public static VolumeCommand volume() {
        return new VolumeCommand();
    }

    public static WorkdirCommand workdir() {
        return new WorkdirCommand();
    }


}
