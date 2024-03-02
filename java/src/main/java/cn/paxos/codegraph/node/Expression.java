/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import cn.paxos.codegraph.generated.JavaParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

public interface Expression extends Statement {

    class Builder {

        public static Expression build(JavaParser.ExpressionContext expressionContext) {
            if (expressionContext.getChildCount() == 1) {
                ParseTree onlyChild = expressionContext.getChild(0);
                if (onlyChild instanceof JavaParser.ExpressionContext) {
                    return build((JavaParser.ExpressionContext) onlyChild);
                } else if (onlyChild instanceof JavaParser.PrimaryContext) {
                    JavaParser.PrimaryContext primaryContext = (JavaParser.PrimaryContext) onlyChild;
                    if (primaryContext.getChildCount() == 1 && primaryContext.getChild(0) instanceof JavaParser.IdentifierContext) {
                        Reference reference = new Reference();
                        reference.build(primaryContext.getChild(0));

                        return reference;
                    } else if (primaryContext.getChildCount() == 3) {
                        if (primaryContext.getChild(1) instanceof TerminalNodeImpl) {
                            // 'Xxx.class'
                            return null;
                        } else {
                            return build((JavaParser.ExpressionContext) primaryContext.getChild(1));
                        }
                    }
                } else if (onlyChild instanceof JavaParser.MethodCallContext) {
                    JavaParser.MethodCallContext methodCallContext = (JavaParser.MethodCallContext) onlyChild;
                    MethodInvoking methodInvoking = new MethodInvoking();
                    methodInvoking.build(methodCallContext);

                    return methodInvoking;
                }
            } else if (expressionContext.getChildCount() == 2
                    && expressionContext.getChild(0) instanceof TerminalNodeImpl
                    && expressionContext.getChild(0).getText().equals("new")) {
                ConstructorInvoking constructorInvoking = new ConstructorInvoking();
                constructorInvoking.build(expressionContext.getChild(1));

                return constructorInvoking;
            } else if (expressionContext.getChildCount() == 3
                    && expressionContext.getChild(1) instanceof TerminalNodeImpl) {
                if (expressionContext.getChild(1).getText().equals(".")) {
                    if (expressionContext.getChild(2) instanceof JavaParser.MethodCallContext) {
                        MethodInvoking methodInvoking = new MethodInvoking();
                        methodInvoking.build(expressionContext);

                        return methodInvoking;
                    } else {
                        Reference reference = new Reference();
                        reference.build(expressionContext);

                        return reference;
                    }
                } else if (expressionContext.getChild(1).getText().equals("::")) {
                    MethodInvoking methodInvoking = new MethodInvoking();
                    methodInvoking.build(expressionContext);

                    return methodInvoking;
                } else {
                    Calculation calculation = new Calculation();
                    calculation.build(expressionContext);

                    return calculation;
                }
            }

            return null;
        }
    }

}
