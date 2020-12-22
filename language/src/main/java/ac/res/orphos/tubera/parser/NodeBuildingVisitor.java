// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.parser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;

import ac.res.orphos.tubera.OrphosParser;
import ac.res.orphos.tubera.nodes.*;
import ac.res.orphos.tubera.runtime.Identifier;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import ac.res.orphos.tubera.OrphosParser.AddPrecedenceExpressionContext;
import ac.res.orphos.tubera.OrphosParser.AliasTypeDeclBodyContext;
import ac.res.orphos.tubera.OrphosParser.ApplicationExpressionContext;
import ac.res.orphos.tubera.OrphosParser.BooleanExpressionContext;
import ac.res.orphos.tubera.OrphosParser.CompilationUnitContext;
import ac.res.orphos.tubera.OrphosParser.DotExpressionContext;
import ac.res.orphos.tubera.OrphosParser.EqPrecedenceExpressionContext;
import ac.res.orphos.tubera.OrphosParser.ExpressionContext;
import ac.res.orphos.tubera.OrphosParser.FloatExpressionContext;
import ac.res.orphos.tubera.OrphosParser.FnExpressionContext;
import ac.res.orphos.tubera.OrphosParser.IdentifierExpressionContext;
import ac.res.orphos.tubera.OrphosParser.IfExpressionContext;
import ac.res.orphos.tubera.OrphosParser.IntegerExpressionContext;
import ac.res.orphos.tubera.OrphosParser.LetExpressionContext;
import ac.res.orphos.tubera.OrphosParser.LogicalExpressionContext;
import ac.res.orphos.tubera.OrphosParser.MulPrecedenceExpressionContext;
import ac.res.orphos.tubera.OrphosParser.ParenExpressionContext;
import ac.res.orphos.tubera.OrphosParser.RecordExpressionContext;
import ac.res.orphos.tubera.OrphosParser.SingleExpressionContext;
import ac.res.orphos.tubera.OrphosParser.StringExpressionContext;
import ac.res.orphos.tubera.OrphosParser.TupleExpressionContext;
import ac.res.orphos.tubera.OrphosParser.TypeDeclContext;
import ac.res.orphos.tubera.OrphosParser.TypeDeclExpressionContext;
import ac.res.orphos.tubera.OrphosParser.TypeExpressionContext;
import ac.res.orphos.tubera.OrphosParser.UnitExpressionContext;
import ac.res.orphos.tubera.OrphosParser.VaritnaTypeDeclBodyContext;
import ac.res.orphos.tubera.OrphosParserVisitor;
import ac.res.orphos.tubera.nodes.internal.Infix;

