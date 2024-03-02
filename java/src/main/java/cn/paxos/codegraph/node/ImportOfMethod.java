/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.PrintStream;

public class ImportOfMethod implements Node {

    public String imported;

    @Override
    public void build(ParseTree parseTree) {
        this.imported = parseTree.getText();
    }

    @Override
    public void print(PrintStream printStream, String prefix) {
        printStream.println(prefix + "import static " + this.imported);
    }
}
