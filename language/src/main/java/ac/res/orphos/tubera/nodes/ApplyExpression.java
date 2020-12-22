// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.NodeInfo;

import ac.res.orphos.tubera.runtime.TailCallException;

@NodeInfo(shortName = "apply")
public class ApplyExpression extends Expression {
    @Child
    private Expression function;
    @Child
    private Expression argument;

    @Child
    private IndirectCallNode callSite = IndirectCallNode.create();

    public ApplyExpression(Expression function, Expression argument) {
        this.function = function;
        this.argument = argument;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object value = argument.execute(frame);
        Function f = (Function) function.execute(frame);
        if (isTail)
            throw new TailCallException(f, value);
        while (true) {
            try {
                return callSite.call(f.getCallTarget(), value);
            } catch (TailCallException e) {
                f = e.function;
                value = e.argument;
            }
        }
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(function.getReferencedVarIds());
        ret.addAll(argument.getReferencedVarIds());
        return ret;
    }

}