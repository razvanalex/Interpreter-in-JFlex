import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Main {
    static private final String INPUT = "input";
    static private final String OUTPUT = "arbore-b";

    public static void main(String[] args) throws IOException {
        ImpLexer lexer = null;
        CommonTokenStream tokenStream = null;
        ImpParser parser = null;
        ParserRuleContext globalTree = null;

        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;

        // Deschidem fisierul input pentru a incepe parsarea
        String fileName = INPUT;
        CharStream input = CharStreams.fromFileName(fileName);

        // Definim Lexer-ul
        lexer = new ImpLexer(input);

        // Obtinem tokenii din input
        tokenStream = new CommonTokenStream(lexer);

        // Definim Parser-ul
        parser = new ImpParser(tokenStream);

        // Incepem parsarea
        ParserRuleContext tree = parser.main();

        // Vizitam AST-ul
        MyVisitor visitor = new MyVisitor(OUTPUT);
        visitor.visit(tree);
        visitor.finish();
    }
}
