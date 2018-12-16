package fr.inria.verveine.extractor.fortran.ast;

public class ASTCaseStmtNode extends ASTNode {

	ASTToken label; // in ASTCaseStmtNode
    ASTToken hiddenTCase; // in ASTCaseStmtNode
    ASTToken hiddenTLparen; // in ASTCaseStmtNode
    //IASTListNode<ASTCaseValueRangeNode> caseValueRangeListSelector; // in ASTCaseStmtNode
    ASTToken hiddenTRparen; // in ASTCaseStmtNode
    ASTToken hasDefaultSelector; // in ASTCaseStmtNode
    //ASTNameNode name; // in ASTCaseStmtNode
    ASTToken hiddenTEos; // in ASTCaseStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IASTListNode<ASTCaseValueRangeNode> getCaseValueRangeListSelector()
    {
        return this.caseValueRangeListSelector;
    }

    public void setCaseValueRangeListSelector(IASTListNode<ASTCaseValueRangeNode> newValue)
    {
        this.caseValueRangeListSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public boolean hasDefaultSelector()
    {
        return this.hasDefaultSelector != null;
    }

    public void setHasDefaultSelector(ASTToken newValue)
    {
        this.hasDefaultSelector = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTNameNode getName()
    {
        return this.name;
    }

    public void setName(ASTNameNode newValue)
    {
        this.name = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTCaseStmtNode(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitICaseBodyConstruct(this);
//        visitor.visitIActionStmt(this);
//        visitor.visitASTNode(this);
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
        case 1:  return this.hiddenTCase;
        case 2:  return this.hiddenTLparen;
        case 3:  return new ASTNullNode(); // this.caseValueRangeListSelector;
        case 4:  return this.hiddenTRparen;
        case 5:  return this.hasDefaultSelector;
        case 6:  return new ASTNullNode(); // this.name;
        case 7:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.hiddenTCase = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return; // this.caseValueRangeListSelector = (IASTListNode<ASTCaseValueRangeNode>)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  this.hasDefaultSelector = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 6:  return; // this.name = (ASTNameNode)value; if (value != null) value.setParent(this); return;
        case 7:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }


}
