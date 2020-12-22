// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.Arrays;

import ac.res.orphos.tubera.nodes.internal.WriteRecordField;
import ac.res.orphos.tubera.runtime.OrphosRecord;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;

public class NewRecord extends SeqExpression {
    @CompilationFinal(dimensions = 1)
    private final String[] labels;
    @Children
    private final WriteRecordField[] writers;

    public String[] getLabels() {
        return labels;
    }

    public NewRecord(String[] labels, Expression[] fields) {
        super(fields);
        this.labels = labels;
        writers = Arrays.asList(labels).stream().map(WriteRecordField::create).toArray(WriteRecordField[]::new);
    }

    @Override
    protected void prologue(VirtualFrame frame) {
        setState(frame, getLanguage().newRecordObject());
    }

    @Override
    protected Object handleResult(VirtualFrame frame, int index, Object result) {
        writers[index].execute(frame, this.<OrphosRecord> getState(frame), result);
        return result;
    }

    @Override
    protected Object epilogue(VirtualFrame frame, Object result) {
        return getState(frame);
    }
}