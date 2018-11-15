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

import java.util.Set;

@SuppressWarnings("all")
public class ASTAllocateStmtNode extends ASTNodeWithErrorRecoverySymbols implements IActionStmt
{
    ASTToken label; // in ASTAllocateStmtNode
    ASTToken hiddenTAllocate; // in ASTAllocateStmtNode
    ASTToken hiddenTLparen; // in ASTAllocateStmtNode
    IASTListNode<ASTAllocationNode> allocationList; // in ASTAllocateStmtNode
    ASTToken hiddenTComma; // in ASTAllocateStmtNode
    ASTToken hiddenTStateq; // in ASTAllocateStmtNode
    ASTVariableNode statusVariable; // in ASTAllocateStmtNode
    ASTToken hiddenTRparen; // in ASTAllocateStmtNode
    ASTToken hiddenTEos; // in ASTAllocateStmtNode

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


    public ASTVariableNode getStatusVariable()
    {
        return this.statusVariable;
    }

    public void setStatusVariable(ASTVariableNode newValue)
    {
        this.statusVariable = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocateStmtNode(this);
        visitor.visitIActionStmt(this);
        visitor.visitASTNode(this);
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
        case 1:  return this.hiddenTAllocate;
        case 2:  return this.hiddenTLparen;
        case 3:  return this.allocationList;
        case 4:  return this.hiddenTComma;
        case 5:  return this.hiddenTStateq;
        case 6:  return this.statusVariable;
        case 7:  return this.hiddenTRparen;
        case 8:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTAllocate = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.allocationList = (IASTListNode<ASTAllocationNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTStateq = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  this.statusVariable = (ASTVariableNode)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public IASTNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(IASTNode parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<? extends IASTNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fullyQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IASTNode> Set<T> findAll(Class<T> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IASTNode> T findNearestAncestor(Class<T> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IASTNode> T findFirst(Class<T> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IASTNode> T findLast(Class<T> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ASTToken findFirstToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ASTToken findLastToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFirstChildInList() {
		// TODO Auto-generated method stub
		return false;
	}
}

