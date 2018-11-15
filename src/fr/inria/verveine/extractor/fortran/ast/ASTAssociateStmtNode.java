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
public class ASTAssociateStmtNode extends ASTNode
{
    ASTToken label; // in ASTAssociateStmtNode
    ASTToken name; // in ASTAssociateStmtNode
    ASTToken hiddenTColon; // in ASTAssociateStmtNode
    ASTToken hiddenTAssociate; // in ASTAssociateStmtNode
    ASTToken hiddenTLparen; // in ASTAssociateStmtNode
    IASTListNode<ASTAssociationNode> associationList; // in ASTAssociateStmtNode
    ASTToken hiddenTRparen; // in ASTAssociateStmtNode
    ASTToken hiddenTEos; // in ASTAssociateStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
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


    public IASTListNode<ASTAssociationNode> getAssociationList()
    {
        return this.associationList;
    }

    public void setAssociationList(IASTListNode<ASTAssociationNode> newValue)
    {
        this.associationList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAssociateStmtNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 8;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.name;
        case 2:  return this.hiddenTColon;
        case 3:  return this.hiddenTAssociate;
        case 4:  return this.hiddenTLparen;
        case 5:  return this.associationList;
        case 6:  return this.hiddenTRparen;
        case 7:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  this.hiddenTAssociate = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.associationList = (IASTListNode<ASTAssociationNode>)value; if (value != null) value.setParent(this); return;
        case 6:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

