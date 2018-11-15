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
public class ASTAllocationNode extends ASTNode
{
    IASTListNode<ASTAllocateObjectNode> allocateObject; // in ASTAllocationNode
    ASTToken hasAllocatedShape; // in ASTAllocationNode
    IASTListNode<ASTSectionSubscriptNode> sectionSubscriptList; // in ASTAllocationNode
    ASTToken hiddenTRparen; // in ASTAllocationNode
    ASTToken hiddenTLbracket; // in ASTAllocationNode
    ASTAllocateCoarraySpecNode allocateCoarraySpec; // in ASTAllocationNode
    ASTToken hiddenTRbracket; // in ASTAllocationNode

    public IASTListNode<ASTAllocateObjectNode> getAllocateObject()
    {
        return this.allocateObject;
    }

    public void setAllocateObject(IASTListNode<ASTAllocateObjectNode> newValue)
    {
        this.allocateObject = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean hasAllocatedShape()
    {
        return this.hasAllocatedShape != null;
    }

    public void setHasAllocatedShape(ASTToken newValue)
    {
        this.hasAllocatedShape = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTSectionSubscriptNode> getSectionSubscriptList()
    {
        return this.sectionSubscriptList;
    }

    public void setSectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.sectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTAllocateCoarraySpecNode getAllocateCoarraySpec()
    {
        return this.allocateCoarraySpec;
    }

    public void setAllocateCoarraySpec(ASTAllocateCoarraySpecNode newValue)
    {
        this.allocateCoarraySpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocationNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 7;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.allocateObject;
        case 1:  return this.hasAllocatedShape;
        case 2:  return this.sectionSubscriptList;
        case 3:  return this.hiddenTRparen;
        case 4:  return this.hiddenTLbracket;
        case 5:  return this.allocateCoarraySpec;
        case 6:  return this.hiddenTRbracket;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.allocateObject = (IASTListNode<ASTAllocateObjectNode>)value; if (value != null) value.setParent(this); return;
        case 1:  this.hasAllocatedShape = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.sectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTLbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.allocateCoarraySpec = (ASTAllocateCoarraySpecNode)value; if (value != null) value.setParent(this); return;
        case 6:  this.hiddenTRbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

