package fr.inria.verveine.extractor.fortran.ast;

public class ASTElseIfConstructNode extends ASTNode {

	//ASTElseIfStmtNode elseIfStmt; // in ASTElseIfConstructNode
    //IASTListNode<IExecutionPartConstruct> conditionalBody; // in ASTElseIfConstructNode
    //ASTEndIfStmtNode endIfStmt; // in ASTElseIfConstructNode
    ASTElseIfConstructNode elseIfConstruct; // in ASTElseIfConstructNode
    //ASTElseConstructNode elseConstruct; // in ASTElseIfConstructNode

/*
    public ASTElseIfStmtNode getElseIfStmt()
    {
        return this.elseIfStmt;
    }

    public void setElseIfStmt(ASTElseIfStmtNode newValue)
    {
        this.elseIfStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<IExecutionPartConstruct> getConditionalBody()
    {
        return this.conditionalBody;
    }

    public void setConditionalBody(IASTListNode<IExecutionPartConstruct> newValue)
    {
        this.conditionalBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndIfStmtNode getEndIfStmt()
    {
        return this.endIfStmt;
    }

    public void setEndIfStmt(ASTEndIfStmtNode newValue)
    {
        this.endIfStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    public ASTElseIfConstructNode getElseIfConstruct()
    {
        return this.elseIfConstruct;
    }

    public void setElseIfConstruct(ASTElseIfConstructNode newValue)
    {
        this.elseIfConstruct = newValue;
        if (newValue != null) newValue.setParent(this);
    }

/*
    public ASTElseConstructNode getElseConstruct()
    {
        return this.elseConstruct;
    }

    public void setElseConstruct(ASTElseConstructNode newValue)
    {
        this.elseConstruct = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTElseIfConstructNode(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 5;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return new ASTNullNode(); // this.elseIfStmt;
        case 1:  return new ASTNullNode(); // this.conditionalBody;
        case 2:  return new ASTNullNode(); // this.endIfStmt;
        case 3:  return this.elseIfConstruct;
        case 4:  return new ASTNullNode(); // this.elseConstruct;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  return; //this.elseIfStmt = (ASTElseIfStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  return; //this.conditionalBody = (IASTListNode<IExecutionPartConstruct>)value; if (value != null) value.setParent(this); return;
        case 2:  return; //this.endIfStmt = (ASTEndIfStmtNode)value; if (value != null) value.setParent(this); return;
        case 3:  this.elseIfConstruct = (ASTElseIfConstructNode)value; if (value != null) value.setParent(this); return;
        case 4:  return; //this.elseConstruct = (ASTElseConstructNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

}
