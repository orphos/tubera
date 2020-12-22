// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import ac.res.orphos.tubera.runtime.OrphosRecord;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;

@NodeChild("target")
@NodeField(type = String.class, name = "label")
public abstract class ReadRecordField extends Expression {
    public static ReadRecordField create(Expression target, String label) {
        return ReadRecordFieldNodeGen.create(target, label);
    }

    public abstract String getLabel();

    @Specialization(limit = "4")
    public Object doRead(OrphosRecord target, @CachedLibrary("target") DynamicObjectLibrary objectLibrary) {
        return objectLibrary.getOrDefault(target, getLabel(), 0);
    }

    public HashSet<Identifier> getReferencedVarIds() {
        return null;
    }

}