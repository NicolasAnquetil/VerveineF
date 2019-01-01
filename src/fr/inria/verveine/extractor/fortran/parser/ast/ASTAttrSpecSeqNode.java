package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTAttrSpecSeqNode extends ASTNode {

    ASTToken hiddenTComma;
	ASTAttrSpecNode attrSpec;
	
    public ASTAttrSpecNode getAttrSpec()
    {
        return this.attrSpec;
    }

    public void setAttrSpec(ASTAttrSpecNode newValue)
    {
        this.attrSpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAttrSpecSeqNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 2;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.hiddenTComma;
        case 1:  return this.attrSpec;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.attrSpec = (ASTAttrSpecNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
