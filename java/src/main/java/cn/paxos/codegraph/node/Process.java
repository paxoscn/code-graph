/*
 * Copyright (c) 2024, StartDT and/or its affiliates. All rights reserved.
 */
package cn.paxos.codegraph.node;

import java.util.LinkedList;
import java.util.List;

public abstract class Process implements Member {

    public List<Statement> statements = new LinkedList<>();
}
