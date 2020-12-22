// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.runtime;

import com.oracle.truffle.api.dsl.TypeSystem;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

@TypeSystem({ long.class })
public class OrphosRuntimeTypes {
    @ImplicitCast
    @TruffleBoundary
    public static OrphosInteger asInteger(long value) {
        return new OrphosInteger(value);
    }
}