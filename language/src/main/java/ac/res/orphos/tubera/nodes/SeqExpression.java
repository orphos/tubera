// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.BlockNode;

public class SeqExpression extends Expression implements BlockNode.ElementExecutor<Expression> {
    @Child
    private BlockNode<Expression> blockNode;

    private Expression[] children;

    public SeqExpression(Expression[] children) {
        this.blockNode = BlockNode.create(children, this);
        this.children = children;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        prologue(frame);
        return epilogue(frame, blockNode.executeGeneric(frame, 0));
    }

    @Override
    public void executeVoid(VirtualFrame frame, Expression node, int index, int argument) {
        handleResult(frame, index, executeGeneric(frame, node, index, argument));
    }

    @Override
    public Object executeGeneric(VirtualFrame frame, Expression node, int index, int argument) {
        return handleResult(frame, index, node.execute(frame));
    }

    protected void prologue(VirtualFrame frame) {
    }

    protected Object epilogue(VirtualFrame frame, Object result) {
        return result;
    }

    protected Object handleResult(VirtualFrame frame, int index, Object result) {
        return result;
    }

    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        for (Expression child : children)
            ret.addAll(child.getReferencedVarIds());
        return ret;
    }

}