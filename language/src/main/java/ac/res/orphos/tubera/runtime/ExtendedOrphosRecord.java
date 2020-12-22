// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.runtime;

import com.oracle.truffle.api.object.Shape;

public class ExtendedOrphosRecord extends OrphosRecord {
    @DynamicField
    private Object _obj0;
    @DynamicField
    private Object _obj1;
    @DynamicField
    private Object _obj2;
    @DynamicField
    private long _long0;
    @DynamicField
    private long _long1;
    @DynamicField
    private long _long2;

    public ExtendedOrphosRecord(Shape shape) {
        super(shape);
    }

}