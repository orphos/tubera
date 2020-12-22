// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.orphos.tubera.nodes.internal.Infix;

public class InfixExpression extends Expression {
    @Child
    private Expression left;
    @Child
    private Infix op;
    @Child
    private Expression right;

    public InfixExpression(Expression left, Infix op, Expression right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object r = right.execute(frame);
        Object l = left.execute(frame);
        return op.execute(frame, l, r);
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(left.getReferencedVarIds());
        ret.addAll(right.getReferencedVarIds());
        return ret;
    }

}