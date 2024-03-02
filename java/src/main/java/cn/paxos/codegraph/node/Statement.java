/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public interface Statement extends Node {

    class Builder {

        public static Statement build(JavaParser.StatementContext statementContext) {
            if (statementContext.getChildCount() > 2 && statementContext.getChild(2) instanceof JavaParser.ForControlContext) {
                Block block = new Block();

                JavaParser.ForControlContext forControlContext = (JavaParser.ForControlContext) statementContext.getChild(2);

                if (forControlContext.getChild(0) instanceof JavaParser.EnhancedForControlContext) {
                    JavaParser.EnhancedForControlContext enhancedForControlContext = (JavaParser.EnhancedForControlContext) forControlContext.getChild(0);

                    if (enhancedForControlContext.getChild(0) instanceof JavaParser.TypeTypeContext) {
                        FieldDeclaration fieldDeclaration = new FieldDeclaration();
                        block.statements.add(fieldDeclaration);

                        fieldDeclaration.build(enhancedForControlContext);
                    }
                }

                ParseTree firstChild = statementContext.getChild(4).getChild(0);

                if (firstChild instanceof JavaParser.BlockContext) {
                    block.build(firstChild);
                } else if (firstChild instanceof JavaParser.ExpressionContext) {
                    Expression expression = Expression.Builder.build((JavaParser.ExpressionContext) firstChild);

                    block.statements.add(expression);
                }

                return block;
            } if (statementContext.getChild(0) instanceof TerminalNodeImpl) {
                return Control.Builder.build(statementContext);
            } else {
                ParseTree firstChild = statementContext.getChild(0);

                if (firstChild instanceof JavaParser.BlockContext) {
                    Block block = new Block();
                    block.build(firstChild);

                    return block;
                } else if (firstChild instanceof JavaParser.ExpressionContext) {
                    return Expression.Builder.build((JavaParser.ExpressionContext) firstChild);
                }
            }

            return null;
        }
    }
}
