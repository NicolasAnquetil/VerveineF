package fr.inria.verveine.extractor.fortran.ast;

public class ASTTypeDeclarationStmtNode extends ASTNode  implements IBlockDataBodyConstruct, IBodyConstruct, IDeclarationConstruct, IHPField, IModuleBodyConstruct, ISpecificationPartConstruct // extends ASTNodeWithErrorRecoverySymbols
{
	private static final int TATTRSPECSEQ = 2;
	public static final int TCOLON = 3;
	public static final int TCOLON2 = 4;
	public static final int TCOMMA = 5;
	public static final int TEOS = 7;

	ASTToken label;
    //ASTTypeSpecNode typeSpec;
    IASTListNode<ASTAttrSpecSeqNode> attrSpecSeq;
    ASTToken hiddenTColon;
    ASTToken hiddenTColon2;
    ASTToken hiddenTComma;
    IASTListNode<ASTEntityDeclNode> entityDeclList;
    ASTToken hiddenTEos;

    public ASTTypeDeclarationStmtNode() {
    	setEntityDeclList(new ASTListNode<>());
    	setAttrSpecSeq(new ASTListNode<>());
    }
    
    public ASTToken getLabel()
    {
        return (ASTToken) getASTField(0);
    }

    public void setLabel(ASTToken newValue)
    {
        setASTField(0, newValue);
    }

/*
    public ASTTypeSpecNode getTypeSpec()
    {
        return this.typeSpec;
    }

    public void setTypeSpec(ASTTypeSpecNode newValue)
    {
        this.typeSpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public IASTListNode<ASTAttrSpecSeqNode> getAttrSpecSeq()
    {
        return this.attrSpecSeq;
    }

    public void setAttrSpecSeq(IASTListNode<ASTAttrSpecSeqNode> newValue)
    {
        this.attrSpecSeq = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTEntityDeclNode> getEntityDeclList()
    {
        return this.entityDeclList;
    }

    public void setEntityDeclList(IASTListNode<ASTEntityDeclNode> newValue)
    {
        this.entityDeclList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTTypeDeclarationStmtNode(this);
        visitor.visitIBlockDataBodyConstruct(this);
        visitor.visitIBodyConstruct(this);
        visitor.visitIDeclarationConstruct(this);
        visitor.visitIHPField(this);
        visitor.visitIModuleBodyConstruct(this);
        visitor.visitISpecificationPartConstruct(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 8;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return new ASTNullNode(); //this.typeSpec;
        case TATTRSPECSEQ:  return this.attrSpecSeq;
        case TCOLON:  return this.hiddenTColon;
        case TCOLON2:  return this.hiddenTColon2;
        case TCOMMA:  return this.hiddenTComma;
        case 6:  return this.entityDeclList;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  return; //this.typeSpec = (ASTTypeSpecNode)value; if (value != null) value.setParent(this); return;
        case TATTRSPECSEQ:  this.attrSpecSeq = (IASTListNode<ASTAttrSpecSeqNode>)value; if (value != null) value.setParent(this); return;
        case TCOLON:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TCOLON2:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TCOMMA:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.entityDeclList = (IASTListNode<ASTEntityDeclNode>)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	Object name = "--noEntityDecl--";
    	if (entityDeclList.size() > 0) {
    		name = this.entityDeclList.iterator().next().getObjectName();
    	}
    	return "ASTTypeDeclarationStmtNode " + name;
    }

}
