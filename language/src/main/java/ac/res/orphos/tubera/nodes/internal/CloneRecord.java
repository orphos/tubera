// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import ac.res.orphos.tubera.runtime.OrphosRecord;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

import ac.res.orphos.tubera.nodes.OrphosNode;

public abstract class CloneRecord extends OrphosNode {
    public static CloneRecord create() {
        return CloneRecordNodeGen.create();
    }

    public abstract Object executeClone(VirtualFrame frame, OrphosRecord source);

    @Specialization(limit = "4")
    public Object doClone(VirtualFrame frame, OrphosRecord source,
            @CachedLibrary("source") DynamicObjectLibrary objectLibrary) {
        OrphosRecord ret = getLanguage().newRecordObject();
        for (Object key : objectLibrary.getKeyArray(source)) {
            objectLibrary.put(ret, key, objectLibrary.getOrDefault(source, key, null));
        }
        return ret;
    }

}