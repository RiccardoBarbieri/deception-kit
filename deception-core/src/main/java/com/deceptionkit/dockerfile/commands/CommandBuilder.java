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

    public static CopyCommand copy(String source, String destination) {
        return new CopyCommand().add(source, destination);
    }

    public static EntrypointCommand entrypoint() {
        return new EntrypointCommand();
    }

    public static EntrypointCommand entrypoint(String executable, String... args) {
        return new EntrypointCommand().entrypoint(executable, new ArrayList<>(List.of(args)));
    }

    public static EnvCommand env() {
        return new EnvCommand();
    }

    public static EnvCommand env(String key, String value) {
        return new EnvCommand().env(key, value);
    }

    public static ExposeCommand expose() {
        return new ExposeCommand();
    }

    public static ExposeCommand expose(Integer port) {
        return new ExposeCommand().expose(port);
    }

    public static FromCommand from() {
        return new FromCommand();
    }

    public static FromCommand from(String image, String tag) {
        return new FromCommand().from(image).tag(tag);
    }

    public static HealthcheckCommand healthcheck() {
        return new HealthcheckCommand();
    }

    public static HealthcheckCommand healthcheck(String executable, String... args) {
        return new HealthcheckCommand().healthcheck(executable, new ArrayList<>(List.of(args)));
    }

    public static LabelCommand label() {
        return new LabelCommand();
    }

    public static LabelCommand label(String key, String value) {
        return new LabelCommand().label(key, value);
    }

    @Deprecated
    public static MaintainerCommand maintainer() {
        return new MaintainerCommand();
    }

    @Deprecated
    public static MaintainerCommand maintainer(String maintainer) {
        return new MaintainerCommand().maintainer(maintainer);
    }

    public static OnbuildCommand onbuild() {
        return new OnbuildCommand();
    }

    public static OnbuildCommand onbuild(Command command) {
        return new OnbuildCommand().onbuild(command);
    }

    public static RunCommand run() {
        return new RunCommand();
    }

    public static RunCommand run(String command) {
        return new RunCommand().command(command);
    }

    public static RunCommand run(String executable, String... args) {
        return new RunCommand().executable(executable).args(new ArrayList<>(List.of(args)));
    }

    public static ShellCommand shell() {
        return new ShellCommand();
    }

    public static ShellCommand shell(String shell, String... args) {
        return new ShellCommand().shell(shell, new ArrayList<>(List.of(args)));
    }

    public static StopsignalCommand stopsignal() {
        return new StopsignalCommand();
    }

    public static StopsignalCommand stopsignal(Integer signal) {
        return new StopsignalCommand().signal(signal);
    }

    public static UserCommand user() {
        return new UserCommand();
    }

    public static UserCommand user(String user, String group) {
        return new UserCommand().user(user, group);
    }

    public static UserCommand user(Integer user, Integer group) {
        return new UserCommand().user(user, group);
    }

    public static VolumeCommand volume() {
        return new VolumeCommand();
    }

    public static VolumeCommand volume(String volume) {
        return new VolumeCommand().volume(volume);
    }

    public static WorkdirCommand workdir() {
        return new WorkdirCommand();
    }

    public static WorkdirCommand workdir(String path) {
        return new WorkdirCommand().path(path);
    }


}
