package fr.inria.verveine.extractor.fortran.ast;

public class ASTIfStmtNode extends ASTNode implements IActionStmt //extends ASTNodeWithErrorRecoverySymbols
{

	ASTToken label; // in ASTIfStmtNode
    ASTToken tIf; // in ASTIfStmtNode
    ASTToken hiddenTLparen; // in ASTIfStmtNode
    //IExpr guardingExpression; // in ASTIfStmtNode
    ASTToken hiddenTRparen; // in ASTIfStmtNode
    //IActionStmt actionStmt; // in ASTIfStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTToken getTIf()
    {
        return this.tIf;
    }

    public void setTIf(ASTToken newValue)
    {
        this.tIf = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public IExpr getGuardingExpression()
    {
        return this.guardingExpression;
    }

    public void setGuardingExpression(IExpr newValue)
    {
        this.guardingExpression = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IActionStmt getActionStmt()
    {
        return this.actionStmt;
    }

    public void setActionStmt(IActionStmt newValue)
    {
        this.actionStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTIfStmtNode(this);
        visitor.visitIActionStmt(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 6;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case 1:  return this.tIf;
        case 2:  return this.hiddenTLparen;
        case 3:  return new ASTNullNode(); //this.guardingExpression;
        case 4:  return this.hiddenTRparen;
        case 5:  return new ASTNullNode(); //this.actionStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 1:  this.tIf = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.guardingExpression = (IExpr)value; if (value != null) value.setParent(this); return;
        case 4:  this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 5:  return; //this.actionStmt = (IActionStmt)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}

