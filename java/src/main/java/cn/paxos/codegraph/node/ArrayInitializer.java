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
import java.util.Map;

public class ArrayInitializer implements Expression {

    public List<Expression> items = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        for (int ai = 0, ac = parseTree.getChildCount(); ai < ac; ai++) {
            ParseTree arrayItem = parseTree.getChild(ai);

            if (arrayItem instanceof JavaParser.ExpressionContext) {
                JavaParser.ExpressionContext expressionContext = (JavaParser.ExpressionContext) arrayItem;
                Expression item = Builder.build(expressionContext);
                items.add(item);
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + "[");

        for (Expression item : items) {
            if (item != null) {
                item.print(printStream, prefix + "    ");
            }
        }

        printStream.println(prefix + "]");
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        for (Expression item : items) {
            if (item != null) {
                item.draw(drawingContext, graph);
            }
        }

        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, null);
    }
}
