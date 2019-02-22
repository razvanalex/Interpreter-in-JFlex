import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.Integer;

class MyVisitor extends ImpBaseVisitor<Integer> {
    private StringBuilder outputContent;
    private BufferedWriter bw;
    private int tabs = 0;

    public MyVisitor(String fileName) {
        outputContent = new StringBuilder();

        try {
            bw = new BufferedWriter(new FileWriter(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Functie rudimentara pentru a printa tab-uri
    private void printTabs() {
        for (int i = 0; i < this.tabs; i++) {
            outputContent.append("\t");
        }
    }

    @Override 
    public Integer visitMain(ImpParser.MainContext ctx) {
        this.printTabs();
        outputContent.append("<MainNode>\n");

        this.tabs++;
        visit(ctx.stmt());
        this.tabs--;

        return 0;
    }
    
    @Override 
    public Integer visitEmptyBlock(ImpParser.EmptyBlockContext ctx) {
        this.printTabs();
        outputContent.append("<BlockNode> {}\n");

        return 0;
    }

    @Override 
    public Integer visitNonEmptyBlock(ImpParser.NonEmptyBlockContext ctx) {
        this.printTabs();
        outputContent.append("<BlockNode> {}\n");

        this.tabs++;
        visit(ctx.stmt());
        this.tabs--;

        return 0;
    }


    @Override 
    public Integer visitWhileNode(ImpParser.WhileNodeContext ctx) { 
        this.printTabs();
        outputContent.append("<WhileNode> while\n");

        this.tabs++;
        visit(ctx.cond);
        this.tabs--;

        this.tabs++;
        visit(ctx.bodyB);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitIfNode(ImpParser.IfNodeContext ctx) {
        this.printTabs();
        outputContent.append("<IfNode> if\n");

        this.tabs++;
        visit(ctx.cond);
        this.tabs--;

        this.tabs++;
        visit(ctx.bodyB);
        this.tabs--;

        this.tabs++;
        visit(ctx.elseB);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitAssignNode(ImpParser.AssignNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<AssignmentNode> =\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitSequenceNode(ImpParser.SequenceNodeContext ctx) {
        this.printTabs();

        outputContent.append("<SequenceNode>\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitBoolBracketCond(ImpParser.BoolBracketCondContext ctx) { 
        this.printTabs();

        outputContent.append("<BracketNode> ()\n");

        this.tabs++;
        visit(ctx.child);
        this.tabs--;

        return 0;
    }


    @Override
    public Integer visitIntNode(ImpParser.IntNodeContext ctx) {
        this.printTabs();
        outputContent.append("<IntNode> " + ctx.getText() + "\n");
        
        return 0;
    }

    @Override 
    public Integer visitArithBracket(ImpParser.ArithBracketContext ctx) { 
        this.printTabs();

        outputContent.append("<BracketNode> ()\n");

        this.tabs++;
        visit(ctx.child);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitDivNode(ImpParser.DivNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<DivNode> /\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitPlusNode(ImpParser.PlusNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<PlusNode> +\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitBoolNode(ImpParser.BoolNodeContext ctx) { 
         this.printTabs();
         outputContent.append("<BoolNode> " + ctx.getText() + "\n");
         
         return 0;
    }

    @Override 
    public Integer visitBoolBracket(ImpParser.BoolBracketContext ctx) { 
        this.printTabs();

        outputContent.append("<BracketNode> ()\n");

        this.tabs++;
        visit(ctx.child);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitAndNode(ImpParser.AndNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<AndNode> &&\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitGreaterNode(ImpParser.GreaterNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<GreaterNode> >\n");

        this.tabs++;
        visit(ctx.left);
        this.tabs--;

        this.tabs++;
        visit(ctx.right);
        this.tabs--;

        return 0;
    }

    @Override 
    public Integer visitNotNode(ImpParser.NotNodeContext ctx) { 
        this.printTabs();

        outputContent.append("<NotNode> !\n");

        this.tabs++;
        visit(ctx.child);
        this.tabs--;

        return 0;
    }


    @Override 
    public Integer visitVar(ImpParser.VarContext ctx) { 
        this.printTabs();
        outputContent.append("<VariableNode> " + ctx.getText() + "\n");

        return 0;
    }

    public void finish() {
        try {
            bw.write(outputContent.toString());
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
