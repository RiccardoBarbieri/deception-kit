package com.deceptionkit.test.dockerfile;

import com.deceptionkit.dockerfile.commands.Command;
import com.deceptionkit.dockerfile.commands.CommandBuilder;
import com.deceptionkit.dockerfile.options.CommandOptionsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandBuilderTest {

//    public static void main(String[] args) {
//        Command com1 = CommandBuilder.add().src("src").dest("dest");//, CommandOptionsBuilder.add().checksum("abc123");
//        Command com2 = CommandBuilder.add().add("src", "dest");//, CommandOptionsBuilder.add().checksum("abc123").chown("user:group").exclude("**\\||*");
//
//        System.out.println(com1);
//
//        System.out.println(com2);
//
//        assertEquals(com1.build(), com2.build());
//    }

    @Test
    void add() {
        Command com1 = CommandBuilder.add().src("src").dest("dest");//, CommandOptionsBuilder.add().checksum("abc123");
        Command com2 = CommandBuilder.add().add("src", "dest");//, CommandOptionsBuilder.add().checksum("abc123").chown("user:group").exclude("**\\||*");

        System.out.println(com1.build());

        System.out.println(com2.build());

        assertEquals(com1.build(), com2.build());
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}