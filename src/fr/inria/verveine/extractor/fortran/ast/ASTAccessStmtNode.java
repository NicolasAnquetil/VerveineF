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
public class ASTAccessStmtNode extends ASTNode //implements ISpecificationStmt
{
	public static final int T_LABEL = 0;
    public static final int T_EOS = 5;

	ASTToken label;
    ASTAccessSpecNode accessSpec;
    ASTToken hiddenTColon;
    ASTToken hiddenTColon2;
    //IASTListNode<IAccessId> accessIdList;
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


    public ASTAccessSpecNode getAccessSpec()
    {
        return this.accessSpec;
    }

    public void setAccessSpec(ASTAccessSpecNode newValue)
    {
        this.accessSpec = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<IAccessId> getAccessIdList()
    {
        return this.accessIdList;
    }

    public void setAccessIdList(IASTListNode<IAccessId> newValue)
    {
        this.accessIdList = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAccessStmtNode(this);
//        visitor.visitISpecificationStmt(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 6;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case T_LABEL:  return this.label;
        case 1:  return this.accessSpec;
        case 2:  return this.hiddenTColon;
        case 3:  return this.hiddenTColon2;
        case 4:  return new ASTNullNode();  // this.accessIdList;
        case T_EOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case T_LABEL:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.accessSpec = (ASTAccessSpecNode)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  return;  // this.accessIdList = (IASTListNode<IAccessId>)value; if (value != null) value.setParent(this); return;
        case T_EOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTAccessStmtNode " + hiddenTEos;
    }

}

