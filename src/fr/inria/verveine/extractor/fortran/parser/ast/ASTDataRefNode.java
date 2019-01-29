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
package fr.inria.verveine.extractor.fortran.parser.ast;

import java.io.PrintStream;
import java.util.Iterator;

import java.util.List;

@SuppressWarnings("all")
public class ASTDataRefNode extends ASTNode
{
    ASTToken hiddenTLparen;
    ASTToken name;
    //IASTListNode<ASTSectionSubscriptNode> primarySectionSubscriptList;
    ASTToken hiddenTRparen;
    //ASTImageSelectorNode imageSelector;
    ASTToken hasDerivedTypeComponentName;
    ASTDataRefNode componentName;  // was a simple ASTNameNode (i.e. a simple name) but would not allow for a%b%c

	public String fortranNameToString() {
		return name.getText() + (componentName == null ? "" : "%" + componentName.fortranNameToString());
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


    public ASTImageSelectorNode getImageSelector()
    {
        return this.imageSelector;
    }

    public void setImageSelector(ASTImageSelectorNode newValue)
    {
        this.imageSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public boolean hasDerivedTypeComponentName()
    {
        return this.hasDerivedTypeComponentName != null;
    }

    public void setHasDerivedTypeComponentName(ASTToken newValue)
    {
        this.hasDerivedTypeComponentName = newValue;
        if (newValue != null) newValue.setParent(this);
    }

    public ASTDataRefNode getComponentName()
    {
        return this.componentName;
    }

    public void setComponentName(ASTDataRefNode newValue)
    {
        this.componentName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTDataRefNode(this);
//        visitor.visitASTNode(this);
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
        case 2:  return new ASTNullNode(); // this.primarySectionSubscriptList;
        case 3:  return this.hiddenTRparen;
        case 4:  return new ASTNullNode(); // this.imageSelector;
        case 5:  return this.hasDerivedTypeComponentName;
        case 6:  return new ASTNullNode(); // this.componentName;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  return; // this.primarySectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  return; // this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 5:  this.hasDerivedTypeComponentName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.componentName = (ASTDataRefNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {    	
    	return "ASTDataRefNode " + fortranNameToString();
    }

}

