package fr.inria.verveine.extractor.fortran.ast;

public class ASTEntityDeclNode extends ASTNode {
	public static final int TNAME = 0;
	public static final int TASTERISK2 = 1;
	public static final int TLPAREN2 = 3;
	public static final int TLPAREN = 4;
	public static final int TRPAREN = 6;
	public static final int TLBRACKET = 7;
	public static final int TRBRACKET = 9;
	public static final int TASTERISK = 10;
	public static final int TSLASH = 12;
	public static final int TSLASH2 = 14;
	public static final int TRPAREN2 = 15;
	
	ASTToken objectName; // in ASTEntityDeclNode
    ASTToken hiddenAsterisk2; // in ASTEntityDeclNode
    //ASTCharLengthNode initialCharLength; // in ASTEntityDeclNode
    ASTToken hiddenLparen2; // in ASTEntityDeclNode
    ASTToken hiddenTLparen; // in ASTEntityDeclNode
    //ASTArraySpecNode arraySpec; // in ASTEntityDeclNode
    ASTToken hiddenTRparen; // in ASTEntityDeclNode
    ASTToken hiddenTLbracket; // in ASTEntityDeclNode
    //ASTCoarraySpecNode coarraySpec; // in ASTEntityDeclNode
    ASTToken hiddenTRbracket; // in ASTEntityDeclNode
    ASTToken hiddenTAsterisk; // in ASTEntityDeclNode
    //ASTCharLengthNode charLength; // in ASTEntityDeclNode
    ASTToken hiddenTSlash; // in ASTEntityDeclNode
    //IASTListNode<ASTDataStmtValueNode> dataStmtValueList; // in ASTEntityDeclNode
    ASTToken hiddenTSlash2; // in ASTEntityDeclNode
    ASTToken hiddenRparen2; // in ASTEntityDeclNode
    //ASTInitializationNode initialization; // in ASTEntityDeclNode

    public ASTToken getObjectName()
    {
        return (ASTToken) getASTField(TNAME);
    }

    public void setObjectName(ASTToken newValue)
    {
        setASTField(TNAME, newValue);
    }


/*    
    public ASTCharLengthNode getInitialCharLength()
    {
        return this.initialCharLength;
    }

    public void setInitialCharLength(ASTCharLengthNode newValue)
    {
        this.initialCharLength = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTArraySpecNode getArraySpec()
    {
        return this.arraySpec;
    }

    public void setArraySpec(ASTArraySpecNode newValue)
    {
        this.arraySpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTCoarraySpecNode getCoarraySpec()
    {
        return this.coarraySpec;
    }

    public void setCoarraySpec(ASTCoarraySpecNode newValue)
    {
        this.coarraySpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTCharLengthNode getCharLength()
    {
        return this.charLength;
    }

    public void setCharLength(ASTCharLengthNode newValue)
    {
        this.charLength = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTDataStmtValueNode> getDataStmtValueList()
    {
        return this.dataStmtValueList;
    }

    public void setDataStmtValueList(IASTListNode<ASTDataStmtValueNode> newValue)
    {
        this.dataStmtValueList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTInitializationNode getInitialization()
    {
        return this.initialization;
    }

    public void setInitialization(ASTInitializationNode newValue)
    {
        this.initialization = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTEntityDeclNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 17;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case TNAME:  return this.objectName;
        case TASTERISK2:  return this.hiddenAsterisk2;
        case 2:  return new ASTNullNode(); // this.initialCharLength;
        case TLPAREN2:  return this.hiddenLparen2;
        case TLPAREN:  return this.hiddenTLparen;
        case 5:  return new ASTNullNode(); // this.arraySpec;
        case TRPAREN:  return this.hiddenTRparen;
        case TLBRACKET:  return this.hiddenTLbracket;
        case 8:  return new ASTNullNode(); // this.coarraySpec;
        case TRBRACKET:  return this.hiddenTRbracket;
        case TASTERISK: return this.hiddenTAsterisk;
        case 11: return new ASTNullNode(); // this.charLength;
        case TSLASH: return this.hiddenTSlash;
        case 13: return new ASTNullNode(); // this.dataStmtValueList;
        case TSLASH2: return this.hiddenTSlash2;
        case TRPAREN2: return this.hiddenRparen2;
        case 16: return new ASTNullNode(); // this.initialization;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case TNAME: this.objectName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TASTERISK2:  this.hiddenAsterisk2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  return ; //this.initialCharLength = (ASTCharLengthNode)value; if (value != null) value.setParent(this); return;
        case TLPAREN2:  this.hiddenLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLPAREN:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return ; //this.arraySpec = (ASTArraySpecNode)value; if (value != null) value.setParent(this); return;
        case TRPAREN:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TLBRACKET:  this.hiddenTLbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  return ; //this.coarraySpec = (ASTCoarraySpecNode)value; if (value != null) value.setParent(this); return;
        case TRBRACKET:  this.hiddenTRbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TASTERISK: this.hiddenTAsterisk = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 11: return ; //this.charLength = (ASTCharLengthNode)value; if (value != null) value.setParent(this); return;
        case TSLASH: this.hiddenTSlash = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 13: return ; //this.dataStmtValueList = (IASTListNode<ASTDataStmtValueNode>)value; if (value != null) value.setParent(this); return;
        case TSLASH2: this.hiddenTSlash2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TRPAREN2: this.hiddenRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 16: return ; //this.initialization = (ASTInitializationNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTEntityDeclNode " + objectName;
    }

}
