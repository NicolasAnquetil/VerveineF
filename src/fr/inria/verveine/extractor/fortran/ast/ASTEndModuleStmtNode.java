package fr.inria.verveine.extractor.fortran.ast;

public class ASTEndModuleStmtNode extends ASTNode {

	public static final int TEND = 1;
	public static final int TENDMODULE = 2;
	public static final int TMODULE = 3;
	public static final int TEOS = 5;

    ASTToken label;
    ASTToken hiddenTEnd;
    ASTToken hiddenTEndmodule;
    ASTToken hiddenTModule;
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
        visitor.visitASTEndModuleStmtNode(this);
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
        case TEND:  return this.hiddenTEnd;
        case TENDMODULE:  return this.hiddenTEndmodule;
        case TMODULE:  return this.hiddenTModule;
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
        case TEND:  this.hiddenTEnd = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TENDMODULE:  this.hiddenTEndmodule = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TMODULE:  this.hiddenTModule = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.endName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }


/////////////////////////////////// User Code //////////////////////////////////

 public boolean hasEndModule() { return hiddenTModule != null || hiddenTEndmodule != null; }

}
