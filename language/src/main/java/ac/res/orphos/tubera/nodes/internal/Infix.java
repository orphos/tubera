// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes.internal;

import com.oracle.truffle.api.frame.VirtualFrame;

import ac.res.orphos.tubera.nodes.OrphosNode;

public abstract class Infix extends OrphosNode {
    public abstract Object execute(VirtualFrame frame, Object left, Object right);

    public static Infix create(String op) {
        switch (op) {
        case "+":
            return Add.create();
        case "-":
            return Subtract.create();
        case "==":
            return Eq.create();
        case "<":
            return LessThan.create();
        default:
            throw new IllegalArgumentException("Unsupported operator");
        }
    }
}