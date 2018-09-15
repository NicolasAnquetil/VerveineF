package fr.inria.verveine.extractor.fortran.ast;

public class ASTEndFunctionStmtNode extends ASTNode {
	public static final int TENDFUNCTION = 1;
	public static final int TEND = 2;
	public static final int TFUNCTION = 3;
	public static final int TEOS = 5;

    ASTToken label;
    ASTToken hiddenTEndfunction;
    ASTToken hiddenTEnd;
    ASTToken hiddenTFunction;
    ASTToken endName;
    ASTToken hiddenTEos;

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getEndName()
    {
        return this.endName;
    }

    public void setEndName(ASTToken newValue)
    {
        this.endName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTEndFunctionStmtNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 6;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case TENDFUNCTION:  return this.hiddenTEndfunction;
        case TEND:  return this.hiddenTEnd;
        case TFUNCTION:  return this.hiddenTFunction;
        case 4:  return this.endName;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TENDFUNCTION:  this.hiddenTEndfunction = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEND:  this.hiddenTEnd = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TFUNCTION:  this.hiddenTFunction = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.endName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }


/////////////////////////////////// User Code //////////////////////////////////

 public boolean hasEndFunction() { return hiddenTFunction != null || hiddenTEndfunction != null; }
}
