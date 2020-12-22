// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import ac.res.orphos.tubera.runtime.Identifier;
import ac.res.orphos.tubera.runtime.OrphosUnit;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.HashSet;

public class UnitExpression extends Expression {
    @Override
    public Object execute(VirtualFrame frame) {
        return OrphosUnit.INSTANCE;
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        return new HashSet<>();
    }
}
