/*******************************************************************************
 * Copyright (c) 2007 University of Illinois at Urbana-Champaign and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     UIUC - Initial API and implementation
 *******************************************************************************/
package fr.inria.verveine.extractor.fortran.ast;

@SuppressWarnings("all")
public class ASTAllocateCoarraySpecNode extends ASTNode
{
    IASTListNode<ASTSectionSubscriptNode> sectionSubscriptList; // in ASTAllocateCoarraySpecNode
    ASTToken hiddenTComma; // in ASTAllocateCoarraySpecNode
    IExpr lb; // in ASTAllocateCoarraySpecNode
    ASTToken hiddenTColon; // in ASTAllocateCoarraySpecNode
    ASTToken isAsterisk; // in ASTAllocateCoarraySpecNode

    public IASTListNode<ASTSectionSubscriptNode> getSectionSubscriptList()
    {
        return this.sectionSubscriptList;
    }

    public void setSectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.sectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IExpr getLb()
    {
        return this.lb;
    }

    public void setLb(IExpr newValue)
    {
        this.lb = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isAsterisk()
    {
        return this.isAsterisk != null;
    }

    public void setIsAsterisk(ASTToken newValue)
    {
        this.isAsterisk = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocateCoarraySpecNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 5;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.sectionSubscriptList;
        case 1:  return this.hiddenTComma;
        case 2:  return this.lb;
        case 3:  return this.hiddenTColon;
        case 4:  return this.isAsterisk;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.sectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.lb = (IExpr)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.isAsterisk = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

