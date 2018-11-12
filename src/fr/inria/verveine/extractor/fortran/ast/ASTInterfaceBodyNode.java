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

public class ASTInterfaceBodyNode extends ASTNode implements IInterfaceSpecification
{
    ASTFunctionStmtNode functionStmt; // in ASTInterfaceBodyNode
    ASTSubroutineStmtNode subroutineStmt; // in ASTInterfaceBodyNode
    IASTListNode<ISpecificationPartConstruct> subprogramInterfaceBody; // in ASTInterfaceBodyNode
    ASTEndFunctionStmtNode endFunctionStmt; // in ASTInterfaceBodyNode
    ASTEndSubroutineStmtNode endSubroutineStmt; // in ASTInterfaceBodyNode

    public ASTFunctionStmtNode getFunctionStmt()
    {
        return this.functionStmt;
    }

    public void setFunctionStmt(ASTFunctionStmtNode newValue)
    {
        this.functionStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTSubroutineStmtNode getSubroutineStmt()
    {
        return this.subroutineStmt;
    }

    public void setSubroutineStmt(ASTSubroutineStmtNode newValue)
    {
        this.subroutineStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ISpecificationPartConstruct> getSubprogramInterfaceBody()
    {
        return this.subprogramInterfaceBody;
    }

    public void setSubprogramInterfaceBody(IASTListNode<ISpecificationPartConstruct> newValue)
    {
        this.subprogramInterfaceBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndFunctionStmtNode getEndFunctionStmt()
    {
        return this.endFunctionStmt;
    }

    public void setEndFunctionStmt(ASTEndFunctionStmtNode newValue)
    {
        this.endFunctionStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndSubroutineStmtNode getEndSubroutineStmt()
    {
        return this.endSubroutineStmt;
    }

    public void setEndSubroutineStmt(ASTEndSubroutineStmtNode newValue)
    {
        this.endSubroutineStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTInterfaceBodyNode(this);
        visitor.visitIInterfaceSpecification(this);
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
        case 0:  return this.functionStmt;
        case 1:  return this.subroutineStmt;
        case 2:  return this.subprogramInterfaceBody;
        case 3:  return this.endFunctionStmt;
        case 4:  return this.endSubroutineStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.functionStmt = (ASTFunctionStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.subroutineStmt = (ASTSubroutineStmtNode)value; if (value != null) value.setParent(this); return;
        case 2:  this.subprogramInterfaceBody = (IASTListNode<ISpecificationPartConstruct>)value; if (value != null) value.setParent(this); return;
        case 3:  this.endFunctionStmt = (ASTEndFunctionStmtNode)value; if (value != null) value.setParent(this); return;
        case 4:  this.endSubroutineStmt = (ASTEndSubroutineStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

