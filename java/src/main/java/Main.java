/*
 * Copyright 2022 paxos.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.paxos.codegraph.SourceParser;
import cn.paxos.codegraph.generated.JavaLexer;
import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import cn.paxos.codegraph.node.Node;
import cn.paxos.codegraph.node.Source;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String root = ".";
        if (args.length > 0) {
            root = args[0];
        }

        Graph graph = new Graph();

        Set<String> classesInProject = (Set<String>) graph.attributes.get(Graph.ATTRIBUTE_CLASSES_IN_PROJECT);

        iterateJavaFile(root, (filePath) -> {
            SourceParser sourceParser = new SourceParser();
            Source source = sourceParser.parse(filePath);

            DrawingContext drawingContext = new DrawingContext();
            source.draw(drawingContext, graph);

            Set<String> classesInSource = (Set<String>) drawingContext.attributes.get(Node.DRAWING_CONTEXT_ATTRIBUTE_CLASSES_IN_PROJECT);
            if (classesInSource != null) {
                classesInProject.addAll(classesInSource);
            }
        });

        System.out.println(graph.toPlantUML());
    }

    private static void iterateJavaFile(String root, JavaFileHandler javaFileHandler) throws Exception {
        Queue<String> paths = new LinkedList<>();
        paths.offer(root);

        while (!paths.isEmpty()) {
            String path = paths.poll();
            File file = new File(path);

            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    paths.offer(child.getAbsolutePath());
                }
            } else {
                if (file.getName().endsWith(".java")) {
                    javaFileHandler.handle(file.getAbsolutePath());
                }
            }
        }
    }

    interface JavaFileHandler {

        void handle(String filePath) throws Exception;
    }
}
