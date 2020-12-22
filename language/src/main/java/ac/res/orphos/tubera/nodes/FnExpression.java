// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "fn")
public class FnExpression extends Expression {
    @CompilationFinal
    private Identifier argId;
    @CompilationFinal(dimensions = 1)
    private Identifier[] freeVarIds;
    @Children
    private ReadSlot[] readFreeVars;
    @Children
    private WriteSlot[] writeFreeVars;
    @CompilationFinal
    private Expression body;

    @CompilationFinal
    private FrameDescriptor freeVarsFD = null;

    public FnExpression(Identifier argId, Expression body) {
        this.argId = argId;
        this.body = body;
        freeVarIds = getReferencedVarIds().toArray(Identifier[]::new);
        readFreeVars = new ReadSlot[freeVarIds.length];
        writeFreeVars = new WriteSlot[freeVarIds.length];
        for (int i = 0; i < freeVarIds.length; ++i) {
            readFreeVars[i] = ReadSlot.create(freeVarIds[i]);
            writeFreeVars[i] = WriteSlot.create(freeVarIds[i]);
        }
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (freeVarsFD == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            freeVarsFD = new FrameDescriptor();
        }
        MaterializedFrame freeVars = Truffle.getRuntime().createMaterializedFrame(new Object[0], freeVarsFD);
        for (int i = 0; i < freeVarIds.length; ++i)
            writeFreeVars[i].executeWrite(freeVars, readFreeVars[i].execute(frame));
        return new Function(argId, freeVarIds, body, getLanguage(), freeVars, false);
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        for (Identifier freeVarId : body.getReferencedVarIds())
            if (argId != freeVarId)
                ret.add(freeVarId);
        return ret;
    }

}