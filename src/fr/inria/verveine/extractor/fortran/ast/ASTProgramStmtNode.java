package fr.inria.verveine.extractor.fortran.ast;

public class ASTProgramStmtNode extends ASTNode
{
	public static final int TPROGRAM = 1;
	public static final int TEOS = 3;
	
	ASTToken label;
    ASTToken programToken;
    ASTToken programName;
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


    public ASTToken getProgramToken()
    {
        return this.programToken;
    }

    public void setProgramToken(ASTToken newValue)
    {
        this.programToken = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getProgramName()
    {
        return this.programName;
    }

    public void setProgramName(ASTToken astToken)
    {
        this.programName = astToken;
        if (astToken != null) astToken.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTProgramStmtNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 4;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case TPROGRAM:  return this.programToken;
        case 2:  return this.programName;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TPROGRAM:  this.programToken = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.programName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

