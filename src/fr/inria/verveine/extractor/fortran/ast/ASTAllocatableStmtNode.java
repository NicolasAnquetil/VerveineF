package fr.inria.verveine.extractor.fortran.ast;

import java.io.PrintStream;
import java.util.Iterator;

import java.util.List;




@SuppressWarnings("all")
public class ASTAllocatableStmtNode extends ASTNode implements ISpecificationStmt
{
    ASTToken label; // in ASTAllocatableStmtNode
    ASTToken hiddenTAllocatable; // in ASTAllocatableStmtNode
    ASTToken hiddenTColon; // in ASTAllocatableStmtNode
    ASTToken hiddenTColon2; // in ASTAllocatableStmtNode
    IASTListNode<ASTArrayAllocationNode> arrayAllocationList; // in ASTAllocatableStmtNode
    ASTToken hiddenTEos; // in ASTAllocatableStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTArrayAllocationNode> getArrayAllocationList()
    {
        return this.arrayAllocationList;
    }

    public void setArrayAllocationList(IASTListNode<ASTArrayAllocationNode> newValue)
    {
        this.arrayAllocationList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocatableStmtNode(this);
        visitor.visitISpecificationStmt(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 6;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.hiddenTAllocatable;
        case 2:  return this.hiddenTColon;
        case 3:  return this.hiddenTColon2;
        case 4:  return this.arrayAllocationList;
        case 5:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTAllocatable = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.arrayAllocationList = (IASTListNode<ASTArrayAllocationNode>)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

