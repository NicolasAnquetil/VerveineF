package fr.inria.verveine.extractor.fortran.ast;

public class ASTTypeDeclarationStmtNode extends ASTNode  implements IBlockDataBodyConstruct, IBodyConstruct, IDeclarationConstruct, IHPField, IModuleBodyConstruct, ISpecificationPartConstruct // extends ASTNodeWithErrorRecoverySymbols
{
	ASTToken label; // in ASTTypeDeclarationStmtNode
    //ASTTypeSpecNode typeSpec; // in ASTTypeDeclarationStmtNode
    IASTListNode<ASTAttrSpecSeqNode> attrSpecSeq; // in ASTTypeDeclarationStmtNode
    ASTToken hiddenTColon; // in ASTTypeDeclarationStmtNode
    ASTToken hiddenTColon2; // in ASTTypeDeclarationStmtNode
    ASTToken hiddenTComma; // in ASTTypeDeclarationStmtNode
    IASTListNode<ASTEntityDeclNode> entityDeclList; // in ASTTypeDeclarationStmtNode
    ASTToken hiddenTEos; // in ASTTypeDeclarationStmtNode

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
        case 2:  return this.attrSpecSeq;
        case 3:  return this.hiddenTColon;
        case 4:  return this.hiddenTColon2;
        case 5:  return this.hiddenTComma;
        case 6:  return this.entityDeclList;
        case 7:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  return; //this.typeSpec = (ASTTypeSpecNode)value; if (value != null) value.setParent(this); return;
        case 2:  this.attrSpecSeq = (IASTListNode<ASTAttrSpecSeqNode>)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.entityDeclList = (IASTListNode<ASTEntityDeclNode>)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
