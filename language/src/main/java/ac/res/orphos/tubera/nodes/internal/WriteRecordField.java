// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import ac.res.orphos.tubera.runtime.OrphosRecord;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

import ac.res.orphos.tubera.nodes.OrphosNode;

@NodeField(name = "label", type = String.class)
public abstract class WriteRecordField extends OrphosNode {
    public static WriteRecordField create(String label) {
        return WriteRecordFieldNodeGen.create(label);
    }

    public abstract void execute(VirtualFrame frame, OrphosRecord target, Object value);

    public abstract String getLabel();

    @Specialization(limit = "4")
    public void doWrite(VirtualFrame frame, OrphosRecord target, Object value,
            @CachedLibrary("target") DynamicObjectLibrary objectLibrary) {
        objectLibrary.put(target, getLabel(), value);
    }

}