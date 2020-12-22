// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.math.BigInteger;
import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public class IntegerLiteral extends Expression {
    @CompilationFinal
    private BigInteger value;
    @CompilationFinal
    private long longValue;
    @CompilationFinal
    private boolean isLong = false;

    public IntegerLiteral(BigInteger value) {
        this.value = value;
        try {
            longValue = value.longValueExact();
            isLong = true;
        } catch (ArithmeticException e) {
        }
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (isLong)
            return longValue;
        return value;
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        return new HashSet<>();
    }
}