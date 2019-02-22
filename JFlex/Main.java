import java.io.*;
import java.util.*;

/**
 * Main class that has the starting point.
 */
public class Main {
	static private String inputFile = "input";
	static private String treeFileJFLEX = "arbore";
	static private String outputFile = "output";

	public static void main(String[] args) throws IOException {
		Lexer l = new Lexer(new FileReader(inputFile));
		
		// Parse the file
		l.yylex();

		if (!l.stack.isEmpty() && l.stack.peek() instanceof MainNode) {
			MainNode mainNode = (MainNode) l.stack.peek();
			BufferedWriter bw = null;
			BufferedWriter bw2 = null;

			// Write the tree to output file and then run the program
			try {
				bw = new BufferedWriter(new FileWriter(treeFileJFLEX));
				bw2 = new BufferedWriter(new FileWriter(outputFile));

				String content = mainNode.Show();
				bw.write(content);
				if (!l.compiationError) {
					mainNode.accept(new ExpressionVisitor());

					// Print the variable and their values
					for (Map.Entry<String, Integer> e : mainNode.varList
							.entrySet()) {
						String var = e.getKey();
						Integer val = e.getValue();
						String line = var + "=" + val + "\n";
						bw2.write(line);
					}
				} else {
					String line = "UnassignedVar " + l.errorLine + "\n";
					bw2.write(line);
				}

			} catch (UnassignedVar e) {
				String line = "UnassignedVar " + e.line + "\n";
				bw2.write(line);
			} catch (DivideByZero e) {
				String line = "DivideByZero " + e.line + "\n";
				bw2.write(line);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bw.close();
				bw2.close();
			}
		}
	}
}