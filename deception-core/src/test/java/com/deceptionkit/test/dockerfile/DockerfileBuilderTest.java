package com.deceptionkit.test.dockerfile;


import com.deceptionkit.dockerfile.DockerfileBuilder;
import com.deceptionkit.dockerfile.commands.AddCommand;
import com.deceptionkit.dockerfile.commands.CommandBuilder;
import com.deceptionkit.dockerfile.options.CommandOptionsBuilder;
import org.junit.jupiter.api.Test;

public class DockerfileBuilderTest {

    //TODO: spostare tutti i metodi full di costruzione comando (e.g. add(src, dest) per add) in CommandBuilder
    //TODO: stessa cosa per CommandOptionsBuilder???

    @Test
    public void testAddCommandWithOptions() {
        DockerfileBuilder builder = new DockerfileBuilder();
        builder.addCommand(CommandBuilder.add().src("src").dest("dest"), CommandOptionsBuilder.add().checksum("abc123"));
        builder.addCommand(CommandBuilder.add().add("src", "dest"), CommandOptionsBuilder.add().checksum("abc123").chown("user:group").exclude("**\\||*"));

        String file = builder.build();
        System.out.println(file);
    }


}
