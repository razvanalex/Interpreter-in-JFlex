import java.util.*;

%%

%class Lexer
%standalone

%eof{
    // Now on stack, we must have 2 nodes: the main node and the statements.
    if (!stack.isEmpty() && stack.peek() instanceof Statement) {
        Statement stmt = (Statement)stack.pop();
        if (!stack.isEmpty() && stack.peek() instanceof MainNode) {
            MainNode main = (MainNode)stack.peek();
            main.child = stmt;
        } else {
            throw new Error("Program must start with an int statement!");
        }
    }

    // When we reach eof, we check if the stack has only one node,
    // otherwise it's a parsing error.
    if (stack.size() > 1)
        throw new Error("Stack does not have one node after parsing!");
    else if (stack.size() == 0) 
        throw new Error("Stack cannot have zero elements after parsing!");
%eof}


%{
    // The else node used to disallow the user to miss the "else" keyword.
    class ElseNode extends Statement {
        @Override
        protected String Show(int level) {
            return "";
        }

        @Override
        public void accept(IVisitor visitor) throws Exception {
        }
    }

    // The stack of our automaton. 
	Stack<Expression> stack = new Stack<>();
    
    // Counter for number of lines
    int lineNum = 1;
    
    // Check for compile errors
    boolean compiationError = false;
    int errorLine = -1;
    boolean initLine = true;

    /**
     * Check if {@code ex} is a valid arithmetic expression.
     */
    private void checkForArithExpr(Expression ex) throws Exception {
        if (!(ex instanceof ArithExpression || ex instanceof BracketNode))
            throw new Exception("No arithmetic expression provided!");
        
        if (ex instanceof ArithExpression) {
            ArithExpression arith = (ArithExpression)ex;

            if (arith instanceof PlusNode 
                    && (((PlusNode)arith).leftChild == null
                        || ((PlusNode)arith).rightChild == null)) {
                throw new Exception("Incomplete \"+\" operation given!");
            }

            if (arith instanceof DivNode 
                    && (((DivNode)arith).leftChild == null
                        || ((DivNode)arith).rightChild == null)) {
                throw new Exception("Incomplete \"/\" operation given!");
            }
        } else if (ex instanceof BracketNode) {
            BracketNode bracket = (BracketNode)ex;

            if (!bracket.isArithmetic())
                throw new Exception("Arithmetic bracket type expected, but " +
                                    "boolean bracket given!");
        } 
    }

    /**
     * Check if {@code ex} is a valid boolean expression.
     */
    private void checkForBoolExpr(Expression ex) throws Exception {
        if (!(ex instanceof BoolExpression || ex instanceof BracketNode))
            throw new Exception("No boolean expression provided!");

        if (ex instanceof BoolExpression) {
            BoolExpression boolExpr = (BoolExpression)ex;

            if (boolExpr instanceof AndNode
                    && (((AndNode)boolExpr).leftChild == null
                        || ((AndNode)boolExpr).rightChild == null)) {
                throw new Exception("Incomplete \"&&\" operation given!");
            }

            if (boolExpr instanceof NotNode
                    && ((NotNode)boolExpr).child == null){
                throw new Exception("Incomplete \"!\" operation given!");
            }
        } else if (ex instanceof BracketNode) {
            BracketNode bracket = (BracketNode)ex;

            if (!bracket.isBoolean())
                throw new Exception("Boolean bracket type expected, but " +
                                    "arithmetic bracket given!");
        }
    }

    /**
     * Apply the nested NOT rule
     */
    private void nestedNotRule(NotNode not, ArithExpression operator, 
            Expression lastNode) {
        stack.push(not);
        stack.push(not.child);
        stack.push(operator);
        arithReduceStack(lastNode);
        stack.pop();
    }

    /**
     * Apply the nested AND rule
     */
    private void nestedAndRule(AndNode and, ArithExpression operator, 
            Expression lastNode) {
        stack.push(and);
        stack.push(and.rightChild);
        stack.push(operator);
        arithReduceStack(lastNode);
        stack.pop();
    }

    /**
     * Apply the GREATER reduction
     */
    private void greaterRule(ArithExpression operator, GreaterNode greater, 
            Expression lastNode, Expression toPush) {
        
        if (operator instanceof PlusNode) {
            PlusNode plus = (PlusNode)operator;

            plus.leftChild = greater.rightChild;
            plus.rightChild = lastNode;
            greater.rightChild = plus;

        } else if (operator instanceof DivNode) {
            DivNode div = (DivNode)operator;

            if (greater.rightChild instanceof PlusNode) {
                PlusNode plus = (PlusNode)greater.rightChild;
                div.leftChild = plus.rightChild;
                div.rightChild = lastNode;
                plus.rightChild = div;

            } else {
                div.leftChild = greater.rightChild;
                div.rightChild = lastNode;
                greater.rightChild = div;
                
            } 
        }

        stack.push(toPush);
    }

    /**
     * Apply the BOOLEAN rules
     */
    void booleanRules(ArithExpression operator, Expression leftMember,
            Expression lastNode) {

        if (leftMember instanceof GreaterNode) {
            GreaterNode greater = (GreaterNode)leftMember;
            greaterRule(operator, greater, lastNode, greater);

        } else if (leftMember instanceof AndNode) {
            AndNode and = (AndNode)leftMember;

            if (and.rightChild instanceof GreaterNode) {
                GreaterNode greater = (GreaterNode)and.rightChild;
                greaterRule(operator, greater, lastNode, and);   
                
            } else {
               nestedAndRule(and, operator, lastNode);
            }
        } else if (leftMember instanceof NotNode) {
            NotNode not = (NotNode)leftMember;

            if (not.child instanceof GreaterNode) {
                GreaterNode greater = (GreaterNode)not.child;
                greaterRule(operator, greater, lastNode, not);
                      
            } else {
                nestedNotRule(not, operator, lastNode);
            }
        }
    }

    /**
     * Apply the PLUS rules
     */    
    private void plusRules(PlusNode plus, Expression lastNode) {
        if (emptyStack())
            throw new Error("+ operator must have a left hand side member!");

        Expression leftMember = stack.pop();
        boolean isArithmetic = true;

        // Check left member for arithmetic expression 
        try {
            checkForArithExpr(leftMember);
        } catch (Exception e) {
            isArithmetic = false;
        }

        if (isArithmetic) {
            plus.leftChild = leftMember;
            plus.rightChild = lastNode;
            stack.push(plus);

        } else {
            booleanRules(plus, leftMember, lastNode);
        }
    }

    /**
     * Apply the DIV rules
     */
    private void divRules(DivNode div, Expression lastNode) {
         if (emptyStack())
            throw new Error("/ operator must have a left hand side member!");

        Expression leftMember = stack.pop();
        boolean isArithmetic = true;

        // Check left member for arithmetic expression 
        try {
            checkForArithExpr(leftMember);
        } catch (Exception e) {
            isArithmetic = false;
        }

        if (isArithmetic) {
            if (leftMember instanceof PlusNode) {
                PlusNode plus = (PlusNode)leftMember;
                div.leftChild = plus.rightChild;
                div.rightChild = lastNode;
                plus.rightChild = div;
                
                stack.push(plus);
            } else {
                div.leftChild = leftMember;
                div.rightChild = lastNode;

                stack.push(div);
            }
        } else {
            booleanRules(div, leftMember, lastNode);
        }
    }

    /**
     * Apply the GREATER rules
     */
    private void greaterRules(GreaterNode greater, Expression lastNode) {
        if (emptyStack()) 
            throw new Error("> operator must have a left hand side member!");

        Expression leftMember = stack.pop();
        try {
            checkForArithExpr(leftMember);
            checkForArithExpr(lastNode);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }

        // Put the Greater node into AST
        greater.leftChild = leftMember;
        greater.rightChild = lastNode;

        boolReduceStack(greater);
    }

    /**
     * Apply reduction for arithmetic expressions
     */
    private void arithReduceStack(Expression lastNode) {
        // Arith function should have an arithmetic expression parameter
        try {
            checkForArithExpr(lastNode);
        } catch (Exception e) {
            throw new Error("Invalid arithReduceStack() call! " + 
                e.getMessage());
        } 

        // If the stack is empty, put the node on stack
        if (emptyStack()) {
            stack.push(lastNode);
            return;
        }

        // If the stack is non-empty, then apply the rules
        if (stack.peek() instanceof PlusNode) {
            PlusNode plus = (PlusNode)stack.pop();
            plusRules(plus, lastNode);
        } else if (stack.peek() instanceof DivNode) {
            DivNode div = (DivNode)stack.pop();
            divRules(div, lastNode);
        } else if (stack.peek() instanceof GreaterNode) {
            GreaterNode greater = (GreaterNode)stack.pop();
            greaterRules(greater, lastNode);
        } else {
            stack.push(lastNode);
        }
    }

    /**
     * Apply the AND rules
     */
    private void andRules(AndNode and, Expression lastNode) {
        if (emptyStack())
            throw new Error("&& operator must have a left hand side member!");

        Expression leftMember = stack.pop();
        boolean isBoolean = true;

        // Bool function should have a boolean expression parameter
        try {
            checkForBoolExpr(leftMember);
        } catch (Exception e) {
            isBoolean = false;
        }

        if (isBoolean) {
            and.leftChild = leftMember;
            and.rightChild = lastNode;

            stack.push(and);
        }
    }

    /**
     * Apply the NOT rules
     */
    private void notRules(NotNode not, Expression lastNode) {
        if (!emptyStack() && stack.peek() instanceof AndNode) {
            AndNode and = (AndNode)stack.pop();

            // Here we cannot have empty stack!
            if (emptyStack())
                throw new Error("&& operator must have a left hand side " + 
                                "member!");

            Expression leftMember = stack.pop();
            boolean isBoolean = true;

            // Bool function should have a boolean expression parameter
            try {
                checkForBoolExpr(leftMember);
            } catch (Exception e) {
                isBoolean = false;
            }

            if (isBoolean) {
                and.leftChild = leftMember;
                and.rightChild = not;
                not.child = lastNode;

                stack.push(and);
            }
        } else {        
            not.child = lastNode;
            boolReduceStack(not);
        }
    }

    /**
     * Apply the reduction for boolean expressions
     */
    private void boolReduceStack(Expression lastNode) {
        // Bool function should have a boolean expression parameter
        try {
            checkForBoolExpr(lastNode);
        } catch (Exception e) {
            throw new Error("Invalid boolReduceStack() call! " + 
                e.getMessage());
        }

        // If the stack is empty, put the node on stack
        if (emptyStack()) {
            stack.push(lastNode);
            return;
        }

        // Apply the rules
        if (stack.peek() instanceof AndNode) {
            AndNode and = (AndNode)stack.pop();
            andRules(and, lastNode);
            
        } else if (stack.peek() instanceof NotNode) {
            NotNode not = (NotNode)stack.pop();
            notRules(not, lastNode);

        } else if (stack.peek() instanceof IfNode) {
            stack.push(lastNode);
        } else if (stack.peek() instanceof WhileNode) {
            stack.push(lastNode);
        } else {
            try {
                checkForBoolExpr(stack.peek());
            } catch (Exception e) {
                throw new Error("Cannot match to a boolean expression! " + 
                    e.getMessage());
            }
            stack.push(lastNode);
        }
    }

    /**
     * Apply the reduction for statements
     */
    private void reduceStatement(Statement stmt) {
        if (!emptyStack() && stack.peek() instanceof Statement) {
            if (stack.peek() instanceof BlockNode) {
                BlockNode block = (BlockNode)stack.peek();
                if (!block.isSet){
                    stack.push(stmt);
                    return;
                }
            }

            if (stack.peek() instanceof SequenceNode) {
                SequenceNode seq = (SequenceNode)stack.peek();

                if (seq.rightChild instanceof SequenceNode) {
                    stack.push(seq.rightChild);
                    reduceStatement(stmt);
                    stack.pop();
                } else {
                    SequenceNode newSeq = new SequenceNode();
                    newSeq.leftChild = seq.rightChild;
                    newSeq.rightChild = stmt;
                    seq.rightChild = newSeq;
                }

            } else {
                SequenceNode seq = new SequenceNode(); 
                seq.leftChild = (Statement)stack.pop();
                seq.rightChild = stmt;

                stack.push(seq);
            }

        } else {
            stack.push(stmt);
        }
    }

    /**
     * Apply the reduction for blocks
     */
    private void sequenceBlocks() {
        if (emptyStack()) 
            return;

        if (stack.peek() instanceof BlockNode) {
            BlockNode stmt1 = (BlockNode)stack.pop();

            if (!emptyStack() && stack.peek() instanceof ElseNode) {
                stack.pop();

                if (!emptyStack() && stack.peek() instanceof BlockNode) {
                    BlockNode stmt2 = (BlockNode)stack.pop();
                    
                    if (!emptyStack() && stack.peek() instanceof BracketNode) {
                        BracketNode cond = (BracketNode)stack.pop();

                        if (cond.isBoolean()) {
                            if (stack.peek() instanceof IfNode) {
                                IfNode ifStmt = (IfNode)stack.pop();
                                ifStmt.condition = cond;
                                ifStmt.thenBranch = stmt2;
                                ifStmt.elseBrench = stmt1;

                                reduceStatement(ifStmt);
                            } else {
                                throw new Error("If statement expected!");
                            }

                        } else {
                            throw new Error("Condition cannot be other than " + 
                                            "boolean!");
                        }
                    }
                }

            } else if (!emptyStack() && stack.peek() instanceof BracketNode) {
                BracketNode cond = (BracketNode)stack.pop();

                if (cond.isBoolean()) {
                    if (stack.peek() instanceof WhileNode) {
                        WhileNode whileStmt = (WhileNode)stack.pop();
                        whileStmt.condition = cond;
                        whileStmt.body = stmt1;

                        reduceStatement(whileStmt);
                    } else {
                        stack.push(cond);
                        stack.push(stmt1);
                    }
                } else {
                    throw new Error("Condition cannot be other than boolean!");
                }          
            } else if (!emptyStack() && stack.peek() instanceof BlockNode) {
                BlockNode stmt2 = (BlockNode)stack.pop();
                if (stmt1.isSet && stmt2.isSet) {
                    SequenceNode seq = new SequenceNode();
                    seq.rightChild = stmt1;
                    seq.leftChild = stmt2;

                    stack.push(seq);
                } else if (stmt1.isSet && !stmt2.isSet) {
                    stmt2.set(stmt1);
                    stack.push(stmt2);
                    stack.push(stmt1);              
                }     
            } else if (!emptyStack() && stack.peek() instanceof SequenceNode) {
                SequenceNode stmt2 = (SequenceNode)stack.peek();

                SequenceNode seq = new SequenceNode();
                seq.rightChild = stmt1;
                seq.leftChild = stmt2.rightChild;
                stmt2.rightChild = seq;

            } else {
                stack.push(stmt1);
            }
        }
    }

    /**
     * Reduce expressions when ')' is parsed
     */
    private void closeParenthesis() {
        try {
            Expression crtExpr = stack.pop();
            Expression lastOpenParenthesis = stack.pop();

            if (lastOpenParenthesis instanceof BracketNode) {
                BracketNode openBracket = (BracketNode)lastOpenParenthesis;
                openBracket.child = crtExpr;
                openBracket.setType();
                stack.push(lastOpenParenthesis);

                if (openBracket.isArithmetic())
                    arithReduceStack(stack.pop());
                else if (openBracket.isBoolean())
                    boolReduceStack(stack.pop());
                    
            } else {
                /* Parsing error! */
                throw new Error("Error while parsing parenthesis!");
            }
        } catch (EmptyStackException e) {
            throw new Error("Error while parsing! Empty stack exception " + 
                            "thrown!");
        }
    }

    /**
     * Reduce statement when semicolon is parsed
     */
    private void semicolonReduction() {
        if (emptyStack())
            throw new Error("Semicolon cannot be used alone!");

        addVariableToList();

         // Check stack peek for arithmetic expression 
        try {
            checkForArithExpr(stack.peek());
        } catch (Exception e) {
            if (!initLine)
                throw new Error("Assignment cannot have other than " + 
                                "arithmetic expression as right operand!");
            else return;
        }

        Expression arith = stack.pop();

        if (emptyStack())
            throw new Error("Semicolon cannot be used with arithmetic " + 
                            "expressions!");
        
        if (stack.peek() instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode)stack.pop();

            if (!emptyStack() && stack.peek() instanceof VarNode) {
                VarNode var = (VarNode)stack.pop();
                var.main = (MainNode)stack.firstElement();
                assign.variable = var;
                assign.value = arith;

                reduceStatement(assign);
            } else {
                throw new Error("VarNode required as left operand of " +
                                "assignment!");
            }
        }
        
    }

    /**
     * Reduce blocks when '}' is parsed
     */
    private void closeBlock() {
        if (emptyStack()) 
            throw new Error("No block detected on stack!");
                
        if (stack.peek() instanceof Statement) {
            if (stack.peek() instanceof BlockNode) {
                BlockNode block = (BlockNode)stack.peek();
                if (block.isSet) {
                    Statement stmt = (Statement)stack.pop();
                    if (emptyStack()) 
                        throw new Error("No block detected on stack!");

                    if (stack.peek() instanceof BlockNode) {
                        BlockNode block2 = (BlockNode)stack.peek();
                        block2.set(stmt);
                    } else {
                        throw new Error("No block detected on stack!");
                    }
                } else {
                    block.set(null);
                }
            } else {
                Statement stmt = (Statement)stack.pop();
                if (emptyStack()) 
                    throw new Error("No block detected on stack!");
                
                if (stack.peek() instanceof BlockNode) {
                    BlockNode block = (BlockNode)stack.peek();
                    block.set(stmt);

                } else {
                    throw new Error("No block detected on stack!");
                }
            }
        }
        
        sequenceBlocks();
    }

    /* 
     * Add variables to variables list 
     */
    private void addVariableToList() {
        if (stack.peek() instanceof VarNode) {
            VarNode var = (VarNode)stack.pop();
            
            if (stack.peek() instanceof MainNode) {
                MainNode mainNode = (MainNode)stack.peek();
                mainNode.varList.put(var.name, null);
            } else {
                stack.push(var);
            }
        }
    }
    
    /* 
     * Check for empty stack 
     */
    private boolean emptyStack() {
        return stack.isEmpty() || stack.peek() instanceof MainNode;
    }

