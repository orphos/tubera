// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class Expression extends OrphosNode {
    public abstract Object execute(VirtualFrame frame);

    public abstract HashSet<Identifier> getReferencedVarIds();

    @CompilationFinal
    protected boolean isTail = false;

    public void markTail() {
        if (isTail)
            return;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        isTail = true;
    }
}