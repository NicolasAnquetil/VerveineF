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
public class ASTDerivedTypeDefNode extends ASTNode //ScopingNode implements IDeclarationConstruct
{
    ASTDerivedTypeStmtNode derivedTypeStmt;
    //ASTTypeParamDefStmtNode typeParamDefStmt;
    //IASTListNode<IDerivedTypeBodyConstruct> derivedTypeBody;
    //ASTTypeBoundProcedurePartNode typeBoundProcedurePart;
    //ASTEndTypeStmtNode endTypeStmt;

    public ASTDerivedTypeStmtNode getDerivedTypeStmt()
    {
        return this.derivedTypeStmt;
    }

    public void setDerivedTypeStmt(ASTDerivedTypeStmtNode newValue)
    {
        this.derivedTypeStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTTypeParamDefStmtNode getTypeParamDefStmt()
    {
        return this.typeParamDefStmt;
    }

    public void setTypeParamDefStmt(ASTTypeParamDefStmtNode newValue)
    {
        this.typeParamDefStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<IDerivedTypeBodyConstruct> getDerivedTypeBody()
    {
        return this.derivedTypeBody;
    }

    public void setDerivedTypeBody(IASTListNode<IDerivedTypeBodyConstruct> newValue)
    {
        this.derivedTypeBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTTypeBoundProcedurePartNode getTypeBoundProcedurePart()
    {
        return this.typeBoundProcedurePart;
    }

    public void setTypeBoundProcedurePart(ASTTypeBoundProcedurePartNode newValue)
    {
        this.typeBoundProcedurePart = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndTypeStmtNode getEndTypeStmt()
    {
        return this.endTypeStmt;
    }

    public void setEndTypeStmt(ASTEndTypeStmtNode newValue)
    {
        this.endTypeStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        //visitor.visitASTDerivedTypeDefNode(this);
        //visitor.visitIDeclarationConstruct(this);
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
        case 0:  return this.derivedTypeStmt;
        case 1:  return new ASTNullNode(); //this.typeParamDefStmt;
        case 2:  return new ASTNullNode(); //this.derivedTypeBody;
        case 3:  return new ASTNullNode(); //this.typeBoundProcedurePart;
        case 4:  return new ASTNullNode(); //this.endTypeStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.derivedTypeStmt = (ASTDerivedTypeStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  return; //this.typeParamDefStmt = (ASTTypeParamDefStmtNode)value; if (value != null) value.setParent(this); return;
        case 2:  return; //this.derivedTypeBody = (IASTListNode<IDerivedTypeBodyConstruct>)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.typeBoundProcedurePart = (ASTTypeBoundProcedurePartNode)value; if (value != null) value.setParent(this); return;
        case 4:  return; //this.endTypeStmt = (ASTEndTypeStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTDerivedTypeDefNode " + ((derivedTypeStmt != null) ? derivedTypeStmt.basename() : "without DerivedTypeStmt");
    }

}

