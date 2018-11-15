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
public class ASTDataRefNode extends ASTNode
{
    ASTToken hiddenTLparen; // in ASTDataRefNode
    ASTToken name; // in ASTDataRefNode
    IASTListNode<ASTSectionSubscriptNode> primarySectionSubscriptList; // in ASTDataRefNode
    ASTToken hiddenTRparen; // in ASTDataRefNode
    ASTImageSelectorNode imageSelector; // in ASTDataRefNode
    ASTToken hasDerivedTypeComponentName; // in ASTDataRefNode
    ASTNameNode componentName; // in ASTDataRefNode

    public ASTToken getName()
    {
        return this.name;
    }

    public void setName(ASTToken newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTSectionSubscriptNode> getPrimarySectionSubscriptList()
    {
        return this.primarySectionSubscriptList;
    }

    public void setPrimarySectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.primarySectionSubscriptList = newValue;
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


    public boolean hasDerivedTypeComponentName()
    {
        return this.hasDerivedTypeComponentName != null;
    }

    public void setHasDerivedTypeComponentName(ASTToken newValue)
    {
        this.hasDerivedTypeComponentName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTNameNode getComponentName()
    {
        return this.componentName;
    }

    public void setComponentName(ASTNameNode newValue)
    {
        this.componentName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTDataRefNode(this);
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
        case 0:  return this.hiddenTLparen;
        case 1:  return this.name;
        case 2:  return this.primarySectionSubscriptList;
        case 3:  return this.hiddenTRparen;
        case 4:  return this.imageSelector;
        case 5:  return this.hasDerivedTypeComponentName;
        case 6:  return this.componentName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.primarySectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 5:  this.hasDerivedTypeComponentName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.componentName = (ASTNameNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

