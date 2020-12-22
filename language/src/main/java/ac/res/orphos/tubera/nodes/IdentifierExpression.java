// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "id")
public class IdentifierExpression extends Expression {
    @CompilationFinal
    private Identifier id;
    @CompilationFinal
    private boolean local;
    @Child
    private ReadSlot readLocal;

    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        if (!local)
            ret.add(id);
        return ret;
    }

    public IdentifierExpression(Identifier id, boolean local) {
        this.id = id;
        this.local = local;
        this.readLocal = ReadSlot.create(id);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return readLocal.execute(frame);
    }

}