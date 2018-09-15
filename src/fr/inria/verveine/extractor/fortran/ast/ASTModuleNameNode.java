package fr.inria.verveine.extractor.fortran.ast;

public class ASTModuleNameNode extends ASTNode {

    ASTToken moduleName; // in ASTModuleNameNode

    public ASTToken getModuleName()
    {
        return this.moduleName;
    }

    public void setModuleName(ASTToken newValue)
    {
        this.moduleName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTModuleNameNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 1;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.moduleName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.moduleName = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

}
