// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public class LetExpression extends Expression {
    @CompilationFinal
    private boolean rec;
    @CompilationFinal
    private Identifier binder;
    @Child
    private Expression bindant;
    @Child
    private WriteSlot writeLocal;
    @Child
    private Expression target;

    @Child
    private WriteSlot writeRec;

    public LetExpression(boolean rec, Identifier binder, Expression bindant, Expression target) {
        this.rec = rec;
        this.binder = binder;
        this.bindant = bindant;
        this.target = target;
        this.writeLocal = WriteSlot.create(binder);
        this.writeRec = WriteSlot.create(binder);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object value = bindant.execute(frame);
        writeLocal.executeWrite(frame, value);
        if (rec && value instanceof Function)
            writeRec.executeWrite(((Function) value).getFreeVarFrame(), value);
        return target.execute(frame);
        // TODO: clear local
    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        HashSet<Identifier> ret = new HashSet<>();
        ret.addAll(target.getReferencedVarIds());
        ret.remove(binder);
        ret.addAll(bindant.getReferencedVarIds());
        if (rec)
            ret.remove(binder);
        return ret;
    }

    @Override
    public void markTail() {
        super.markTail();
        target.markTail();
    }

}