// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import ac.res.orphos.tubera.runtime.OrphosInteger;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class Subtract extends Infix {
    public static Subtract create() {
        return SubtractNodeGen.create();
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    public long addLong(long left, long right) throws ArithmeticException {
        return Math.subtractExact(left, right);
    }

    @Specialization
    @TruffleBoundary
    public OrphosInteger addBigInteger(OrphosInteger left, OrphosInteger right) {
        return new OrphosInteger(left.toBigInteger().subtract(right.toBigInteger()));
    }
}