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
public class ASTAssociationNode extends ASTNode
{
    ASTToken associateName; // in ASTAssociationNode
    ASTToken hiddenTEqgreaterthan; // in ASTAssociationNode
    ISelector selector; // in ASTAssociationNode

    public ASTToken getAssociateName()
    {
        return this.associateName;
    }

    public void setAssociateName(ASTToken newValue)
    {
        this.associateName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ISelector getSelector()
    {
        return this.selector;
    }

    public void setSelector(ISelector newValue)
    {
        this.selector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAssociationNode(this);
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
        case 0:  return this.associateName;
        case 1:  return this.hiddenTEqgreaterthan;
        case 2:  return this.selector;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.associateName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTEqgreaterthan = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.selector = (ISelector)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

