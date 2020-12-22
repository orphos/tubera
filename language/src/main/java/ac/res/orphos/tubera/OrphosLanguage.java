// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera;

import ac.res.orphos.tubera.nodes.Expression;
import ac.res.orphos.tubera.nodes.Function;
import ac.res.orphos.tubera.parser.NodeBuildingVisitor;
import ac.res.orphos.tubera.runtime.Identifier;
import ac.res.orphos.tubera.runtime.OrphosContext;
import ac.res.orphos.tubera.runtime.OrphosRecord;
import ac.res.orphos.tubera.OrphosParser;
import ac.res.orphos.tubera.OrphosLexer;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.source.Source;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

@TruffleLanguage.Registration(id = OrphosLanguage.ID, name = "Orphos", defaultMimeType = OrphosLanguage.MIME_TYPE, characterMimeTypes = OrphosLanguage.MIME_TYPE, fileTypeDetectors = OrphosDetector.class)
public final class OrphosLanguage extends TruffleLanguage<OrphosContext> {
    public static final String ID = "orphos";
    public static final String MIME_TYPE = "application/x-orphos";

    private final Shape initialRecordShape;

    public OrphosLanguage() {
        initialRecordShape = Shape.newBuilder()/* .layout(ExtendedOrphosRecord.class) */.build();
    }

    @Override
    protected OrphosContext createContext(Env env) {
        return new OrphosContext();
    }

    public OrphosRecord newRecordObject() {
        return new OrphosRecord(initialRecordShape);
    }

    protected CallTarget parse(ParsingRequest request) throws Exception {
        Source src = request.getSource();
        OrphosLexer lexer = new OrphosLexer(CharStreams.fromString(src.getCharacters().toString()));
        OrphosParser parser = new OrphosParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.compilationUnit();
        Function rootNode = new Function(null, new Identifier[0], (Expression) new NodeBuildingVisitor().visit(tree),
                this, Truffle.getRuntime().createMaterializedFrame(new Object[0]), true);
        return rootNode.getCallTarget();
    }
}