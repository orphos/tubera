// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class Eq extends Infix {
    public static Eq create() {
        return EqNodeGen.create();
    }

    @Specialization
    public boolean eq(long left, long right) throws ArithmeticException {
        return left == right;
    }

    @Fallback
    @TruffleBoundary
    public boolean eq(Object left, Object right) {
        return left.equals(right);
    }
}