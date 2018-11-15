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
public class ASTAllocateObjectNode extends ASTNode
{
    ASTVariableNameNode variableName; // in ASTAllocateObjectNode
    ASTFieldSelectorNode fieldSelector; // in ASTAllocateObjectNode

    public ASTVariableNameNode getVariableName()
    {
        return this.variableName;
    }

    public void setVariableName(ASTVariableNameNode newValue)
    {
        this.variableName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTFieldSelectorNode getFieldSelector()
    {
        return this.fieldSelector;
    }

    public void setFieldSelector(ASTFieldSelectorNode newValue)
    {
        this.fieldSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAllocateObjectNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 2;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.variableName;
        case 1:  return this.fieldSelector;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.variableName = (ASTVariableNameNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.fieldSelector = (ASTFieldSelectorNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

