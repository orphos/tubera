// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
lexer grammar OrphosLexer;

SPACE: [ \t]+ -> skip;
COMMENT: '//' [^\r\n]*;
SHEBANG: '#!' [^\r\n]* (' ' | '/') 'orphos' ('\r\n' | '\r' | '\n');
SHELL: '$' [^\r\n]* ('\r\n' | '\r' | '\n');
NL: '\r\n' | '\r' | '\n';
LET: 'let';
REC: 'rec';
AND: 'and';
IN: 'in';
MATCH : 'match';
WITH: 'with';
WHEN: 'when';
END: 'end';
FN: 'fn';
IF: 'if';
OF: 'of';
THEN: 'then';
ELSE: 'else';
EXCEPTION: 'exception';
TYPE: 'type';

TRIPLE_CIRCUMFLEX: '^^^';
TRIPLE_LESSER_THAN: '<<<';
TRIPLE_GREATER_THAN: '>>>';
TRIPLE_AMPERSAND: '&&&';
TRIPLE_VERTICAL: '|||';
DOUBLE_AMPERSAND: '&&';
DOUBLE_VERTICAL: '||';
COMMA: ',';
ARROW: '->';
VERTICAL_LESSER_THAN: '|>';
VERTICAL: '|';
AT: '@';
ASTERISK: '*';
SLASH: '/';
PLUS: '+';
MINUS: '-';
PERCENT: '%';
DOLLAR: '$';
LESSER_THAN: '<';
GREATER_THAN: '>';
DOUBLE_EQ: '==';
SEMI: ';';
DOUBLE_COLON: '::';
DOT: '.';
LPAREN: '(';
RPAREN: ')';
EQ: '=';

fragment DecimalInteger: [1-9] [_0-9]*;
fragment OctalInteger: '0' [_0-7]*;
fragment HexInteger: '0x' [_0-9a-f]+;
fragment BinaryInteger: '0b' [_01]+;
Integer: DecimalInteger | HexInteger | BinaryInteger | OctalInteger;
fragment FloatSuffix: 'd' | 'f';
fragment DecimalFloat: [0-9]+ '.' [0-9]* | '.' [0-9]+;
FloatLiteral: DecimalFloat FloatSuffix?;
StringLiteral: '"' (~'"' | '\\"')* '"';
BooleanLiteral: 'true' | 'false';

Identifier:
  [a-zA-Z] [_a-zA-Z0-9]*
  | [_a-z] [_a-zA-Z0-9]+
  | '${' ~'}'* '}'
  ;

GraveIdentifier:
  '`' [_a-zA-Z0-9]+
  | '`{' ~'}'* '}'
  ;

WILDCARD: '_';

LCBRACKET: '{';
RCBRACKET: '}';
LBRACKET: '[';
RBRACKET: ']';
COLON: ':';