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

public class MethodDefinition extends Process {

    public String name;
    public List<FieldDeclaration> parameters = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            if (child instanceof JavaParser.IdentifierContext) {
                this.name = child.getText();
            } else if (child instanceof JavaParser.MethodBodyContext) {
                for (int bi = 0, bc = child.getChildCount(); bi < bc; bi++) {
                    ParseTree bodyMember = child.getChild(bi);

                    if (bodyMember instanceof JavaParser.BlockContext) {
                        JavaParser.BlockContext blockContext = (JavaParser.BlockContext) bodyMember;
                        for (int si = 0, sc = blockContext.getChildCount(); si < sc; si++) {
                            ParseTree statementContext = blockContext.getChild(si);

                            if (statementContext instanceof JavaParser.BlockStatementContext) {
                                // The second one is ';'.
                                ParseTree firstChildOfStatement = statementContext.getChild(0);

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
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + this.name + "()");

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
