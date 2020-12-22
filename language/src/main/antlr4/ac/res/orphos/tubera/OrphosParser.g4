// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
parser grammar OrphosParser;

options { tokenVocab=OrphosLexer; }

compilationUnit: SHEBANG? expression EOF;

qualifiedIdentifier: (root=DOUBLE_COLON? path+=Identifier (DOUBLE_COLON path+=Identifier)*)? Identifier;

expression:
  operand=expression DOT label=Identifier # DotExpression
  | left=expression right=expression # ApplicationExpression
  | left=expression op=(ASTERISK | SLASH | PERCENT) right=expression # MulPrecedenceExpression
  | left=expression op=(PLUS | MINUS) right=expression # AddPrecedenceExpression
  | left=expression op=(DOUBLE_EQ | GREATER_THAN | LESSER_THAN) right=expression # EqPrecedenceExpression
  | left=expression op=(DOUBLE_AMPERSAND | DOUBLE_VERTICAL) right=expression # LogicalExpression
  | <assoc=right> head=expression (SEMI tail+=expression) # SeqExpression
  | LET REC? binder=Identifier EQ bindant=expression IN body=expression # LetExpression
  | FN param=Identifier ARROW body=expression # FnExpression
  | IF cond=expression THEN body=expression ((ELSE fallback=expression) | END) # IfExpression
  | LCBRACKET (source=expression WITH)? (labels+=Identifier EQ values+=expression SEMI)* RCBRACKET # RecordExpression
  | typeDecl # TypeDeclExpression
  | simpleExpression # SingleExpression
  ;

simpleExpression:
  qualifiedIdentifier # IdentifierExpression
  | Integer # IntegerExpression
  | FloatLiteral # FloatExpression
  | StringLiteral # StringExpression
  | BooleanLiteral # BooleanExpression
  | LBRACKET (head=expression (COMMA tail+=expression)* last_asterisk=ASTERISK?)? RBRACKET # ListExpression
  | LPAREN head=expression (COMMA tail+=expression)+ RPAREN # TupleExpression
  | LPAREN expression RPAREN # ParenExpression
  | LPAREN RPAREN # UnitExpression
  ;

typeExpression: qualifiedIdentifier;

typeDeclBody:
  typeExpression # AliasTypeDeclBody
  | (ctors+=Identifier (OF typeExpression)+) # VaritnaTypeDeclBody
  ;

typeDecl: TYPE Identifier EQ typeDeclBody;
