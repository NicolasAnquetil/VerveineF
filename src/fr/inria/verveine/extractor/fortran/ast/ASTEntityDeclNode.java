package fr.inria.verveine.extractor.fortran.ast;

public class ASTEntityDeclNode extends ASTNode {
	//ASTObjectNameNode objectName; // in ASTEntityDeclNode
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

/*
    public ASTEntityDeclNode(IASTNode parent) {
    	super(parent);
    }
    
    public ASTObjectNameNode getObjectName()
    {
        return this.objectName;
    }

    public void setObjectName(ASTObjectNameNode newValue)
    {
        this.objectName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


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
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 17;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return new ASTNullNode(); // this.objectName;
        case 1:  return this.hiddenAsterisk2;
        case 2:  return new ASTNullNode(); // this.initialCharLength;
        case 3:  return this.hiddenLparen2;
        case 4:  return this.hiddenTLparen;
        case 5:  return new ASTNullNode(); // this.arraySpec;
        case 6:  return this.hiddenTRparen;
        case 7:  return this.hiddenTLbracket;
        case 8:  return new ASTNullNode(); // this.coarraySpec;
        case 9:  return this.hiddenTRbracket;
        case 10: return this.hiddenTAsterisk;
        case 11: return new ASTNullNode(); // this.charLength;
        case 12: return this.hiddenTSlash;
        case 13: return new ASTNullNode(); // this.dataStmtValueList;
        case 14: return this.hiddenTSlash2;
        case 15: return this.hiddenRparen2;
        case 16: return new ASTNullNode(); // this.initialization;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  return ; //this.objectName = (ASTObjectNameNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenAsterisk2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  return ; //this.initialCharLength = (ASTCharLengthNode)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return ; //this.arraySpec = (ASTArraySpecNode)value; if (value != null) value.setParent(this); return;
        case 6:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTLbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  return ; //this.coarraySpec = (ASTCoarraySpecNode)value; if (value != null) value.setParent(this); return;
        case 9:  this.hiddenTRbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 10: this.hiddenTAsterisk = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 11: return ; //this.charLength = (ASTCharLengthNode)value; if (value != null) value.setParent(this); return;
        case 12: this.hiddenTSlash = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 13: return ; //this.dataStmtValueList = (IASTListNode<ASTDataStmtValueNode>)value; if (value != null) value.setParent(this); return;
        case 14: this.hiddenTSlash2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 15: this.hiddenRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 16: return ; //this.initialization = (ASTInitializationNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
