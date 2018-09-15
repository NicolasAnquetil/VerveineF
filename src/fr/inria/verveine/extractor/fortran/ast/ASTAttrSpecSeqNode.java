package fr.inria.verveine.extractor.fortran.ast;

public class ASTAttrSpecSeqNode extends ASTNode {

	ASTAttrSpecNode attrSpec; // in ASTAttrSpecSeqNode
	
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
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 2;
    }

    @Override protected ASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return new ASTNullNode();//this.hiddenTComma;
        case 1:  return this.attrSpec;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  return; //this.hiddenTComma = (org.eclipse.photran.internal.core.lexer.Token)value; if (value != null) value.setParent(this); return;
        case 1:  this.attrSpec = (ASTAttrSpecNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
