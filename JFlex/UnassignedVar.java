/**
 * Exception class for reporting Unassigned variable error. The parameter
 * {@code line} is used to identify the line from the input file, where the
 * error was reported.
 * 
 * @author razvan
 *
 */
public class UnassignedVar extends Exception {
	/**
	 * Serial Version UID generated randomly.
	 */
	private static final long serialVersionUID = 5760305026640316100L;

	/**
	 * Line from code where the error was reported.
	 */
	public int line = -1;

	/**
	 * Throw an error for unassigned variable exception.
	 * 
	 * @param line
	 *            where the error is thrown
	 */
	public UnassignedVar(int line) {
		this.line = line;
	}
}