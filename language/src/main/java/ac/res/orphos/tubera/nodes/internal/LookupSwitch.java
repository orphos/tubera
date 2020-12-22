// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import java.util.Arrays;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import ac.res.orphos.tubera.nodes.Expression;

public class LookupSwitch extends Switch {
    @CompilationFinal(dimensions = 1)
    private int[] indices;

    public LookupSwitch(Expression expr, Expression[] targets, int[] indices) {
        super(expr, targets);
        this.indices = indices;
    }

    @Override
    protected int computeTargetIndex(Object value) {
        int targetIndex = Arrays.binarySearch(indices, (int) value);
        return targetIndex >= 0 ? targetIndex : targets.length - 1;
    }
}