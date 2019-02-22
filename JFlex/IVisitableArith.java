/**
 * Interface to visit arithmetic expressions.
 * 
 * @author razvan
 *
 */
public interface IVisitableArith {

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @return the evaluation of the node
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public int accept(IVisitor visitor) throws Exception;
}