%}


lineTerminator = \n | \r\n

varListStart = int
comma = ,

Digit = [1-9]
Number = {Digit}(0 | {Digit})* | 0
String = [a-z]+
Var = {String}
AVal = {Number}

openParenthesis = \(
closeParenthesis = \)
plusOperator = \+
divOperator = \/

BVal = true | false
andOperator = \&\&
greaterOperator = \>
notOperator = \!

assignOperator = \=
semicolon = \;

emptyBlock = \{\}
openBlock = \{
closeBlock = \}

ifCondition = if
elseStatement = else

whileStatement = while


%%
{lineTerminator} {
    lineNum++;
}

{comma} {
    addVariableToList();
}

{varListStart} {
    MainNode node = new MainNode();
    node.line = lineNum;
    stack.push(node);
}

/* If statements */
{ifCondition} {
    IfNode node = new IfNode();
    node.line = lineNum;
    stack.push(node);
}

{elseStatement} {
    ElseNode node = new ElseNode();

    stack.push(node);
}

/* While statemets */
{whileStatement} {
    WhileNode node = new WhileNode();
    node.line = lineNum;
    stack.push(node);
}

/* Parenthesis */
{openParenthesis} {
    BracketNode node = new BracketNode();
    node.line = lineNum;
    stack.push(node);
}

