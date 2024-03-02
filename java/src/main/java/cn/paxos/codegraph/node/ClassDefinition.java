/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import cn.paxos.codegraph.graph.DrawingContext;
import cn.paxos.codegraph.graph.Graph;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;
import java.util.*;

public class ClassDefinition implements Member {

    public String name;
    public List<Member> members = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        JavaParser.ClassDeclarationContext classDeclarationContext = (JavaParser.ClassDeclarationContext) parseTree;
        JavaParser.IdentifierContext classIdentifierContext = (JavaParser.IdentifierContext) classDeclarationContext.getChild(1);
        this.name = classIdentifierContext.getText();
        for (int di = 0, dc = classDeclarationContext.getChildCount(); di < dc; di++) {
            ParseTree childClassBody = classDeclarationContext.getChild(di);

            if (childClassBody instanceof JavaParser.ClassBodyContext) {
                JavaParser.ClassBodyContext classBodyContext = (JavaParser.ClassBodyContext) childClassBody;
                for (int ci = 0, cc = classBodyContext.getChildCount(); ci < cc; ci++) {
                    ParseTree child = classBodyContext.getChild(ci);

                    if (child instanceof JavaParser.ClassBodyDeclarationContext) {
                        JavaParser.ClassBodyDeclarationContext classBodyDeclarationContext = (JavaParser.ClassBodyDeclarationContext) child;

                        for (int mi = 0, mc = classBodyDeclarationContext.getChildCount(); mi < mc; mi++) {
                            ParseTree childMember = classBodyDeclarationContext.getChild(mi);

                            if (childMember instanceof JavaParser.MemberDeclarationContext) {
                                JavaParser.MemberDeclarationContext memberDeclarationContext = (JavaParser.MemberDeclarationContext) childMember;
                                ParseTree firstChild = memberDeclarationContext.getChild(0);
                                if (firstChild instanceof JavaParser.ConstructorDeclarationContext) {
                                    ConstructorDefinition constructorDefinition = new ConstructorDefinition();
                                    members.add(constructorDefinition);

                                    constructorDefinition.build(firstChild);
                                } else if (firstChild instanceof JavaParser.MethodDeclarationContext) {
                                    MethodDefinition methodDefinition = new MethodDefinition();
                                    members.add(methodDefinition);

                                    methodDefinition.build(firstChild);
                                } else if (firstChild instanceof JavaParser.ClassDeclarationContext) {
                                    ClassDefinition classDefinition = new ClassDefinition();
                                    members.add(classDefinition);

                                    classDefinition.build(firstChild);
                                } else if (firstChild instanceof JavaParser.FieldDeclarationContext) {
                                    FieldDeclaration fieldDeclaration = new FieldDeclaration();
                                    members.add(fieldDeclaration);

                                    fieldDeclaration.build(firstChild);
                                }
                            } else if (childMember instanceof JavaParser.BlockContext) {
                                JavaParser.BlockContext blockContext = (JavaParser.BlockContext) childMember;
                                Block block = new Block();
                                members.add(block);

                                block.build(blockContext);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + "class " + this.name + " {");

        for (Member member : members) {
            member.print(printStream, prefix + "    ");
        }

        printStream.println(prefix + "}");
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        String packageName = (String) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_PACKAGE);
        String className = packageName == null ? this.name : (packageName + "." + this.name);
        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_CLASS, className);

        Map<String, String> classToFQN = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN);
        Map<String, String> variableToClass = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS);
        classToFQN.put(this.name, className);
        variableToClass.put(this.name, className);

        drawingContext.attributes.putIfAbsent(DRAWING_CONTEXT_ATTRIBUTE_CLASSES_IN_PROJECT, new HashSet<String>());
        Set<String> classesInProject = (Set<String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASSES_IN_PROJECT);
        classesInProject.add(className);

        for (Member member : members) {
            member.draw(drawingContext, graph);
        }
    }
}
