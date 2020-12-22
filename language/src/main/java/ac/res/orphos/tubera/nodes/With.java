// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import ac.res.orphos.tubera.nodes.internal.CloneRecord;
import ac.res.orphos.tubera.runtime.OrphosRecord;
import com.oracle.truffle.api.frame.VirtualFrame;

public class With extends NewRecord {
    @Child
    private Expression source;
    @Child
    private CloneRecord cloner = CloneRecord.create();

    public With(Expression source, String[] labels, Expression[] values) {
        super(labels, values);
        this.source = source;
    }

    @Override
    public void prologue(VirtualFrame frame) {
        setState(frame, cloner.executeClone(frame, (OrphosRecord) source.execute(frame)));
    }
}