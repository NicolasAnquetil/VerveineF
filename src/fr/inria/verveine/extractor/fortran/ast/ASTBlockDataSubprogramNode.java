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

public class ASTBlockDataSubprogramNode extends ASTNode implements IProgramUnit  // extends ScopingNode
{
    //ASTBlockDataStmtNode blockDataStmt; // in ASTBlockDataSubprogramNode
    IASTListNode<IBlockDataBodyConstruct> blockDataBody; // in ASTBlockDataSubprogramNode
    //ASTEndBlockDataStmtNode endBlockDataStmt; // in ASTBlockDataSubprogramNode
/*
    public ASTBlockDataStmtNode getBlockDataStmt()
    {
        return this.blockDataStmt;
    }

    public void setBlockDataStmt(ASTBlockDataStmtNode newValue)
    {
        this.blockDataStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public IASTListNode<IBlockDataBodyConstruct> getBlockDataBody()
    {
        return this.blockDataBody;
    }

    public void setBlockDataBody(IASTListNode<IBlockDataBodyConstruct> newValue)
    {
        this.blockDataBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTEndBlockDataStmtNode getEndBlockDataStmt()
    {
        return this.endBlockDataStmt;
    }

    public void setEndBlockDataStmt(ASTEndBlockDataStmtNode newValue)
    {
        this.endBlockDataStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
//        visitor.visitASTBlockDataSubprogramNode(this);
        visitor.visitIProgramUnit(this);
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
//        case 0:  return this.blockDataStmt;
        case 1:  return this.blockDataBody;
//        case 2:  return this.endBlockDataStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
//        case 0:  this.blockDataStmt = (ASTBlockDataStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.blockDataBody = (IASTListNode<IBlockDataBodyConstruct>)value; if (value != null) value.setParent(this); return;
//        case 2:  this.endBlockDataStmt = (ASTEndBlockDataStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public boolean isTopLevelNode() {
		return true;
	}
    
}

