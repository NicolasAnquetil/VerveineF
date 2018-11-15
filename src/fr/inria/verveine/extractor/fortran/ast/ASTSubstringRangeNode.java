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
public class ASTSubstringRangeNode extends ASTNode
{
    ASTToken hiddenTLparen; // in ASTSubstringRangeNode
    ASTSubscriptTripletNode subscriptTriplet; // in ASTSubstringRangeNode
    ASTToken hiddenTRparen; // in ASTSubstringRangeNode

    public ASTSubscriptTripletNode getSubscriptTriplet()
    {
        return this.subscriptTriplet;
    }

    public void setSubscriptTriplet(ASTSubscriptTripletNode newValue)
    {
        this.subscriptTriplet = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTSubstringRangeNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 3;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.hiddenTLparen;
        case 1:  return this.subscriptTriplet;
        case 2:  return this.hiddenTRparen;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.subscriptTriplet = (ASTSubscriptTripletNode)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