{closeParenthesis} {
    closeParenthesis();
}

/* Arithmetic operators */
{plusOperator} {
    PlusNode node = new PlusNode();
    node.line = lineNum;
    stack.push(node);
}

{divOperator} {
    DivNode node = new DivNode();
    node.line = lineNum;
    stack.push(node);
}

/* Variables and values */
{AVal} {
    IntNode node = new IntNode(Integer.parseInt(yytext()));
    node.line = lineNum;
    arithReduceStack(node);
}

{BVal} {
    BoolNode node = new BoolNode(Boolean.parseBoolean(yytext()));
    node.line = lineNum;
    boolReduceStack(node);
}

{Var} {
    VarNode var = new VarNode(yytext());
    var.line = lineNum;
    var.main = (MainNode)stack.firstElement();

    if (!initLine && !compiationError && !var.main.varList.containsKey(var.name)) {
        compiationError = true;
        errorLine = lineNum;
    }

    arithReduceStack(var);
}


/* Boolean operators */
{andOperator} {
    AndNode node = new AndNode();
    node.line = lineNum;
    stack.push(node);
}

{greaterOperator} {
    GreaterNode node = new GreaterNode();
    node.line = lineNum;
    stack.push(node);
}

{notOperator} {
    NotNode node = new NotNode();
    node.line = lineNum;
    stack.push(node);
}

/* Assignments */
{assignOperator} {
    AssignmentNode node = new AssignmentNode();
    node.line = lineNum;
    stack.push(node);
}

{semicolon} {
    semicolonReduction(); 
    if (initLine) 
        initLine = false;
}

/* Blocks */
{emptyBlock} {
    stack.push(new BlockNode());
    closeBlock();
}

{openBlock} {
    stack.push(new BlockNode());
}

{closeBlock} {
    closeBlock();
}

. {}
