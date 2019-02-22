grammar Imp;

/* Rules */
main : 'int' varlist ';' stmt;

varlist : var | var ',' varlist;

stmt 
    : left=var '=' right=aexpr ';'                           # AssignNode
    | block                                                  # BlockNode
    | 'if' cond=condition bodyB=block 'else' elseB=block     # IfNode
    | 'while' cond=condition bodyB=block                     # WhileNode
    | <assoc=right> left=stmt right=stmt                     # SequenceNode
    ;

condition 
    : '(' child=bexpr ')'                                   # BoolBracketCond
    ;

block 
    : '{''}'                                                # EmptyBlock
    | '{' stmt '}'                                          # NonEmptyBlock
    ;

aexpr
    : var                                                   # VarNode
    | AVAL                                                  # IntNode
    | '(' child=aexpr ')'                                   # ArithBracket
    | <assoc=left> left=aexpr '/' right=aexpr               # DivNode
    | <assoc=left> left=aexpr '+' right=aexpr               # PlusNode
    ;

bexpr 
    : BVAL                                                  # BoolNode
    | '(' child=bexpr ')'                                   # BoolBracket
    | left=aexpr '>' right=aexpr                            # GreaterNode
    | '!' child=bexpr                                       # NotNode
    | <assoc=left> left=bexpr '&&' right=bexpr              # AndNode
    ;

var : VAR;

/* Tokens */
AVAL : [1-9]+([0-9]+)* | '0';
BVAL : 'true' | 'false';
VAR : [a-z]+;

WS : [ \t\r\n]+ -> skip ;
