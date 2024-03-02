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

public class ConstructorInvoking implements Expression {

    public String constructorName;
    public List<Expression> parameters = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        JavaParser.CreatedNameContext createdNameContext = (JavaParser.CreatedNameContext) parseTree.getChild(0);
        this.constructorName = createdNameContext.getText();

        if (parseTree.getChild(1) instanceof JavaParser.ArrayCreatorRestContext) {
            // new String[] { "a", "b", ... }

            // TODO
        } else {
            JavaParser.ClassCreatorRestContext classCreatorRestContext = (JavaParser.ClassCreatorRestContext) parseTree.getChild(1);
            JavaParser.ArgumentsContext argumentsContext = (JavaParser.ArgumentsContext) classCreatorRestContext.getChild(0);
            if (argumentsContext.getChildCount() == 3) {
                JavaParser.ExpressionListContext expressionListContext = (JavaParser.ExpressionListContext) argumentsContext.getChild(1);

                for (int i = 0, c = expressionListContext.getChildCount(); i < c; i++) {
                    ParseTree child = expressionListContext.getChild(i);

                    if (child instanceof TerminalNodeImpl && child.getText().equals(",")) {
                        continue;
                    }

                    parameters.add(Builder.build((JavaParser.ExpressionContext) child));
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + this.constructorName + "(");

        for (Expression parameter : parameters) {
            if (parameter != null) {
                parameter.print(printStream, prefix + "    ");
            }
        }

        printStream.println(prefix + ")");
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        Map<String, String> classToFQN = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN);
        String calleeFQN = classToFQN.get(this.constructorName);
        if (calleeFQN == null) {
            String packageName = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_PACKAGE);
            calleeFQN = packageName + "." + this.constructorName;
        }

        String currentClass = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS);
        if (!currentClass.equals(calleeFQN)) {
            graph.addEdge(currentClass, calleeFQN);
        }

        for (Expression parameter : parameters) {
            if (parameter != null) {
                parameter.draw(drawingContext, graph);
            }
        }

        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, calleeFQN);
    }
}
