package fr.inria.verveine.extractor.fortran.parser.ast;
/**
 * 
 * 
 *
 */
@SuppressWarnings("all")
public class ASTCallStmtNode extends ASTNode implements IActionStmt
{
	public static final int TCALL = 1;
    public static final int TEOS = 7;
    
	ASTToken label; // in ASTCallStmtNode
    ASTToken hiddenTCall; // in ASTCallStmtNode
    ASTToken subroutineName; // in ASTCallStmtNode
    //IASTListNode<ASTDerivedTypeQualifiersNode> derivedTypeQualifiers; // in ASTCallStmtNode
    //ASTToken hiddenTLparen; // in ASTCallStmtNode
    //IASTListNode<ASTSubroutineArgNode> argList; // in ASTCallStmtNode
    //ASTToken hiddenTRparen; // in ASTCallStmtNode
    ASTToken hiddenTEos; // in ASTCallStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getSubroutineName()
    {
        return this.subroutineName;
    }

    public void setSubroutineName(ASTToken newValue)
    {
        this.subroutineName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    /*public IASTListNode<ASTDerivedTypeQualifiersNode> getDerivedTypeQualifiers()
    {
        return this.derivedTypeQualifiers;
    }

    public void setDerivedTypeQualifiers(IASTListNode<ASTDerivedTypeQualifiersNode> newValue)
    {
        this.derivedTypeQualifiers = newValue;
        if (newValue != null) newValue.setParent(this);
    }*/


    /*public IASTListNode<ASTSubroutineArgNode> getArgList()
    {
        return this.argList;
    }

    public void setArgList(IASTListNode<ASTSubroutineArgNode> newValue)
    {
        this.argList = newValue;
        if (newValue != null) newValue.setParent(this);
    }*/


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTCallStmtNode(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitASTNode(this);
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
        case TCALL:  return this.hiddenTCall;
        case 2:  return this.subroutineName;
        case 3:  return new ASTNullNode();  // this.derivedTypeQualifiers;
        case 4:  return new ASTNullNode();  // this.hiddenTLparen;
        case 5:  return new ASTNullNode();  // this.argList;
        case 6:  return new ASTNullNode();  // this.hiddenTRparen;
        case 7:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TCALL:  this.hiddenTCall = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.subroutineName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return;  // this.derivedTypeQualifiers = (IASTListNode<ASTDerivedTypeQualifiersNode>)value; if (value != null) value.setParent(this); return;
        case 4:  return;  // this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return;  // this.argList = (IASTListNode<ASTSubroutineArgNode>)value; if (value != null) value.setParent(this); return;
        case 6:  return;  // this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTCallStmtNode " + subroutineName;
    }

}

