// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashSet;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class Pattern extends Expression {
    public static class TuplePattern extends Pattern {
        private final Identifier target;
        private final Identifier[] captures;
        private FrameSlot targetSlot;
        private FrameSlot[] captureSlots;

        public TuplePattern(Identifier target, Identifier[] captures) {
            this.target = target;
            this.captures = captures;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            try {
                Object t = frame.getObject(targetSlot);

            } catch (FrameSlotTypeException e) {
                throw new AssertionError();
            }
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public HashSet<Identifier> getReferencedVarIds() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}