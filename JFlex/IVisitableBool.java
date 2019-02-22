/**
 * Interface to visit boolean expressions.
 * 
 * @author razvan
 *
 */
public interface IVisitableBool {

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @return the evaluation of the node
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public boolean accept(IVisitor visitor) throws Exception;
}