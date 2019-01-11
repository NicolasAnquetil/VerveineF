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
public class ASTTypeSpecNode extends ASTNode
{
	public static final int T_TYPENAME = 13;

    /* using 'typeName' token instead
	ASTToken isCharacter;
    ASTToken isDouble;
    ASTToken isReal;
    ASTToken isDblComplex;
    ASTToken hiddenTComplex;
    ASTToken isComplex;
    ASTToken isInteger;
    ASTToken isDerivedType;
	ASTToken isByte;
	ASTToken isLogical;
    */

	ASTToken hiddenTPrecision;
    //ASTKindSelectorNode kindSelector;
    ASTToken hiddenTLparen;
    ASTToken isAsterisk;
    ASTToken typeName;
    ASTToken hiddenHiddenLParen2;
    //IASTListNode<ASTTypeParamSpecNode> typeParamSpecList;
    ASTToken hiddenHiddenRParen2;
    ASTToken hiddenTRparen;
    //ASTCharSelectorNode charSelector;

/*
    public boolean isCharacter()
    {
        return this.isCharacter != null;
    }

    public void setIsCharacter(ASTToken newValue)
    {
        this.isCharacter = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isDouble()
    {
        return this.isDouble != null;
    }

    public void setIsDouble(ASTToken newValue)
    {
        this.isDouble = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isReal()
    {
        return this.isReal != null;
    }

    public void setIsReal(ASTToken newValue)
    {
        this.isReal = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isDblComplex()
    {
        return this.isDblComplex != null;
    }

    public void setIsDblComplex(ASTToken newValue)
    {
        this.isDblComplex = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isComplex()
    {
        return this.isComplex != null;
    }

    public void setIsComplex(ASTToken newValue)
    {
        this.isComplex = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isInteger()
    {
        return this.isInteger != null;
    }

    public void setIsInteger(ASTToken newValue)
    {
        this.isInteger = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isDerivedType()
    {
        return this.isDerivedType != null;
    }

    public void setIsDerivedType(ASTToken newValue)
    {
        this.isDerivedType = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public boolean isLogical()
    {
        return this.isLogical != null;
    }

    public void setIsLogical(ASTToken newValue)
    {
        this.isLogical = newValue;
        if (newValue != null) newValue.setParent(this);
    }
    
    public boolean isByte()
    {
    	return this.isByte != null;
    }
    
    public void setIsByte(ASTToken newValue)
    {
    	this.isByte = newValue;
    	if (newValue != null) newValue.setParent(this);
    }

    public ASTKindSelectorNode getKindSelector()
    {
        return this.kindSelector;
    }

    public void setKindSelector(ASTKindSelectorNode newValue)
    {
        this.kindSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }
 */


    public boolean isAsterisk()
    {
        return this.isAsterisk != null;
    }

    public void setIsAsterisk(ASTToken newValue)
    {
        this.isAsterisk = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getTypeName()
    {
        return this.typeName;
    }

    public void setTypeName(ASTToken newValue)
    {
        this.typeName = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTTypeParamSpecNode> getTypeParamSpecList()
    {
        return this.typeParamSpecList;
    }

    public void setTypeParamSpecList(IASTListNode<ASTTypeParamSpecNode> newValue)
    {
        this.typeParamSpecList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTCharSelectorNode getCharSelector()
    {
        return this.charSelector;
    }

    public void setCharSelector(ASTCharSelectorNode newValue)
    {
        this.charSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }

*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTTypeSpecNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 20;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return new ASTNullNode(); // this.isCharacter;
        case 1:  return new ASTNullNode(); // thisthis.isDouble;
        case 2:  return new ASTNullNode(); // thisthis.isReal;
        case 3:  return new ASTNullNode(); // thisthis.isDblComplex;
        case 4:  return new ASTNullNode(); // thisthis.hiddenTComplex;
        case 5:  return new ASTNullNode(); // thisthis.isComplex;
        case 6:  return new ASTNullNode(); // thisthis.isInteger;
        case 7:  return new ASTNullNode(); // thisthis.isDerivedType;
        case 8:  return this.hiddenTPrecision;
        case 9:  return new ASTNullNode(); // thisthis.isLogical;
        case 10: return new ASTNullNode(); // thisthis.kindSelector;
        case 11: return this.hiddenTLparen;
        case 12: return this.isAsterisk;
        case T_TYPENAME: return this.typeName;
        case 14: return this.hiddenHiddenLParen2;
        case 15: return new ASTNullNode(); // thisthis.typeParamSpecList;
        case 16: return this.hiddenHiddenRParen2;
        case 17: return this.hiddenTRparen;
        case 18: return new ASTNullNode(); // thisthis.charSelector;
        case 19: return new ASTNullNode(); // thisthis.isByte;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  return; // this.isCharacter = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  return; // this.isDouble = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  return; // this.isReal = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return; // this.isDblComplex = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 4:  return; // this.hiddenTComplex = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return; // this.isComplex = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  return; // this.isInteger = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 7:  return; // this.isDerivedType = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  this.hiddenTPrecision = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 9:  return; // this.isLogical = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 10: return; // this.kindSelector = (ASTKindSelectorNode)value; if (value != null) value.setParent(this); return;
        case 11: this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 12: this.isAsterisk = (ASTToken)value; if (value != null) value.setParent(this); return;
        case T_TYPENAME: this.typeName = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 14: this.hiddenHiddenLParen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 15: return; // this.typeParamSpecList = (IASTListNode<ASTTypeParamSpecNode>)value; if (value != null) value.setParent(this); return;
        case 16: this.hiddenHiddenRParen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 17: this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 18: return; // this.charSelector = (ASTCharSelectorNode)value; if (value != null) value.setParent(this); return;
        case 19: return; // this.isByte = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
    
    @Override
    public String toString() {
    	return "ASTTypeSpecNode " + typeName.getText();
    }

}

