// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.runtime;

import ac.res.orphos.tubera.nodes.Function;
import com.oracle.truffle.api.nodes.ControlFlowException;

public class TailCallException extends ControlFlowException {
    private static final long serialVersionUID = 1L;
    public final Function function;
    public final Object argument;

    public TailCallException(Function function, Object argument) {
        this.function = function;
        this.argument = argument;
    }
}