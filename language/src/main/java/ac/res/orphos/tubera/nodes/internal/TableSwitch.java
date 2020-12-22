// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import ac.res.orphos.tubera.nodes.Expression;

public class TableSwitch extends Switch {
    @CompilationFinal
    private int startIndex;

    public TableSwitch(Expression expr, Expression[] targets, int startIndex) {
        super(expr, targets);
        this.startIndex = startIndex;
    }

    @Override
    protected int computeTargetIndex(Object value) {
        int targetIndex = (int) value - startIndex;
        return 0 <= targetIndex && targetIndex < targets.length ? targetIndex : targets.length - 1;
    }
}