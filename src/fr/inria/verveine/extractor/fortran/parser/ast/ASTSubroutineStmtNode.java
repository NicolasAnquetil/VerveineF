package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTSubroutineStmtNode extends ASTNode implements IActionStmt /* extends ASTNodeWithErrorRecoverySymbols*/ {
	public static final int TSUBROUT =2 ;
	public static final int TLPAREN =4 ;
	public static final int TRPAREN =6 ;
	public static final int TBIND =7 ;
	public static final int TLPAREN2 =8 ;
	public static final int TIDENT =9 ;
	public static final int TRPAREN2 =10 ;
	public static final int TEOS =11 ;

	ASTToken label; // in ASTSubroutineStmtNode
    //IASTListNode<ASTPrefixSpecNode> prefixSpecList; // in ASTSubroutineStmtNode
    ASTToken hiddenTSubroutine; // in ASTSubroutineStmtNode
    ASTToken subroutineName; // in ASTSubroutineStmtNode
    ASTToken hiddenTLparen; // in ASTSubroutineStmtNode
    //IASTListNode<ASTSubroutineParNode> subroutinePars; // in ASTSubroutineStmtNode
    ASTToken hiddenTRparen; // in ASTSubroutineStmtNode
    ASTToken hiddenTBind; // in ASTSubroutineStmtNode
    ASTToken hiddenTLparen2; // in ASTSubroutineStmtNode
    ASTToken hiddenTIdent; // in ASTSubroutineStmtNode
    ASTToken hiddenTRparen2; // in ASTSubroutineStmtNode
    ASTToken hiddenTEos; // in ASTSubroutineStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTPrefixSpecNode> getPrefixSpecList()
    {
        return this.prefixSpecList;
    }

    public void setPrefixSpecList(IASTListNode<ASTPrefixSpecNode> newValue)
    {
        this.prefixSpecList = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public ASTToken getSubroutineName()
    {
        return this.subroutineName;
    }

    public void setSubroutineName(ASTToken newValue)
    {
        this.subroutineName = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTSubroutineParNode> getSubroutinePars()
    {
        return this.subroutinePars;
    }

    public void setSubroutinePars(IASTListNode<ASTSubroutineParNode> newValue)
    {
        this.subroutinePars = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTSubroutineStmtNode(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 12;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return new ASTNullNode(); // this.prefixSpecList;
        case TSUBROUT:  return this.hiddenTSubroutine;
        case 3:  return this.subroutineName;
        case TLPAREN:  return this.hiddenTLparen;
        case 5:  return new ASTNullNode(); // this.subroutinePars;
        case TRPAREN:  return this.hiddenTRparen;
        case TBIND:  return this.hiddenTBind;
        case TLPAREN2:  return this.hiddenTLparen2;
        case TIDENT:  return this.hiddenTIdent;
        case TRPAREN2: return this.hiddenTRparen2;
        case TEOS: return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  return; // this.prefixSpecList = (IASTListNode<ASTPrefixSpecNode>)value; if (value != null) value.setParent(this); return;
        case TSUBROUT:  this.hiddenTSubroutine = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.subroutineName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return; // this.subroutinePars = (IASTListNode<ASTSubroutineParNode>)value; if (value != null) value.setParent(this); return;
        case TRPAREN:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TBIND:  this.hiddenTBind = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN2:  this.hiddenTLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TIDENT:  this.hiddenTIdent = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TRPAREN2: this.hiddenTRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS: this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTSubroutineStmtNode " + subroutineName;
    }

}
