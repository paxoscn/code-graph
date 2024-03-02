/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public abstract class Control implements Statement {

    public Block block;

    public static class Builder {

        public static Statement build(JavaParser.StatementContext statementContext) {
            TerminalNodeImpl firstChild = (TerminalNodeImpl) statementContext.getChild(0);
            String firstChildText = firstChild.getText();

            switch (firstChildText) {
                case "return":
                    if (statementContext.getChild(1) instanceof TerminalNodeImpl) {
                        // 'return;'
                        return null;
                    } else {
                        return Expression.Builder.build((JavaParser.ExpressionContext) statementContext.getChild(1));
                    }

                case "for":
                    return Statement.Builder.build((JavaParser.StatementContext) statementContext);

                case "while":
                    return Statement.Builder.build((JavaParser.StatementContext) statementContext.getChild(2));

                case "if":
                    return Statement.Builder.build((JavaParser.StatementContext) statementContext.getChild(2));
            }

            return null;
        }
    }
}
