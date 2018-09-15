package fr.inria.verveine.extractor.fortran.ast;

public class ASTEndProgramStmtNode extends ASTNode {

	public static final int TPROGRAM = 2;
	public static final int TEOS = 4;
	
    ASTToken label;
    ASTToken endToken;
    ASTToken hiddenTProgram;
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


    public ASTToken getEndToken()
    {
        return this.endToken;
    }

    public void setEndToken(ASTToken newValue)
    {
        this.endToken = newValue;
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
        visitor.visitASTEndProgramStmtNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 5;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.endToken;
        case TPROGRAM:  return this.hiddenTProgram;
        case 3:  return this.endName;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.endToken = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TPROGRAM:  this.hiddenTProgram = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.endName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

}
