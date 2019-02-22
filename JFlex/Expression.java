import java.util.HashMap;
import java.lang.UnsupportedOperationException;

/**
 * Generic node.
 */
public abstract class Expression {
	/**
	 * The line where the node is in the input code. It is used to show the
	 * error line.
	 */
	int line = -1;

	/**
	 * Create a representation of the tree, starting from current node as root,
	 * and using a custom level of indentation.
	 * 
	 * @param level
	 *            of indentation
	 * @return a string representation of the tree
	 */
	protected abstract String Show(int level);

	/**
	 * Create a string representation of the node, using indentation 0. It is
	 * equivalent to {@code Show(0)}.
	 * 
	 * @return the representation as String
	 */
	public String Show() {
		return Show(0);
	}
}

/**
 * Identifies an arithmetic expression.
 */
abstract class ArithExpression extends Expression implements IVisitableArith {
	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitableArith#accept(IVisitor)
	 */
	@Override
	public int accept(IVisitor visitor) throws Exception {
		throw new UnsupportedOperationException();
	}
}

/**
 * Identifies a boolean expression.
 */
abstract class BoolExpression extends Expression implements IVisitableBool {
	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitableBool#accept(IVisitor)
	 */
	@Override
	public boolean accept(IVisitor visitor) throws Exception {
		throw new UnsupportedOperationException();
	}
}

/**
 * Identifies a statement.
 */
abstract class Statement extends Expression implements IVisitableStmt {
	/*
	 * (non-Javadoc)
	 * 
	 * @see IVisitableStmt#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		throw new UnsupportedOperationException();
	}
}

/**
 * Class for integer node.
 */
class IntNode extends ArithExpression {
	/**
	 * The value of the integer node.
	 */
	int val;

