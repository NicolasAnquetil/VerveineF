package fr.inria.verveine.extractor.fortran.ast;

public class ASTFunctionNameNode extends ASTNode {
    ASTToken functionName; // in ASTFunctionNameNode

    public ASTToken getFunctionName()
    {
        return this.functionName;
    }

    public void setFunctionName(ASTToken newValue)
    {
        this.functionName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTFunctionNameNode(this);
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
        case 0:  return this.functionName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.functionName = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
