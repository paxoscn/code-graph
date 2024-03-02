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

public class MethodInvoking implements Expression {

    public Expression callee;
    public String methodName;
    public List<Expression> parameters = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        if (parseTree instanceof JavaParser.MethodCallContext) {
            JavaParser.MethodCallContext methodCallContext = (JavaParser.MethodCallContext) parseTree;
            if (methodCallContext.getChild(0) instanceof TerminalNodeImpl) {
                // super(...)
                TerminalNodeImpl terminalNode = (TerminalNodeImpl) methodCallContext.getChild(0);
                this.methodName = terminalNode.getText();
            } else {
                JavaParser.IdentifierContext identifierContext = (JavaParser.IdentifierContext) methodCallContext.getChild(0);
                this.methodName = identifierContext.getText();
            }
            JavaParser.ArgumentsContext argumentsContext = (JavaParser.ArgumentsContext) methodCallContext.getChild(1);
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
        } else {
            if (parseTree.getChild(1).getText().equals("::")) {
                Reference reference = new Reference();
                reference.referenceName = parseTree.getChild(0).getText();
                callee = reference;

                this.methodName = parseTree.getChild(2).getText();
            } else {
                callee = Builder.build((JavaParser.ExpressionContext) parseTree.getChild(0));

                JavaParser.MethodCallContext methodCallContext = (JavaParser.MethodCallContext) parseTree.getChild(2);
                JavaParser.IdentifierContext identifierContext = (JavaParser.IdentifierContext) methodCallContext.getChild(0);
                this.methodName = identifierContext.getText();
                JavaParser.ArgumentsContext argumentsContext = (JavaParser.ArgumentsContext) methodCallContext.getChild(1);
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
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        if (callee != null) {
            callee.print(printStream, prefix);
        } else {
            printStream.println(prefix + "#null");
        }
        printStream.println(prefix + "        ." + this.methodName + "(");

        for (Expression parameter : parameters) {
            if (parameter != null) {
                parameter.print(printStream, prefix + "            ");
            }
        }

        printStream.println(prefix + "        )");
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        if (callee != null) {
            callee.draw(drawingContext, graph);
            String expressionType = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE);

            String currentClass = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS);
            if (!currentClass.equals(expressionType)) {
                graph.addEdge(currentClass, expressionType);
            }
        }

        for (Expression parameter : parameters) {
            if (parameter != null) {
                parameter.draw(drawingContext, graph);
            }
        }

        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_EXPRESSION_TYPE, null);
    }
}
