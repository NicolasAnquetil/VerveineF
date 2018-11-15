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
public class ASTFieldSelectorNode extends ASTNode
{
    ASTToken hiddenTLparen; // in ASTFieldSelectorNode
    IASTListNode<ASTSectionSubscriptNode> sectionSubscriptList; // in ASTFieldSelectorNode
    ASTToken hiddenTRparen; // in ASTFieldSelectorNode
    ASTImageSelectorNode imageSelector; // in ASTFieldSelectorNode
    ASTToken hasDerivedTypeComponentRef; // in ASTFieldSelectorNode
    ASTToken name; // in ASTFieldSelectorNode

    public IASTListNode<ASTSectionSubscriptNode> getSectionSubscriptList()
    {
        return this.sectionSubscriptList;
    }

    public void setSectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.sectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTImageSelectorNode getImageSelector()
    {
        return this.imageSelector;
    }

    public void setImageSelector(ASTImageSelectorNode newValue)
    {
        this.imageSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean hasDerivedTypeComponentRef()
    {
        return this.hasDerivedTypeComponentRef != null;
    }

    public void setHasDerivedTypeComponentRef(ASTToken newValue)
    {
        this.hasDerivedTypeComponentRef = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getName()
    {
        return this.name;
    }

    public void setName(ASTToken newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTFieldSelectorNode(this);
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
        case 0:  return this.hiddenTLparen;
        case 1:  return this.sectionSubscriptList;
        case 2:  return this.hiddenTRparen;
        case 3:  return this.imageSelector;
        case 4:  return this.hasDerivedTypeComponentRef;
        case 5:  return this.name;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.sectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 4:  this.hasDerivedTypeComponentRef = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

