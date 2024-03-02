/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */

package cn.paxos.codegraph.node;

import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;

public interface Node {

    String DRAWING_CONTEXT_ATTRIBUTE_CLASSES_IN_PROJECT = "CLASSES_IN_PROJECT";
    String DRAWING_CONTEXT_ATTRIBUTE_PACKAGE = "PACKAGE";
    String DRAWING_CONTEXT_ATTRIBUTE_CLASS = "CLASS";
    String DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TYPE = "VARIABLE_TYPE";
    String DRAWING_CONTEXT_ATTRIBUTE_VARIABLE = "VARIABLE";
    String DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE = "EXPRESSION_TYPE";
    String DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN = "CLASS_TO_FQN";
    String DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS = "VARIABLE_TO_CLASS";
    String DRAWING_CONTEXT_ATTRIBUTE_IMPORTED_METHOD_TO_CLASS = "IMPORTED_METHOD_TO_CLASS";

    default void build(ParseTree parseTree) {}

    default void print(PrintStream printStream, String prefix) {}

    default void draw(DrawingContext drawingContext, Graph graph) {}
}
