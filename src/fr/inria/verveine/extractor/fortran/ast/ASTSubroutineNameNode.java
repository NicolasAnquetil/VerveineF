package fr.inria.verveine.extractor.fortran.ast;

public class ASTSubroutineNameNode extends ASTNode {
    ASTToken subroutineName; // in ASTSubroutineNameNode

    public ASTToken getSubroutineName()
    {
        return this.subroutineName;
    }

    public void setSubroutineName(ASTToken newValue)
    {
        this.subroutineName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTSubroutineNameNode(this);
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
        case 0:  return this.subroutineName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.subroutineName = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
