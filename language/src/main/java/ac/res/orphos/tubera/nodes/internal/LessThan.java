// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import ac.res.orphos.tubera.runtime.OrphosInteger;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class LessThan extends Infix {
    public static LessThan create() {
        return LessThanNodeGen.create();
    }

    @Specialization
    public boolean addLong(long left, long right) throws ArithmeticException {
        return left < right;
    }

    @Specialization
    @TruffleBoundary
    public boolean lessThan(OrphosInteger left, OrphosInteger right) {
        return left.toBigInteger().compareTo(right.toBigInteger()) < 0;
    }
}