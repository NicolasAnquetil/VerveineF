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
public class ASTDerivedTypeStmtNode extends ASTNode
{
    ASTToken label;
    ASTToken hiddenTType;
    ASTToken hiddenTComma;
    //IASTListNode<ASTTypeAttrSpecNode> typeAttrSpecList;
    ASTToken hiddenTColon;
    ASTToken hiddenTColon2;
    ASTToken typeName;
    ASTToken hiddenTLparen;
    //IASTListNode<ASTTypeParamNameNode> typeParamNameList;
    ASTToken hiddenTRparen;
    ASTToken hiddenTEos;

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTTypeAttrSpecNode> getTypeAttrSpecList()
    {
        return this.typeAttrSpecList;
    }

    public void setTypeAttrSpecList(IASTListNode<ASTTypeAttrSpecNode> newValue)
    {
        this.typeAttrSpecList = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public ASTToken getTypeName()
    {
        return this.typeName;
    }

    public void setTypeName(ASTToken newValue)
    {
        this.typeName = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTTypeParamNameNode> getTypeParamNameList()
    {
        return this.typeParamNameList;
    }

    public void setTypeParamNameList(IASTListNode<ASTTypeParamNameNode> newValue)
    {
        this.typeParamNameList = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTDerivedTypeStmtNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 11;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.hiddenTType;
        case 2:  return this.hiddenTComma;
        case 3:  return new ASTNullNode();  // this.typeAttrSpecList;
        case 4:  return this.hiddenTColon;
        case 5:  return this.hiddenTColon2;
        case 6:  return this.typeName;
        case 7:  return this.hiddenTLparen;
        case 8:  return new ASTNullNode();  // this.typeParamNameList;
        case 9:  return this.hiddenTRparen;
        case 10: return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTType = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.typeAttrSpecList = (IASTListNode<ASTTypeAttrSpecNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.typeName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  return; //this.typeParamNameList = (IASTListNode<ASTTypeParamNameNode>)value; if (value != null) value.setParent(this); return;
        case 9:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 10: this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTDerivedTypeStmtNode " + typeName;
    }

}

