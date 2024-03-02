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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Source implements Node {

    public String packageName;
    public List<ImportOfClass> importsOfClass = new LinkedList<>();
    public List<ImportOfWildcardClasses> importsOfWildcardClasses = new LinkedList<>();
    public List<ImportOfMethod> importsOfMethod = new LinkedList<>();
    public List<ImportOfWildcardMethods> importsOfWildcardMethods = new LinkedList<>();
    public List<ClassDefinition> classDefinitions = new LinkedList<>();

    @Override
    public void build(ParseTree parseTree) {
        for (int i = 0, c = parseTree.getChildCount(); i < c; i++) {
            ParseTree child = parseTree.getChild(i);

            if (child instanceof JavaParser.PackageDeclarationContext) {
                JavaParser.PackageDeclarationContext packageDeclarationContext = (JavaParser.PackageDeclarationContext) child;
                this.packageName = packageDeclarationContext.getChild(1).getText();
            } else if (child instanceof JavaParser.ImportDeclarationContext) {
                JavaParser.ImportDeclarationContext importDeclarationContext = (JavaParser.ImportDeclarationContext) child;
                if (importDeclarationContext.getChild(1) instanceof TerminalNodeImpl) {
                    if (importDeclarationContext.getChildCount() == 4) {
                        ImportOfMethod importOfMethod = new ImportOfMethod();
                        importsOfMethod.add(importOfMethod);

                        importOfMethod.build(importDeclarationContext.getChild(2));
                    } else {
                        ImportOfWildcardMethods importOfWildcardMethods = new ImportOfWildcardMethods();
                        importsOfWildcardMethods.add(importOfWildcardMethods);

                        importOfWildcardMethods.build(importDeclarationContext.getChild(2));
                    }
                } else {
                    if (importDeclarationContext.getChildCount() == 3) {
                        ImportOfClass importOfClass = new ImportOfClass();
                        importsOfClass.add(importOfClass);

                        importOfClass.build(importDeclarationContext.getChild(1));
                    } else {
                        ImportOfWildcardClasses importOfWildcardClasses = new ImportOfWildcardClasses();
                        importsOfWildcardClasses.add(importOfWildcardClasses);

                        importOfWildcardClasses.build(importDeclarationContext.getChild(1));
                    }
                }
            } else if (child instanceof JavaParser.TypeDeclarationContext) {
                for (int ci = 0, cc = child.getChildCount(); ci < cc; ci++) {
                    ParseTree grandchild = child.getChild(ci);

                    if (grandchild instanceof JavaParser.ClassDeclarationContext) {
                        ClassDefinition classDefinition = new ClassDefinition();
                        classDefinitions.add(classDefinition);

                        classDefinition.build(grandchild);
                    }
                }
            }
        }
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        for (ImportOfClass importOfClass : importsOfClass) {
            importOfClass.print(printStream, prefix);
        }

        for (ImportOfWildcardClasses importOfWildcardClasses : importsOfWildcardClasses) {
            importOfWildcardClasses.print(printStream, prefix);
        }

        for (ImportOfMethod importOfMethod : importsOfMethod) {
            importOfMethod.print(printStream, prefix);
        }

        for (ImportOfWildcardMethods importOfWildcardMethods : importsOfWildcardMethods) {
            importOfWildcardMethods.print(printStream, prefix);
        }

        for (ClassDefinition classDefinition : classDefinitions) {
            classDefinition.print(printStream, prefix);
        }
    }

    @Override
    public void draw(DrawingContext drawingContext, Graph graph) {
        drawingContext.attributes.put(DRAWING_CONTEXT_ATTRIBUTE_PACKAGE, this.packageName);
        drawingContext.attributes.putIfAbsent(DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN, new HashMap<String, String>());
        drawingContext.attributes.putIfAbsent(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS, new HashMap<String, String>());
        Map<String, String> classToFQN = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_CLASS_TO_FQN);
        Map<String, String> variableToClass = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_VARIABLE_TO_CLASS);
        for (ImportOfClass importOfClass : importsOfClass) {
            classToFQN.put(importOfClass.imported.replaceFirst(".*\\.", ""), importOfClass.imported);

            variableToClass.put(importOfClass.imported.replaceFirst(".*\\.", ""), importOfClass.imported);
        }

        drawingContext.attributes.putIfAbsent(DRAWING_CONTEXT_ATTRIBUTE_IMPORTED_METHOD_TO_CLASS, new HashMap<String, String>());
        Map<String, String> importedMethodToClass = (Map<String, String>) drawingContext.attributes.get(DRAWING_CONTEXT_ATTRIBUTE_IMPORTED_METHOD_TO_CLASS);
        for (ImportOfMethod importOfMethod : importsOfMethod) {
            importedMethodToClass.put(importOfMethod.imported.replaceFirst(".*\\.", ""), importOfMethod.imported.replaceFirst("\\.([^.+])$", ""));
        }

        for (ClassDefinition classDefinition : classDefinitions) {
            classDefinition.draw(drawingContext, graph);
        }
    }
}
