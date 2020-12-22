// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import java.util.HashSet;

import ac.res.orphos.tubera.nodes.Expression;
import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class Switch extends Expression {
    @Child
    private Expression expr;
    @Children
    protected Expression[] targets;

    public Switch(Expression expr, Expression[] targets) {
        this.expr = expr;
        this.targets = targets;
    }

    protected abstract int computeTargetIndex(Object value);

    @Override
    public Object execute(VirtualFrame frame) {
        Expression target = targets[computeTargetIndex(expr.execute(frame))];
        return target.execute(frame);
    }

    @Override
    public void markTail() {
        super.markTail();
        for (Expression target : targets)
            target.markTail();
    }

    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(expr.getReferencedVarIds());
        for (Expression target : targets)
            ret.addAll(target.getReferencedVarIds());
        return ret;
    }
}