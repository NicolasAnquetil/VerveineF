package fr.inria.verveine.extractor.fortran.ast;

import java.util.Iterator;

import java.util.List;

@SuppressWarnings("all")
public class ASTProgramNameNode extends ASTNode
{
	ASTToken programName; // in ASTProgramNameNode

    public ASTToken getProgramName()
    {
        return this.programName;
    }

    public void setProgramName(ASTToken newValue)
    {
        this.programName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTProgramNameNode(this);
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
        case 0:  return this.programName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.programName = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

