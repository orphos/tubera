// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "if")
public class If extends Expression {
    @Child
    private Expression cond;
    @Child
    private Expression target;
    @Child
    private Expression fallback;

    public If(Expression cond, Expression target, Expression fallback) {
        this.cond = cond;
        this.target = target;
        this.fallback = fallback;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if ((boolean) cond.execute(frame))
            return target.execute(frame);
        return fallback.execute(frame);
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(cond.getReferencedVarIds());
        ret.addAll(target.getReferencedVarIds());
        ret.addAll(fallback.getReferencedVarIds());
        return ret;
    }

    @Override
    public void markTail() {
        super.markTail();
        target.markTail();
        fallback.markTail();
    }

}