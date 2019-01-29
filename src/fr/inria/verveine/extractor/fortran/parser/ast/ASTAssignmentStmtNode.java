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
public class ASTAssignmentStmtNode extends ASTNode/*WithErrorRecoverySymbols*/ implements IActionStmt, IBodyConstruct, ICaseBodyConstruct, IExecutableConstruct, IExecutionPartConstruct //, IForallBodyConstruct, IWhereBodyConstruct
{
    ASTToken label;
    ASTVariableNode lhsVariable;  // was ASTNameNode
    ASTToken hiddenTLparen;
    //IASTListNode<ASTSFDummyArgNameListNode> lhsNameList;
    //IASTListNode<ASTSFExprListNode> lhsExprList;
    ASTToken hiddenTRparen;
    //ASTImageSelectorNode imageSelector;
    ASTToken hiddenTPercent;
    //IASTListNode<ASTDataRefNode> derivedTypeComponentRef;
    ASTToken hiddenLparen2;
    //IASTListNode<ASTSectionSubscriptNode> componentSectionSubscriptList;
    ASTToken hiddenRparen2;
    //ASTSubstringRangeNode substringRange;
    ASTToken isPointerAssignment;
    //ASTTargetNode target;
    ASTToken hiddenTEquals;
    IExpr rhs;
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


    public ASTVariableNode getLhsVariable()
    {
        return this.lhsVariable;
    }

    public void setLhsVariable(ASTVariableNode astVariableNode)
    {
        this.lhsVariable = astVariableNode;
        if (astVariableNode != null) astVariableNode.setParent(this);
    }

/*
    public IASTListNode<ASTSFDummyArgNameListNode> getLhsNameList()
    {
        return this.lhsNameList;
    }

    public void setLhsNameList(IASTListNode<ASTSFDummyArgNameListNode> newValue)
    {
        this.lhsNameList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTSFExprListNode> getLhsExprList()
    {
        return this.lhsExprList;
    }

    public void setLhsExprList(IASTListNode<ASTSFExprListNode> newValue)
    {
        this.lhsExprList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTImageSelectorNode getImageSelector()
    {
        return this.imageSelector;
    }

    public void setImageSelector(ASTImageSelectorNode newValue)
    {
        this.imageSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTDataRefNode> getDerivedTypeComponentRef()
    {
        return this.derivedTypeComponentRef;
    }

    public void setDerivedTypeComponentRef(IASTListNode<ASTDataRefNode> newValue)
    {
        this.derivedTypeComponentRef = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ASTSectionSubscriptNode> getComponentSectionSubscriptList()
    {
        return this.componentSectionSubscriptList;
    }

    public void setComponentSectionSubscriptList(IASTListNode<ASTSectionSubscriptNode> newValue)
    {
        this.componentSectionSubscriptList = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTSubstringRangeNode getSubstringRange()
    {
        return this.substringRange;
    }

    public void setSubstringRange(ASTSubstringRangeNode newValue)
    {
        this.substringRange = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public boolean isPointerAssignment()
    {
        return this.isPointerAssignment != null;
    }

    public void setIsPointerAssignment(ASTToken newValue)
    {
        this.isPointerAssignment = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTTargetNode getTarget()
    {
        return this.target;
    }

    public void setTarget(ASTTargetNode newValue)
    {
        this.target = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public IExpr getRhs()
    {
        return this.rhs;
    }

    public void setRhs(IExpr newValue)
    {
        this.rhs = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTAssignmentStmtNode(this);
/*        visitor.visitIActionStmt(this);
        visitor.visitIBodyConstruct(this);
        visitor.visitICaseBodyConstruct(this);
        visitor.visitIExecutableConstruct(this);
        visitor.visitIExecutionPartConstruct(this);
        visitor.visitIForallBodyConstruct(this);
        visitor.visitIWhereBodyConstruct(this);
        visitor.visitASTNode(this);
 */
    }

    @Override protected int getNumASTFields()
    {
        return 18;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.lhsVariable;
        case 2:  return this.hiddenTLparen;
        case 3:  return new ASTNullNode();  // this.lhsNameList;
        case 4:  return new ASTNullNode();  // this.lhsExprList;
        case 5:  return this.hiddenTRparen;
        case 6:  return new ASTNullNode();  // this.imageSelector;
        case 7:  return this.hiddenTPercent;
        case 8:  return new ASTNullNode();  // this.derivedTypeComponentRef;
        case 9:  return this.hiddenLparen2;
        case 10: return new ASTNullNode();  // this.componentSectionSubscriptList;
        case 11: return this.hiddenRparen2;
        case 12: return new ASTNullNode();  // this.substringRange;
        case 13: return this.isPointerAssignment;
        case 14: return new ASTNullNode();  // this.target;
        case 15: return this.hiddenTEquals;
        case 16: return this.rhs;
        case 17: return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.lhsVariable = (ASTVariableNode)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return; // this.lhsNameList = (IASTListNode<ASTSFDummyArgNameListNode>)value; if (value != null) value.setParent(this); return;
        case 4:  return; // this.lhsExprList = (IASTListNode<ASTSFExprListNode>)value; if (value != null) value.setParent(this); return;
        case 5:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  return; // this.imageSelector = (ASTImageSelectorNode)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTPercent = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 8:  return; // this.derivedTypeComponentRef = (IASTListNode<ASTDataRefNode>)value; if (value != null) value.setParent(this); return;
        case 9:  this.hiddenLparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 10: return; // this.componentSectionSubscriptList = (IASTListNode<ASTSectionSubscriptNode>)value; if (value != null) value.setParent(this); return;
        case 11: this.hiddenRparen2 = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 12: return; // this.substringRange = (ASTSubstringRangeNode)value; if (value != null) value.setParent(this); return;
        case 13: this.isPointerAssignment = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 14: return; // this.target = (ASTTargetNode)value; if (value != null) value.setParent(this); return;
        case 15: this.hiddenTEquals = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 16: this.rhs = (IExpr)value; if (value != null) value.setParent(this); return;
        case 17: this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
    	// for debug purposes
    	return "ASTAssignmentStmtNode " + lhsVariable.getDataRef().getName();
    }

}

