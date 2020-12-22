// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.nodes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ac.res.orphos.tubera.runtime.Identifier;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.BlockNode;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "match")
public class MatchExpression extends Expression implements BlockNode.ElementExecutor<MatchClause> {

    private final Map<List<Object>, Identifier> cacheVars = new HashMap<>();

    // private final MatchClause[] clauses;

    @Override
    public Object execute(VirtualFrame frame) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void executeVoid(VirtualFrame frame, MatchClause node, int index, int argument) {
        // TODO Auto-generated method stub

    }

    @Override
    public HashSet<Identifier> getReferencedVarIds() {
        // TODO Auto-generated method stub
        return null;
    }

}