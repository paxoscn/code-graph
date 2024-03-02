/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class Calculation implements Expression {

    public String operator;
    public Expression left;
    public Expression right;

    @Override
    public void build(ParseTree parseTree) {
        operator = parseTree.getChild(1).getText();

        if (parseTree.getChild(0) instanceof JavaParser.IdentifierContext) {
            // TODO
        } else {
            left = Expression.Builder.build((JavaParser.ExpressionContext) parseTree.getChild(0));
        }

        if (parseTree.getChild(2) instanceof JavaParser.IdentifierContext) {
            // TODO
        } else {
            right = Expression.Builder.build((JavaParser.ExpressionContext) parseTree.getChild(2));
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        if (left != null) {
            left.print(printStream, prefix);
        } else {
            printStream.println(prefix + "#null");
        }

        printStream.println(prefix + operator);

        if (right != null) {
            right.print(printStream, prefix);
        } else {
            printStream.println(prefix + "#null");
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        if (left != null) {
            left.draw(drawingContext, graph);
        }

        if (right != null) {
            right.draw(drawingContext, graph);
        }

        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, null);
    }
}
