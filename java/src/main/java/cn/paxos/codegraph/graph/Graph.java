/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    public static final String ATTRIBUTE_RENDERS_PACKAGE_NAME = "RENDERS_PACKAGE_NAME";
    public static final String ATTRIBUTE_RENDERS_CLASSES_IN_PROJECT_ONLY = "RENDERS_CLASSES_IN_PROJECT_ONLY";
    public static final String ATTRIBUTE_CLASSES_IN_PROJECT = "CLASSES_IN_PROJECT";

    public Map<String, Object> attributes = new HashMap() {{
        put(Graph.ATTRIBUTE_RENDERS_PACKAGE_NAME, false);
        put(Graph.ATTRIBUTE_RENDERS_CLASSES_IN_PROJECT_ONLY, true);
        put(Graph.ATTRIBUTE_CLASSES_IN_PROJECT, new HashSet<String>());
    }};

    private Set<String> vertices = new HashSet<>();
    private Set<String> edges = new HashSet<>();

    public void addEdge(String out, String in) {
        vertices.add(out);
        vertices.add(in);
        edges.add(out + " " + in);
    }

    public String toPlantUML() {
        StringBuilder sb = new StringBuilder();
        sb.append("left to right direction\n");

        for (String edge : edges) {
            String out = edge.substring(0, edge.indexOf(" "));
            String in = edge.substring(edge.indexOf(" ") + 1);

            if (out.equals("null") || in.equals("null")) {
                continue;
            }

            if ((Boolean) attributes.get(ATTRIBUTE_RENDERS_CLASSES_IN_PROJECT_ONLY)) {
                Set<String> classesInProject = (Set<String>) attributes.get(Graph.ATTRIBUTE_CLASSES_IN_PROJECT);

                if (!classesInProject.contains(out) || !classesInProject.contains(in)) {
                    continue;
                }
            }

            if (!(Boolean) attributes.get(ATTRIBUTE_RENDERS_PACKAGE_NAME)) {
                out = out.replaceFirst(".*\\.", "");
                in = in.replaceFirst(".*\\.", "");
            }

            sb.append(out);
            sb.append(" ..> ");
            sb.append(in);
            sb.append("\n");
        }

        return sb.toString();
    }
}
