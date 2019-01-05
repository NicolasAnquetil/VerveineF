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

public class ASTUseStmtNode extends ASTNode implements ISpecificationPartConstruct
{
	public static final int TLABEL = 0;
	public static final int TUSE = 1;
	public static final int TNAME = 6;
	public static final int TONLY = 9;
	public static final int TEOS = 12;

	ASTToken label;
    ASTToken useToken;
    ASTToken hiddenHiddenTComma1;
//    ASTModuleNatureNode moduleNature;
    ASTToken hiddenHiddenTColon1;
    ASTToken hiddenHiddenTColon2;
    ASTToken name;
    ASTToken hiddenTComma;
//    IASTListNode<ASTRenameNode> renameList;
    ASTToken hiddenTOnly;
    ASTToken hiddenTColon;
//    IASTListNode<ASTOnlyNode> onlyList;
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


    public ASTToken getUseToken()
    {
        return this.useToken;
    }

    public void setUseToken(ASTToken newValue)
    {
        this.useToken = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTModuleNatureNode getModuleNature()
    {
        return this.moduleNature;
    }

    public void setModuleNature(ASTModuleNatureNode newValue)
    {
        this.moduleNature = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public ASTToken getName()
    {
        return this.name;
    }

    public void setName(ASTToken newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTRenameNode> getRenameList()
    {
        return this.renameList;
    }

    public void setRenameList(IASTListNode<ASTRenameNode> newValue)
    {
        this.renameList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTOnlyNode> getOnlyList()
    {
        return this.onlyList;
    }

    public void setOnlyList(IASTListNode<ASTOnlyNode> newValue)
    {
        this.onlyList = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTUseStmtNode(this);
//        visitor.visitISpecificationPartConstruct(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 13;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case TLABEL:  return this.label;
        case TUSE:  return this.useToken;
        case 2:  return this.hiddenHiddenTComma1;
        case 3:  return new ASTNullNode();  // this.moduleNature;
        case 4:  return this.hiddenHiddenTColon1;
        case 5:  return this.hiddenHiddenTColon2;
        case TNAME:  return this.name;
        case 7:  return this.hiddenTComma;
        case 8:  return new ASTNullNode();  // this.renameList;
        case TONLY:  return this.hiddenTOnly;
        case 10: return this.hiddenTColon;
        case 11: return new ASTNullNode();  // this.onlyList;
        case TEOS: return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case TLABEL:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TUSE:  this.useToken = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenHiddenTComma1 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return;  // this.moduleNature = (ASTModuleNatureNode)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenHiddenTColon1 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenHiddenTColon2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TNAME:  this.name = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTComma = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  return;  // this.renameList = (IASTListNode<ASTRenameNode>)value; if (value != null) value.setParent(this); return;
        case TONLY:  this.hiddenTOnly = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 10: this.hiddenTColon = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 11: return;  // this.onlyList = (IASTListNode<ASTOnlyNode>)value; if (value != null) value.setParent(this); return;
        case TEOS: this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

