import java.util.HashMap;

/**
 * Implementation of the IVisitor used to evaluate the AST.
 * 
 * @author razvan
 *
 */
public class ExpressionVisitor implements IVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(IntNode)
	 */
	public int visit(IntNode intNode) throws Exception {
		return intNode.val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(VarNode)
	 */
	@Override
	public int visit(VarNode varNode) throws Exception {
		// Check for errors
		if (varNode.main == null
				|| varNode.main.varList.get(varNode.name) == null)
			throw new UnassignedVar(varNode.line);

		// Return the value of the variable from HashMap
		return varNode.main.varList.get(varNode.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(PlusNode)
	 */
	@Override
	public int visit(PlusNode plusNode) throws Exception {
		int leftResult = 0;
		int rightResult = 0;

		// Evaluate the left child
		if (plusNode.leftChild instanceof ArithExpression) {
			leftResult = ((ArithExpression) plusNode.leftChild).accept(this);
		} else if (plusNode.leftChild instanceof BracketNode
				&& ((BracketNode) plusNode.leftChild).isArithmetic()) {
			leftResult = ((BracketNode) plusNode.leftChild).acceptA(this);
		}

		// Evaluate the right child
		if (plusNode.rightChild instanceof ArithExpression) {
			rightResult = ((ArithExpression) plusNode.rightChild).accept(this);
		} else if (plusNode.rightChild instanceof BracketNode
				&& ((BracketNode) plusNode.rightChild).isArithmetic()) {
			rightResult = ((BracketNode) plusNode.rightChild).acceptA(this);
		}

		// Return the sum
		return leftResult + rightResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(DivNode)
	 */
	@Override
	public int visit(DivNode divNode) throws Exception {
		int leftResult = 0;
		int rightResult = 0;

		// Evaluate the left child
		if (divNode.leftChild instanceof ArithExpression) {
			leftResult = ((ArithExpression) divNode.leftChild).accept(this);
		} else if (divNode.leftChild instanceof BracketNode
				&& ((BracketNode) divNode.leftChild).isArithmetic()) {
			leftResult = ((BracketNode) divNode.leftChild).acceptA(this);
		}

		// Evaluate the right child
		if (divNode.rightChild instanceof ArithExpression) {
			rightResult = ((ArithExpression) divNode.rightChild).accept(this);
		} else if (divNode.rightChild instanceof BracketNode
				&& ((BracketNode) divNode.rightChild).isArithmetic()) {
			rightResult = ((BracketNode) divNode.rightChild).acceptA(this);
		}

		// Throw error if divide by zero
		if (rightResult == 0) {
			throw new DivideByZero(divNode.line);
		}

		// Return the result
		return leftResult / rightResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visitA(BracketNode)
	 */
	@Override
	public int visitA(BracketNode bracketNode) throws Exception {
		if (bracketNode.isArithmetic()) {
			if (bracketNode.child instanceof ArithExpression)
				return ((ArithExpression) bracketNode.child).accept(this);
			else if (bracketNode.child instanceof BracketNode) 
				return ((BracketNode) bracketNode.child).acceptA(this);
			else 
				throw new Exception("Type of bracket unknown!");
		} else {
			throw new Exception("Cannot evaluate arithmetic bracket!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(BoolNode)
	 */
	@Override
	public boolean visit(BoolNode boolNode) throws Exception {
		return boolNode.val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visitB(BracketNode)
	 */
	@Override
	public boolean visitB(BracketNode bracketNode) throws Exception {
		if (bracketNode.isBoolean()) {
			if (bracketNode.child instanceof BoolExpression)
				return ((BoolExpression) bracketNode.child).accept(this);
			else if (bracketNode.child instanceof BracketNode) 
				return ((BracketNode) bracketNode.child).acceptB(this);
			else 
				throw new Exception("Type of bracket unknown!");
		} else {
			throw new Exception("Cannot evaluate boolean bracket!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(AndNode)
	 */
	@Override
	public boolean visit(AndNode andNode) throws Exception {
		boolean leftResult = false;
		boolean rightResult = false;

		// Evaluate the left child
		if (andNode.leftChild instanceof BoolExpression) {
			leftResult = ((BoolExpression) andNode.leftChild).accept(this);
		} else if (andNode.leftChild instanceof BracketNode
				&& ((BracketNode) andNode.leftChild).isBoolean()) {
			leftResult = ((BracketNode) andNode.leftChild).acceptB(this);
		}

		// Evaluate the right child
		if (andNode.rightChild instanceof BoolExpression) {
			rightResult = ((BoolExpression) andNode.rightChild).accept(this);
		} else if (andNode.rightChild instanceof BracketNode
				&& ((BracketNode) andNode.rightChild).isBoolean()) {
			rightResult = ((BracketNode) andNode.rightChild).acceptB(this);
		}

		// Return the result
		return leftResult && rightResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(GreaterNode)
	 */
	@Override
	public boolean visit(GreaterNode greaterNode) throws Exception {
		int leftResult = 0;
		int rightResult = 0;

		// Evaluate the left child
		if (greaterNode.leftChild instanceof ArithExpression) {
			leftResult = ((ArithExpression) greaterNode.leftChild).accept(this);
		} else if (greaterNode.leftChild instanceof BracketNode
				&& ((BracketNode) greaterNode.leftChild).isArithmetic()) {
			leftResult = ((BracketNode) greaterNode.leftChild).acceptA(this);
		}

		// Evaluate the right child
		if (greaterNode.rightChild instanceof ArithExpression) {
			rightResult = ((ArithExpression) greaterNode.rightChild)
					.accept(this);
		} else if (greaterNode.rightChild instanceof BracketNode
				&& ((BracketNode) greaterNode.rightChild).isArithmetic()) {
			rightResult = ((BracketNode) greaterNode.rightChild).acceptA(this);
		}

		// Return the result
		return leftResult > rightResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(NotNode)
	 */
	@Override
	public boolean visit(NotNode notNode) throws Exception {
		boolean result = false;

		// Evaluate the child
		if (notNode.child instanceof BoolExpression) {
			result = ((BoolExpression) notNode.child).accept(this);
		} else if (notNode.child instanceof BracketNode
				&& ((BracketNode) notNode.child).isBoolean()) {
			result = ((BracketNode) notNode.child).acceptB(this);
		}

		// Return the negation of result
		return !result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(AssignmentNode)
	 */
	@Override
	public void visit(AssignmentNode assignNode) throws Exception {
		String var = assignNode.variable.name;
		Integer val = null;

		if (assignNode.value instanceof ArithExpression) {
			val = ((ArithExpression)assignNode.value).accept(this);
		} else if (assignNode.value instanceof BracketNode) {
			val = ((BracketNode)assignNode.value).acceptA(this);
		}

		HashMap<String, Integer> varList = assignNode.variable.main.varList;

		// Check if the variable is in the map and set the value if affirmative
		if (varList.containsKey(var)) {
			varList.put(var, val);
		} else {
			throw new UnassignedVar(assignNode.variable.line);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(BlockNode)
	 */
	@Override
	public void visit(BlockNode blockNode) throws Exception {
		if (blockNode.statement != null)
			blockNode.statement.accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(IfNode)
	 */
	@Override
	public void visit(IfNode ifNode) throws Exception {
		if (ifNode.condition.acceptB(this)) {
			ifNode.thenBranch.accept(this);
		} else {
			ifNode.elseBrench.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(WhileNode)
	 */
	@Override
	public void visit(WhileNode whileNode) throws Exception {
		while (whileNode.condition.acceptB(this)) {
			whileNode.body.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(SequenceNode)
	 */
	@Override
	public void visit(SequenceNode seqNode) throws Exception {
		seqNode.leftChild.accept(this);
		seqNode.rightChild.accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitor#visit(MainNode)
	 */
	@Override
	public void visit(MainNode mainNode) throws Exception {
		mainNode.child.accept(this);
	}

}