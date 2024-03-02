/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Reference implements Expression {

    public Expression callee;
    public String referenceName;

    @Override
    public void build(ParseTree parseTree) {
        if (parseTree instanceof JavaParser.IdentifierContext) {
            referenceName = parseTree.getText();
        } else {
            callee = Builder.build((JavaParser.ExpressionContext) parseTree.getChild(0));

            referenceName = parseTree.getChild(2).getText();
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        if (callee != null) {
            callee.print(printStream, prefix);
            printStream.println(prefix + "        ." + this.referenceName);
        } else {
            printStream.println(prefix + this.referenceName);
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        if (callee != null) {
            drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, null);
        } else {
            Map<String, String> variableToClass = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS);
            String variableType = variableToClass.get(this.referenceName);

            drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, variableType);
        }
    }
}
