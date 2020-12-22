// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "identifier", type = Identifier.class)
public abstract class ReadSlot extends OrphosNode {
    public static ReadSlot create(Identifier identifier) {
        return ReadSlotNodeGen.create(identifier);
    }

    public abstract Identifier getIdentifier();

    public abstract Object execute(VirtualFrame frame);

    protected FrameSlot getSlot() {
        return slot = getSlot(getIdentifier(), slot);
    }

    @CompilationFinal
    private FrameSlot slot = null;

    @Specialization(guards = "frame.isLong(getSlot())")
    public long readLong(VirtualFrame frame) {
        return FrameUtil.getLongSafe(frame, getSlot());
    }

    @Specialization(guards = "frame.isInt(getSlot())")
    public int readInt(VirtualFrame frame) {
        return FrameUtil.getIntSafe(frame, getSlot());
    }

    @Specialization(guards = "frame.isByte(getSlot())")
    public byte readByte(VirtualFrame frame) {
        return FrameUtil.getByteSafe(frame, getSlot());
    }

    @Specialization(guards = "frame.isDouble(getSlot())")
    public double readDouble(VirtualFrame frame) {
        return FrameUtil.getDoubleSafe(frame, getSlot());
    }

    @Specialization(guards = "frame.isBoolean(getSlot())")
    public boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, getSlot());
    }

    @Specialization(replaces = { "readLong", "readInt", "readByte", "readDouble", "readBoolean" })
    public Object read(VirtualFrame frame) {
        return FrameUtil.getObjectSafe(frame, getSlot());
    }

}