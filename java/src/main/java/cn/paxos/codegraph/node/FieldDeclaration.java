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

public class FieldDeclaration implements Member, Statement {

    public String typeName;
    public List<Assignment> assignments = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            if (child instanceof JavaParser.TypeTypeContext) {
                JavaParser.TypeTypeContext typeTypeContext = (JavaParser.TypeTypeContext) child;
                this.typeName = typeTypeContext.getText();
            } else if (child instanceof JavaParser.VariableDeclaratorsContext) {
                JavaParser.VariableDeclaratorsContext variableDeclaratorsContext = (JavaParser.VariableDeclaratorsContext) child;

                for (int vi = 0, vc = variableDeclaratorsContext.getChildCount(); vi < vc; vi++) {
                    ParseTree childVariable = variableDeclaratorsContext.getChild(vi);

                    if (childVariable instanceof JavaParser.VariableDeclaratorContext) {
                        JavaParser.VariableDeclaratorContext variableDeclaratorContext = (JavaParser.VariableDeclaratorContext) childVariable;
                        Assignment assignment = new Assignment();
                        assignments.add(assignment);

                        assignment.build(variableDeclaratorContext);
                    }
                }
            } else if (child instanceof JavaParser.VariableDeclaratorIdContext) {
                JavaParser.VariableDeclaratorIdContext variableDeclaratorIdContext = (JavaParser.VariableDeclaratorIdContext) child;

                Assignment assignment = new Assignment();
                assignments.add(assignment);

                assignment.variableName = variableDeclaratorIdContext.getText();
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + this.typeName);

        for (Assignment assignment : assignments) {
            assignment.print(printStream, prefix + "    ");
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        Map<String, String> classToFQN = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN);
        String typeFQN = classToFQN.get(this.typeName);
        if (typeFQN == null) {
            String packageName = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_PACKAGE);
            typeFQN = packageName + "." + this.typeName;
        }

        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TYPE, typeFQN);

        for (Assignment assignment : assignments) {
            assignment.draw(drawingContext, graph);
        }
    }
}
