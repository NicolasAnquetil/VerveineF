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
public class ASTIntentSpecNode extends ASTNode
{
    ASTToken isIntentOut;
    ASTToken isIntentInOut;
    ASTToken isIntentIn;
    ASTToken hiddenTOut;

    public boolean isIntentOut()
    {
        return this.isIntentOut != null;
    }

    public void setIsIntentOut(ASTToken newValue)
    {
        this.isIntentOut = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isIntentInOut()
    {
        return this.isIntentInOut != null;
    }

    public void setIsIntentInOut(ASTToken newValue)
    {
        this.isIntentInOut = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isIntentIn()
    {
        return this.isIntentIn != null;
    }

    public void setIsIntentIn(ASTToken newValue)
    {
        this.isIntentIn = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTIntentSpecNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 4;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.isIntentOut;
        case 1:  return this.isIntentInOut;
        case 2:  return this.isIntentIn;
        case 3:  return this.hiddenTOut;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.isIntentOut = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.isIntentInOut = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.isIntentIn = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTOut = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