public class NodeBuildingVisitor extends AbstractParseTreeVisitor<OrphosNode>
        implements OrphosParserVisitor<OrphosNode> {

    private final LinkedList<HashMap<String, Identifier>> scopes;

    public NodeBuildingVisitor() {
        scopes = new LinkedList<>();
        scopes.push(new HashMap<>());
    }

    private HashMap<String, Identifier> enterScope() {
        HashMap<String, Identifier> current = scopes.getFirst();
        scopes.push(new HashMap<>());
        return current;
    }

    private void leaveScope() {
        scopes.pop();
    }

    private Identifier searchLocal(String name) {
        return scopes.getFirst().get(name);
    }

    private Identifier search(String name) {
        for (HashMap<String, Identifier> scope : scopes) {
            Identifier ret = scope.get(name);
            if (ret != null)
                return ret;
        }
        return null;
    }

    private void put(Identifier id) {
        scopes.getFirst().put(id.getName(), id);
    }

    public RuntimeException newPanic() {
        return new RuntimeException();
    }

    @Override
    public OrphosNode visitApplicationExpression(ApplicationExpressionContext ctx) {
        return new ApplyExpression((Expression) visit(ctx.left), (Expression) visit(ctx.right));
    }

    @Override
    public If visitIfExpression(IfExpressionContext ctx) {
        return new If((Expression) visit(ctx.cond), (Expression) visit(ctx.body), (Expression) visit(ctx.fallback));
    }

    @Override
    public OrphosNode visitDotExpression(DotExpressionContext ctx) {
        return ReadRecordField.create((Expression) visit(ctx.operand), ctx.label.getText());
    }

    @Override
    public OrphosNode visitFnExpression(FnExpressionContext ctx) {
        enterScope();
        Identifier argId = new Identifier(ctx.Identifier().getText());
        put(argId);
        Expression body = (Expression) visit(ctx.body);

        FnExpression ret = new FnExpression(argId, body);
        leaveScope();
        return ret;
    }

    @Override
    public Expression visitEqPrecedenceExpression(EqPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public OrphosNode visitSeqExpression(OrphosParser.SeqExpressionContext ctx) {
        return null;
    }

    @Override
    public Expression visitSingleExpression(SingleExpressionContext ctx) {
        return (Expression) (visit(ctx.simpleExpression()));
    }

    @Override
    public OrphosNode visitTupleExpression(TupleExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public Expression visitMulPrecedenceExpression(MulPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitLogicalExpression(LogicalExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public Expression visitAddPrecedenceExpression(AddPrecedenceExpressionContext ctx) {
        return createInfixExpression(ctx.left, ctx.op.getText(), ctx.right);
    }

    @Override
    public OrphosNode visitRecordExpression(RecordExpressionContext ctx) {
        String[] labels = ctx.labels.stream().map(Token::getText).toArray(String[]::new);
        Expression[] values = ctx.values.stream().map(exp -> (Expression) visit(exp)).toArray(Expression[]::new);
        if (ctx.source == null)
            return new NewRecord(labels, values);
        return new With((Expression) visit(ctx.source), labels, values);
    }

    @Override
    public OrphosNode visitLetExpression(LetExpressionContext ctx) {
        Identifier id = new Identifier(ctx.binder.getText());
        boolean rec = ctx.REC() != null;
        boolean shadowing = searchLocal(id.getName()) != null;
        Expression bindant = null;
        if (!rec)
            bindant = (Expression) visit(ctx.bindant);
        if (shadowing)
            enterScope();
        put(id);
        if (rec)
            bindant = (Expression) visit(ctx.bindant);
        LetExpression ret = new LetExpression(ctx.REC() != null, id, bindant, (Expression) visit(ctx.body));
        if (shadowing)
            leaveScope();
        return ret;
    }

    @Override
    public OrphosNode visitTypeDeclExpression(TypeDeclExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public OrphosNode visitIdentifierExpression(IdentifierExpressionContext ctx) {
        String name = ctx.qualifiedIdentifier().getText();
        Identifier id = searchLocal(name);
        boolean local = id != null;
        if (!local)
            id = search(name);
        return new IdentifierExpression(id, local);
    }

    @Override
    public IntegerLiteral visitIntegerExpression(IntegerExpressionContext ctx) {
        String text = ctx.Integer().getText();
        BigInteger value;
        if (text.startsWith("0x"))
            value = new BigInteger(text.substring(2), 16);
        else if (text.startsWith("0b"))
            value = new BigInteger(text.substring(2), 2);
        else if (text.startsWith("0"))
            value = new BigInteger(text, 8);
        else
            value = new BigInteger(text);
        return new IntegerLiteral(value);
    }

    @Override
    public OrphosNode visitFloatExpression(FloatExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public StringExpression visitStringExpression(StringExpressionContext ctx) {
        String text = ctx.StringLiteral().getText();
        return new StringExpression(text.substring(1, text.length() - 1));
    }

    @Override
    public BooleanExpression visitBooleanExpression(BooleanExpressionContext ctx) {
        return new BooleanExpression(ctx.BooleanLiteral().getText().equals("true") ? true : false);
    }

    @Override
    public OrphosNode visitListExpression(OrphosParser.ListExpressionContext ctx) {
        return null;
    }

    @Override
    public Expression visitParenExpression(ParenExpressionContext ctx) {
        return (Expression) visit(ctx.expression());
    }

    @Override
    public OrphosNode visitUnitExpression(UnitExpressionContext ctx) {
        return new UnitExpression();
    }

    @Override
    public OrphosNode visitTypeExpression(TypeExpressionContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public OrphosNode visitAliasTypeDeclBody(AliasTypeDeclBodyContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public OrphosNode visitVaritnaTypeDeclBody(VaritnaTypeDeclBodyContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public OrphosNode visitTypeDecl(TypeDeclContext ctx) {
        // TODO Auto-generated method stub
        throw newPanic();
    }

    @Override
    public Expression visitCompilationUnit(CompilationUnitContext ctx) {
        return (Expression) visit(ctx.expression());
    }

    @Override
    public OrphosNode visitQualifiedIdentifier(OrphosParser.QualifiedIdentifierContext ctx) {
        return null;
    }

    public InfixExpression createInfixExpression(ExpressionContext left, String op, ExpressionContext right) {
        return new InfixExpression((Expression) visit(left), Infix.create(op), (Expression) visit(right));
    }

}