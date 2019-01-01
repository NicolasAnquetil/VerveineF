package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTFunctionStmtNode extends ASTNode implements IActionStmt {  // extends ASTNodeWithErrorRecoverySymbols
	public static final int TFUNCTION =2 ;
	public static final int TLPAREN =4 ;
	public static final int TRPAREN =6 ;
	public static final int TBIND8 =7 ;
	public static final int TLPAREN8 =8 ;
	public static final int TIDENT8 =9 ;
	public static final int TRPAREN8 =10 ;
	public static final int TLPAREN2 =12 ;
	public static final int TRPAREN2 =14 ;
	public static final int TBIND9 =15 ;
	public static final int TLPAREN9 =16 ;
	public static final int TIDENT9 =17 ;
	public static final int TRPAREN9 =18 ;
	public static final int TEOS =19 ;

    ASTToken label; // in ASTFunctionStmtNode
    //IASTListNode<ASTPrefixSpecNode> prefixSpecList; // in ASTFunctionStmtNode
    ASTToken hiddenTFunction; // in ASTFunctionStmtNode
    ASTToken functionName; // in ASTFunctionStmtNode
    ASTToken hiddenTLparen; // in ASTFunctionStmtNode
    //IASTListNode<ASTFunctionParNode> functionPars; // in ASTFunctionStmtNode
    ASTToken hiddenTRparen; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenTBind8; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenLParen8; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenTIdent8; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenRParen8; // in ASTFunctionStmtNode
    ASTToken hasResultClause; // in ASTFunctionStmtNode
    ASTToken hiddenTLparen2; // in ASTFunctionStmtNode
    ASTToken name; // in ASTFunctionStmtNode
    ASTToken hiddenTRparen2; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenTBind9; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenLParen9; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenTIdent9; // in ASTFunctionStmtNode
    ASTToken hiddenHiddenRParen9; // in ASTFunctionStmtNode
    ASTToken hiddenTEos; // in ASTFunctionStmtNode

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

    public ASTToken getFunctionName()
    {
        return this.functionName;
    }

    public void setFunctionName(ASTToken newValue)
    {
        this.functionName = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTFunctionParNode> getFunctionPars()
    {
        return this.functionPars;
    }

    public void setFunctionPars(IASTListNode<ASTFunctionParNode> newValue)
    {
        this.functionPars = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public boolean hasResultClause()
    {
        return this.hasResultClause != null;
    }

    public void setHasResultClause(ASTToken newValue)
    {
        this.hasResultClause = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getName()
    {
        return this.name;
    }

    public void setName(ASTToken newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTFunctionStmtNode(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 20;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return new ASTNullNode(); // this.prefixSpecList;
        case TFUNCTION:  return this.hiddenTFunction;
        case 3:  return this.functionName;
        case TLPAREN:  return this.hiddenTLparen;
        case 5:  return new ASTNullNode(); // this.functionPars;
        case TRPAREN:  return this.hiddenTRparen;
        case TBIND8:  return this.hiddenHiddenTBind8;
        case TLPAREN8:  return this.hiddenHiddenLParen8;
        case TIDENT8:  return this.hiddenHiddenTIdent8;
        case TRPAREN8: return this.hiddenHiddenRParen8;
        case 11: return this.hasResultClause;
        case TLPAREN2: return this.hiddenTLparen2;
        case 13: return this.name;
        case TRPAREN2: return this.hiddenTRparen2;
        case TBIND9: return this.hiddenHiddenTBind9;
        case TLPAREN9: return this.hiddenHiddenLParen9;
        case TIDENT9: return this.hiddenHiddenTIdent9;
        case TRPAREN9: return this.hiddenHiddenRParen9;
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
        case TFUNCTION:  this.hiddenTFunction = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.functionName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return; // this.functionPars = (IASTListNode<ASTFunctionParNode>)value; if (value != null) value.setParent(this); return;
        case TRPAREN:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TBIND8:  this.hiddenHiddenTBind8 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN8:  this.hiddenHiddenLParen8 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TIDENT8:  this.hiddenHiddenTIdent8 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TRPAREN8: this.hiddenHiddenRParen8 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 11: this.hasResultClause = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN2: this.hiddenTLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 13: this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TRPAREN2: this.hiddenTRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TBIND9: this.hiddenHiddenTBind9 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN9: this.hiddenHiddenLParen9 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TIDENT9: this.hiddenHiddenTIdent9 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TRPAREN9: this.hiddenHiddenRParen9 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS: this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
