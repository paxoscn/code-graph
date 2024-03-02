/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;

public class Block extends Process implements Statement {

    @Override
    public void build(ParseTree parseTree) {
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            if (child instanceof JavaParser.BlockStatementContext) {
                // The second one is ';'.
                ParseTree firstChildOfStatement = child.getChild(0);

                if (firstChildOfStatement instanceof JavaParser.LocalVariableDeclarationContext) {
                    FieldDeclaration fieldDeclaration = new FieldDeclaration();
                    statements.add(fieldDeclaration);

                    fieldDeclaration.build(firstChildOfStatement);
                } else if (firstChildOfStatement instanceof JavaParser.StatementContext) {
                    statements.add(Statement.Builder.build((JavaParser.StatementContext) firstChildOfStatement));
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + "#block");

        for (Statement statement : statements) {
            if (statement != null) {
                statement.print(printStream, prefix + "    ");
            }
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        for (Statement statement : statements) {
            if (statement != null) {
                statement.draw(drawingContext, graph);
            }
        }
    }
}
