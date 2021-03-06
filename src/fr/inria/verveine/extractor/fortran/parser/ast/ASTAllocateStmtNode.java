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
public class ASTAllocateStmtNode extends ASTNode /*ASTNodeWithErrorRecoverySymbols*/ implements IActionStmt
{
    public static final int TALLOC = 1;
	public static final int TEOS = 8;

	ASTToken label;
    ASTToken hiddenTAllocate;
    ASTToken hiddenTLparen;
    IASTListNode<ASTAllocationNode> allocationList;
    ASTToken hiddenTComma;
    ASTToken hiddenTStateq;
    ASTDataRefNode statusVariable;   // was: ASTVariableNode statusVariable;
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

    public IASTListNode<ASTAllocationNode> getAllocationList()
    {
        return this.allocationList;
    }

    public void setAllocationList(IASTListNode<ASTAllocationNode> newValue)
    {
        this.allocationList = newValue;
        if (newValue != null) newValue.setParent(this);
    }

    public ASTDataRefNode getStatusVariable()
    {
        return this.statusVariable;
    }

    public void setStatusVariable(ASTDataRefNode astDataRefNode)
    {
        this.statusVariable = astDataRefNode;
        if (astDataRefNode != null) astDataRefNode.setParent(this);
    }

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocateStmtNode(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 9;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case TALLOC:  return this.hiddenTAllocate;
        case 2:  return this.hiddenTLparen;
        case 3:  return this.allocationList;
        case 4:  return this.hiddenTComma;
        case 5:  return this.hiddenTStateq;
        case 6:  return this.statusVariable;
        case 7:  return this.hiddenTRparen;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TALLOC:  this.hiddenTAllocate = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.allocationList = (IASTListNode<ASTAllocationNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTStateq = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.statusVariable = (ASTDataRefNode)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

