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
public class ASTVariableNode extends ASTNode implements IDataStmtObject, IInputItem
{
    IASTListNode<ASTDataRefNode> dataRef; // in ASTVariableNode
    ASTToken stringConst; // in ASTVariableNode
    ASTToken hiddenTLparen; // in ASTVariableNode
    IASTListNode<ASTSectionSubscriptNode> sectionSubscriptList; // in ASTVariableNode
    ASTToken hiddenTRparen; // in ASTVariableNode
    ASTImageSelectorNode imageSelector; // in ASTVariableNode
    ASTSubstringRangeNode substringRange; // in ASTVariableNode

    public IASTListNode<ASTDataRefNode> getDataRef()
    {
        return this.dataRef;
    }

    public void setDataRef(IASTListNode<ASTDataRefNode> newValue)
    {
        this.dataRef = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getStringConst()
    {
        return this.stringConst;
    }

    public void setStringConst(ASTToken newValue)
    {
        this.stringConst = newValue;
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


    public ASTImageSelectorNode getImageSelector()
    {
        return this.imageSelector;
    }

    public void setImageSelector(ASTImageSelectorNode newValue)
    {
        this.imageSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTSubstringRangeNode getSubstringRange()
    {
        return this.substringRange;
    }

    public void setSubstringRange(ASTSubstringRangeNode newValue)
    {
        this.substringRange = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTVariableNode(this);
        visitor.visitIDataStmtObject(this);
        visitor.visitIInputItem(this);
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
        case 0:  return this.dataRef;
        case 1:  return this.stringConst;
        case 2:  return this.hiddenTLparen;
        case 3:  return this.sectionSubscriptList;
        case 4:  return this.hiddenTRparen;
        case 5:  return this.imageSelector;
        case 6:  return this.substringRange;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.dataRef = (IASTListNode<ASTDataRefNode>)value; if (value != null) value.setParent(this); return;
        case 1:  this.stringConst = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.sectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 6:  this.substringRange = (ASTSubstringRangeNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