	/**
	 * Create an IntNode
	 * 
	 * @param val
	 *            the value of the node
	 */
	public IntNode(int val) {
		this.val = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<IntNode> " + val + "\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ArithExpression#accept(IVisitor)
	 */
	@Override
	public int accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for boolean node.
 */
class BoolNode extends BoolExpression {
	/**
	 * The value of the boolean node.
	 */
	boolean val;

	/**
	 * Create a BoolNode.
	 * 
	 * @param val
	 *            the value of the node
	 */
	public BoolNode(boolean val) {
		this.val = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");
		String value = val ? "true" : "false";
		return identation.toString() + "<BoolNode> " + value + "\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoolExpression#accept(IVisitor)
	 */
	@Override
	public boolean accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for variable node.
 */
class VarNode extends ArithExpression {
	/**
	 * The name of the variable
	 */
	String name;

	/**
	 * A reference to the main node, to access the hashmap
	 */
	MainNode main;

	/**
	 * Create a variable node
	 * 
	 * @param name
	 *            of the strings
	 */
	public VarNode(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<VariableNode> " + name + "\n";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ArithExpression#accept(IVisitor)
	 */
	@Override
	public int accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for plus node.
 */
class PlusNode extends ArithExpression {
	/**
	 * The left child expression
	 */
	Expression leftChild = null;

	/**
	 * The right child expression
	 */
	Expression rightChild = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<PlusNode> +\n"
				+ leftChild.Show(level + 1) + rightChild.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ArithExpression#accept(IVisitor)
	 */
	@Override
	public int accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for div node.
 */
class DivNode extends ArithExpression {
	/**
	 * The left child expression
	 */
	Expression leftChild = null;

	/**
	 * The right child expression
	 */
	Expression rightChild = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<DivNode> /\n"
				+ leftChild.Show(level + 1) + rightChild.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ArithExpression#accept(IVisitor)
	 */
	@Override
	public int accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for generic bracket node.
 */
class BracketNode extends Expression {
	/**
	 * The type of the string. It may be: GENERIC, ARITHMETIC or BOOLEAN. It is
	 * used to identify whether the child is an arithmetic or a boolean
	 * expression.
	 */
	private String type = "GENERIC";

	/**
	 * The child of the node
	 */
	Expression child = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<BracketNode> ()\n"
				+ child.Show(level + 1);
	}

	/**
	 * Check if the bracket is for arithmetic expressions.
	 * 
	 * @return whether the bracket is arithmetic or not.
	 */
	public boolean isArithmetic() {
		return type.equals("ARITHMETIC") || type.equals("GENERIC");
	}

	/**
	 * Check if the bracket is for boolean expressions.
	 * 
	 * @return whether the bracket is boolean or not.
	 */
	public boolean isBoolean() {
		return type.equals("BOOLEAN") || type.equals("GENERIC");
	}

	/**
	 * Set the bracket to be arithmetic
	 */
	public void setArithmetic() {
		type = "ARITHMETIC";
	}

	/**
	 * Set the bracket to be boolean
	 */
	public void setBoolean() {
		type = "BOOLEAN";
	}

	/**
	 * Set the type, depending on the type of the child of the bracket
	 */
	public void setType() {
		if (child instanceof BracketNode) {
			type = ((BracketNode) child).type;
		} else if (child instanceof ArithExpression) {
			setArithmetic();
		} else if (child instanceof BoolExpression) {
			setBoolean();
		}
	}

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @return the value after evaluation
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public int acceptA(IVisitor visitor) throws Exception {
		return visitor.visitA(this);
	}

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @return the value after evaluation
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public boolean acceptB(IVisitor visitor) throws Exception {
		return visitor.visitB(this);
	}
}

/**
 * Class for logical AND node.
 */
class AndNode extends BoolExpression {
	/**
	 * The left child of the node
	 */
	Expression leftChild = null;

	/**
	 * The right child of the node
	 */
	Expression rightChild = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<AndNode> &&\n"
				+ leftChild.Show(level + 1) + rightChild.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoolExpression#accept(IVisitor)
	 */
	@Override
	public boolean accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for greater node.
 */
class GreaterNode extends BoolExpression {
	/**
	 * The left child of the node
	 */
	Expression leftChild = null;

	/**
	 * The right child of the node
	 */
	Expression rightChild = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<GreaterNode> >\n"
				+ leftChild.Show(level + 1) + rightChild.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoolExpression#accept(IVisitor)
	 */
	@Override
	public boolean accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for logical NOT node.
 */
class NotNode extends BoolExpression {
	/**
	 * The child of the node
	 */
	Expression child = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<NotNode> !\n" + child.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoolExpression#accept(IVisitor)
	 */
	@Override
	public boolean accept(IVisitor visitor) throws Exception {
		return visitor.visit(this);
	}
}

/**
 * Class for assignment node.
 */
class AssignmentNode extends Statement {
	/**
	 * The left hand side member (i.e. the value)
	 */
	VarNode variable = null;

	/**
	 * The right hand side member (i.e. the arithmetic expression)
	 */
	Expression value = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<AssignmentNode> =\n"
				+ variable.Show(level + 1) + value.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Statement#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}

/**
 * Class for a block node.
 */
class BlockNode extends Statement {
	/**
	 * The child of the node (i.e. the sequence of statements)
	 */
	Statement statement = null;

	/**
	 * Whether is set, or not
	 */
	boolean isSet = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<BlockNode> {}\n"
				+ (statement != null ? statement.Show(level + 1) : "");

	}

	/**
	 * @param statement
	 */
	public void set(Statement statement) {
		this.isSet = true;
		this.statement = statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Statement#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}

/**
 * The class for if node.
 */
class IfNode extends Statement {
	/**
	 * The condition of if statement
	 */
	BracketNode condition;

	/**
	 * The body (i.e. then branch) of if statement
	 */
	BlockNode thenBranch;

	/**
	 * The else branch of if statement
	 */
	BlockNode elseBrench;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<IfNode> if\n"
				+ condition.Show(level + 1) + thenBranch.Show(level + 1)
				+ elseBrench.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Statement#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}

/**
 * The class of while node.
 */
class WhileNode extends Statement {
	/**
	 * The condition of while statement
	 */
	BracketNode condition;

	/**
	 * The body of while condition
	 */
	BlockNode body;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<WhileNode> while\n"
				+ condition.Show(level + 1) + body.Show(level + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Statement#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}

/**
 * The class for sequence node.
 */
class SequenceNode extends Statement {
	/**
	 * The left child of the node
	 */
	Statement leftChild;

	/**
	 * The right child of the node
	 */
	Statement rightChild;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<SequenceNode>\n"
				+ leftChild.Show(level + 1) + rightChild.Show(level + 1);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Statement#accept(IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}

/**
 * The class for main node.
 */
class MainNode extends Expression {
	/**
	 * The child of the node.
	 */
	Statement child;

	/**
	 * The map of all variables and their values
	 */
	HashMap<String, Integer> varList = new HashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see Expression#Show(int)
	 */
	@Override
	protected String Show(int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++)
			identation.append("\t");

		return identation.toString() + "<MainNode>\n"
				+ (child != null ? child.Show(level + 1) : "");
	}

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public void accept(IVisitor visitor) throws Exception {
		visitor.visit(this);
	}
}