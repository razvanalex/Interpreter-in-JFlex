build:
	java -Xmx500M -cp ".:./antlr-4.7.1-complete.jar" org.antlr.v4.Tool Imp.g4 -visitor
	javac -cp ".:./antlr-4.7.1-complete.jar" Imp*.java MyVisitor.java Main.java

run:
	java -cp ".:./antlr-4.7.1-complete.jar" Main

.PHONY: clean
clean:
	rm -f *.class *.interp *.tokens Imp*.java 
