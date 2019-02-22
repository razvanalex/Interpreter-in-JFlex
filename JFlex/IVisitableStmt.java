/**
 * Interface to visit statements.
 * 
 * @author razvan
 *
 */
public interface IVisitableStmt {

	/**
	 * Accept the visitor.
	 * 
	 * @param visitor
	 *            to be accepted
	 * @throws Exception
	 *             if there was thrown an exception
	 */
	public void accept(IVisitor visitor) throws Exception;
}