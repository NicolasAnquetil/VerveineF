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
public class ASTSubscriptTripletNode extends ASTNode
{
    IExpr lb; // in ASTSubscriptTripletNode
    ASTToken hiddenTColon; // in ASTSubscriptTripletNode
    IExpr ub; // in ASTSubscriptTripletNode
    ASTToken hiddenTColon2; // in ASTSubscriptTripletNode
    IExpr step; // in ASTSubscriptTripletNode

    public IExpr getLb()
    {
        return this.lb;
    }

    public void setLb(IExpr newValue)
    {
        this.lb = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IExpr getUb()
    {
        return this.ub;
    }

    public void setUb(IExpr newValue)
    {
        this.ub = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IExpr getStep()
    {
        return this.step;
    }

    public void setStep(IExpr newValue)
    {
        this.step = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTSubscriptTripletNode(this);
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
        case 0:  return this.lb;
        case 1:  return this.hiddenTColon;
        case 2:  return this.ub;
        case 3:  return this.hiddenTColon2;
        case 4:  return this.step;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.lb = (IExpr)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.ub = (IExpr)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.step = (IExpr)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

