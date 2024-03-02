/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;
import java.util.Map;

public class Assignment implements Node {

    public String variableName;
    public Expression assignment;

    @Override
    public void build(ParseTree parseTree) {
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            if (child instanceof JavaParser.VariableDeclaratorIdContext) {
                JavaParser.VariableDeclaratorIdContext variableDeclaratorIdContext = (JavaParser.VariableDeclaratorIdContext) child;
                this.variableName = variableDeclaratorIdContext.getText();
            } else if (child instanceof JavaParser.VariableInitializerContext) {
                JavaParser.VariableInitializerContext variableInitializerContext = (JavaParser.VariableInitializerContext) child;
                if (variableInitializerContext.getChild(0) instanceof JavaParser.ArrayInitializerContext) {
                    ArrayInitializer arrayInitializer = new ArrayInitializer();
                    arrayInitializer.build(variableInitializerContext.getChild(0));

                    assignment = arrayInitializer;
                } else {
                    JavaParser.ExpressionContext expressionContext = (JavaParser.ExpressionContext) variableInitializerContext.getChild(0);
                    assignment = Expression.Builder.build(expressionContext);
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + this.variableName + (this.assignment != null ? " =" : ""));

        if (this.assignment != null) {
            this.assignment.print(printStream, prefix + "        ");
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        String variableType = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TYPE);

        Map<String, String> variableToClass = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS);
        variableToClass.put(this.variableName, variableType);

        if (this.assignment != null) {
            drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE, this.variableName);

            this.assignment.draw(drawingContext, graph);
        }
    }
}
