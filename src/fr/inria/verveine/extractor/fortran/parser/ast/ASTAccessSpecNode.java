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
public class ASTAccessSpecNode extends ASTNode
{
    ASTToken isPrivate;
    ASTToken isPublic;

    public boolean isPrivate()
    {
        return this.isPrivate != null;
    }

    public void setIsPrivate(ASTToken newValue)
    {
        this.isPrivate = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isPublic()
    {
        return this.isPublic != null;
    }

    public void setIsPublic(ASTToken newValue)
    {
        this.isPublic = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAccessSpecNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 2;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.isPrivate;
        case 1:  return this.isPublic;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.isPrivate = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.isPublic = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTAccessSpecNode " + (isPrivate==null?isPublic:isPrivate);
    }

}

