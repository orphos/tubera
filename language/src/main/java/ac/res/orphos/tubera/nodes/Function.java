// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.library.ExportMessage.Ignore;
import com.oracle.truffle.api.nodes.RootNode;

import ac.res.orphos.tubera.OrphosLanguage;

@ExportLibrary(InteropLibrary.class)
public class Function extends RootNode implements TruffleObject {
    @Child
    WriteSlot writeArgument;
    @Children
    ReadSlot[] readFreeVars;
    @Children
    WriteSlot[] writeFreeVars;
    @Child
    Expression body;
    @CompilationFinal
    private MaterializedFrame freeVars;

    public Function(Identifier argId, Identifier[] freeVarIds, Expression body, OrphosLanguage lang,
            MaterializedFrame freeVars, boolean notail) {
        super(lang);
        this.freeVars = freeVars;
        writeArgument = argId == null ? null : WriteSlot.create(argId);
        readFreeVars = new ReadSlot[freeVarIds.length];
        writeFreeVars = new WriteSlot[freeVarIds.length];
        for (int i = 0; i < freeVarIds.length; ++i) {
            readFreeVars[i] = ReadSlot.create(freeVarIds[i]);
            writeFreeVars[i] = WriteSlot.create(freeVarIds[i]);
        }
        this.body = body;
        if (!notail)
            body.markTail();
        setCallTarget(Truffle.getRuntime().createCallTarget(this));
    }

    @Ignore // do not export for interop
    public Object execute(VirtualFrame frame) {
        try {
            for (int i = 0; i < writeFreeVars.length; ++i)
                writeFreeVars[i].executeWrite(frame, readFreeVars[i].execute(freeVars));
            if (writeArgument != null)
                writeArgument.executeWrite(frame, frame.getArguments()[0]);
            return body.execute(frame);
        } catch (Throwable e) {
            e.printStackTrace(); // for debugging
            throw e;
        }
    }

    @ExportMessage
    public boolean isExecutable() {
        return true;
    }

    @ExportMessage
    public Object execute(Object... arguments)
            throws UnsupportedTypeException, ArityException, UnsupportedMessageException {
        return getCallTarget().call(arguments);
    }

    public MaterializedFrame getFreeVarFrame() {
        return freeVars;
    }

}