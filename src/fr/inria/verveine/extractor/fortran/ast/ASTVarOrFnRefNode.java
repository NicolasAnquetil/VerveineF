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

import java.io.PrintStream;
import java.util.Iterator;

import java.util.List;

@SuppressWarnings("all")
public class ASTVarOrFnRefNode extends ASTNode
{
    private static final int TOTAL_NBR_FIELDS = 1;  // only interest in the name
	ASTToken name;
    ASTToken hiddenTLparen;
    //IASTListNode<ASTSectionSubscriptNode> primarySectionSubscriptList;
    //IASTListNode<ASTFunctionArgListNode> functionArgList;
    ASTToken hiddenTRparen;
    ASTToken hiddenTPercent;
    //ASTSubstringRangeNode substringRange;
    //ASTImageSelectorNode imageSelector;
    ASTToken hiddenHiddenPercent2;
    //IASTListNode<ASTDataRefNode> derivedTypeComponentRef;
    ASTToken hiddenLparen2;
    //IASTListNode<ASTSectionSubscriptNode> componentSectionSubscriptList;
    ASTToken hiddenRparen2;
    //ASTSubstringRangeNode substringRange2;

    public ASTToken getName()
    {
        return this.name;
    }

    public void setName(ASTToken newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTSectionSubscriptNode> getPrimarySectionSubscriptList()
    {
        return this.primarySectionSubscriptList;
    }

    public void setPrimarySectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.primarySectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTFunctionArgListNode> getFunctionArgList()
    {
        return this.functionArgList;
    }

    public void setFunctionArgList(IASTListNode<ASTFunctionArgListNode> newValue)
    {
        this.functionArgList = newValue;
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


    public ASTImageSelectorNode getImageSelector()
    {
        return this.imageSelector;
    }

    public void setImageSelector(ASTImageSelectorNode newValue)
    {
        this.imageSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTDataRefNode> getDerivedTypeComponentRef()
    {
        return this.derivedTypeComponentRef;
    }

    public void setDerivedTypeComponentRef(IASTListNode<ASTDataRefNode> newValue)
    {
        this.derivedTypeComponentRef = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTSectionSubscriptNode> getComponentSectionSubscriptList()
    {
        return this.componentSectionSubscriptList;
    }

    public void setComponentSectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.componentSectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTSubstringRangeNode getSubstringRange2()
    {
        return this.substringRange2;
    }

    public void setSubstringRange2(ASTSubstringRangeNode newValue)
    {
        this.substringRange2 = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTVarOrFnRefNode(this);
        //visitor.visitIExpr(this);
        //visitor.visitISelector(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return TOTAL_NBR_FIELDS;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.name;
        case 1:  return this.hiddenTLparen;
        //case 2:  return this.primarySectionSubscriptList;
        //case 3:  return this.functionArgList;
        case 4:  return this.hiddenTRparen;
        case 5:  return this.hiddenTPercent;
        //case 6:  return this.substringRange;
        //case 7:  return this.imageSelector;
        case 8:  return this.hiddenHiddenPercent2;
        //case 9:  return this.derivedTypeComponentRef;
        case 10: return this.hiddenLparen2;
        //case 11: return this.componentSectionSubscriptList;
        case 12: return this.hiddenRparen2;
        //case 13: return this.substringRange2;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        //case 2:  this.primarySectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        //case 3:  this.functionArgList = (IASTListNode<ASTFunctionArgListNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTPercent = (ASTToken)value; if (value != null) value.setParent(this); return;
        //case 6:  this.substringRange = (ASTSubstringRangeNode)value; if (value != null) value.setParent(this); return;
        //case 7:  this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 8:  this.hiddenHiddenPercent2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        //case 9:  this.derivedTypeComponentRef = (IASTListNode<ASTDataRefNode>)value; if (value != null) value.setParent(this); return;
        case 10: this.hiddenLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        //case 11: this.componentSectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 12: this.hiddenRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        //case 13: this.substringRange2 = (ASTSubstringRangeNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

