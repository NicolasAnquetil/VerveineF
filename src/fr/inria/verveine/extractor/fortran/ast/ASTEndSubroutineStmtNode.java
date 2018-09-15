package fr.inria.verveine.extractor.fortran.ast;

public class ASTEndSubroutineStmtNode extends ASTNode {
	public static final int TEND =1 ;
	public static final int TENDSUBROUT =2 ;
	public static final int TSUBROUT =3 ;
	public static final int TEOS =5 ;

    ASTToken label; // in ASTEndSubroutineStmtNode
    ASTToken hiddenTEnd; // in ASTEndSubroutineStmtNode
    ASTToken hiddenTEndsubroutine; // in ASTEndSubroutineStmtNode
    ASTToken hiddenTSubroutine; // in ASTEndSubroutineStmtNode
    ASTToken endName; // in ASTEndSubroutineStmtNode
    ASTToken hiddenTEos; // in ASTEndSubroutineStmtNode

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
        visitor.visitASTEndSubroutineStmtNode(this);
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
        case TENDSUBROUT:  return this.hiddenTEndsubroutine;
        case TSUBROUT:  return this.hiddenTSubroutine;
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
        case TENDSUBROUT:  this.hiddenTEndsubroutine = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TSUBROUT:  this.hiddenTSubroutine = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.endName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }


/////////////////////////////////// User Code //////////////////////////////////

 public boolean hasEndSubroutine() { return hiddenTSubroutine != null || hiddenTEndsubroutine != null; }
}
