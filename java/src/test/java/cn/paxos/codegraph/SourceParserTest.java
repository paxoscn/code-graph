package cn.paxos.codegraph;

import cn.paxos.codegraph.node.Source;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.Map.*;
import static java.util.Arrays.stream;

class SourceParserTest {

    @Test
    void parse() throws IOException {
        SourceParser sourceParser = new SourceParser();
        Source source = sourceParser.parse("./src/test/java/cn/paxos/codegraph/SourceParserTest.java");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos, true);
        source.print(printStream, "");

        String actual = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        String expected = new String(Files.readAllBytes(
                new File("./src/test/resources/source-parser-expectation.txt").toPath()), StandardCharsets.UTF_8);
        assertEquals(expected, actual);
    }

    private static class AAA {

        BBB b0 = new BBB();
        int i = 1, j;

        static {
            int i = 0;
        }

        void BBB() {
            BBB b1 = new BBB();
            BBB b2 = bbb();
            String b3 = (new BBB()).toString().toString();
            b0.ccc();
            b1.ccc();
            new BBB().ccc();
            bbb().ccc();
            this.bbb().ccc();
            BBB.ccc();

            if (b3.equals("1")) {
                System.out.println("!");
            }
            while (b3.equals("1"))
                System.out.println("!");
        }

        BBB bbb() {
            return null;
        }
    }

    private static class BBB {

        BBB() {
            super();
        }

        static void ccc() {
            String[] a0 = { "a", "b", "c" };
            String[] a1 = new String[] { "a", "b", "c" };
            return;
        }
    }
}