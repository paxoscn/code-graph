package cn.paxos.codegraph.node;

import cn.paxos.codegraph.SourceParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void draw() throws IOException {
        SourceParser sourceParser = new SourceParser();
        Source source = sourceParser.parse("./src/test/java/cn/paxos/codegraph/SourceParserTest.java");

        DrawingContext drawingContext = new DrawingContext();
        Graph graph = new Graph();
        source.draw(drawingContext, graph);

        String actual = graph.toPlantUML();
        String expected = "";
        assertEquals(expected, actual);
    }
}