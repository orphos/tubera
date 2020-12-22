// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.runtime;

import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Shape;

public class OrphosRecord extends DynamicObject {

    public OrphosRecord(Shape shape) {
        super(shape);
    }

}