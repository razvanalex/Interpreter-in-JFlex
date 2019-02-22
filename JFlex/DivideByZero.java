/**
 * Exception class for reporting Division by zero error. The parameter
 * {@code line} is used to identify the line from the input file, where the
 * error was reported.
 * 
 * @author razvan
 * 
 */
public class DivideByZero extends Exception {
	/**
	 * Serial Version UID generated randomly.
	 */
	private static final long serialVersionUID = 929113530856719587L;

	/**
	 * Line from code where the error was reported.
	 */
	public int line = -1;

	/**
	 * Throw an error for division by zero exception.
	 * 
	 * @param line
	 *            where the error is thrown
	 */
	public DivideByZero(int line) {
		this.line = line;
	}
}