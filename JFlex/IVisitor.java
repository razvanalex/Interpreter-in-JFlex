
/**
 * Interface used to define a visitor pattern. The visitor will visit the whole
 * abstract syntax tree, in order to execute the code.
 * 
 * @author razvan
 *
 */
public interface IVisitor {
	/**
	 * Visit an IntNode from AST.
	 * 
	 * @param intNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public int visit(IntNode intNode) throws Exception;

	/**
	 * Visit a VarNode from AST.
	 * 
	 * @param varNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public int visit(VarNode varNode) throws Exception;

	/**
	 * Visit a PlusNode from AST.
	 * 
	 * @param plusNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public int visit(PlusNode plusNode) throws Exception;

	/**
	 * Visit a DivNode from AST.
	 * 
	 * @param divNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public int visit(DivNode divNode) throws Exception;

	/**
	 * Visit a BracketNode from AST.
	 * 
	 * @param bracketNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public int visitA(BracketNode bracketNode) throws Exception;
	
	/**
	 * Visit a BoolNode from AST.
	 * 
	 * @param boolNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public boolean visit(BoolNode boolNode) throws Exception;

	/**
	 * Visit a BracketNode from AST.
	 * 
	 * @param bracketNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public boolean visitB(BracketNode bracketNode) throws Exception;

	/**
	 * Visit an AndNode from AST.
	 * 
	 * @param andNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public boolean visit(AndNode andNode) throws Exception;

	/**
	 * Visit a GreaterNode from AST.
	 * 
	 * @param greaterNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public boolean visit(GreaterNode greaterNode) throws Exception;
	
	/**
	 * Visit a NotNode from AST.
	 * 
	 * @param notNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public boolean visit(NotNode notNode) throws Exception;

	/**
	 * Visit an AssignmentNode from AST.
	 * 
	 * @param assignNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(AssignmentNode assignNode) throws Exception;

	/**
	 * Visit a BlockNode from AST.
	 * 
	 * @param blockNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(BlockNode blockNode) throws Exception;

	/**
	 * Visit an IfNode from AST.
	 * 
	 * @param ifNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(IfNode ifNode) throws Exception;

	/**
	 * Visit a WhileNode from AST.
	 * 
	 * @param whileNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(WhileNode whileNode) throws Exception;

	/**
	 * Visit a SequenceNode from AST.
	 * 
	 * @param seqNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(SequenceNode seqNode) throws Exception;

	/**
	 * Visit a MainNode from AST.
	 * 
	 * @param mainNode
	 *            the node to be visited
	 * @return the value after evaluating the node
	 * @throws Exception
	 *             if there was thrown an exception while interpreting the node
	 */
	public void visit(MainNode mainNode) throws Exception;
}