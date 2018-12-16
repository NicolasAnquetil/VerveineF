package fr.inria.verveine.extractor.fortran.ast;

public class ASTIfConstructNode extends ASTNode implements IExecutableConstruct
{
	//ASTIfThenStmtNode ifThenStmt; // in ASTIfConstructNode
    //IASTListNode<IExecutionPartConstruct> conditionalBody; // in ASTIfConstructNode
    //ASTElseConstructNode elseConstruct; // in ASTIfConstructNode
    //ASTEndIfStmtNode endIfStmt; // in ASTIfConstructNode
    ASTElseIfConstructNode elseIfConstruct; // in ASTIfConstructNode

/*
    public ASTIfThenStmtNode getIfThenStmt()
    {
        return this.ifThenStmt;
    }

    public void setIfThenStmt(ASTIfThenStmtNode newValue)
    {
        this.ifThenStmt = newValue;
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


    public ASTElseConstructNode getElseConstruct()
    {
        return this.elseConstruct;
    }

    public void setElseConstruct(ASTElseConstructNode newValue)
    {
        this.elseConstruct = newValue;
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


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTIfConstructNode(this);
//        visitor.visitIExecutableConstruct(this);
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
        case 0:  return new ASTNullNode(); //this.ifThenStmt;
        case 1:  return new ASTNullNode(); //this.conditionalBody;
        case 2:  return new ASTNullNode(); //this.elseConstruct;
        case 3:  return new ASTNullNode(); //this.endIfStmt;
        case 4:  return this.elseIfConstruct;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  return; //this.ifThenStmt = (ASTIfThenStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  return; //this.conditionalBody = (IASTListNode<IExecutionPartConstruct>)value; if (value != null) value.setParent(this); return;
        case 2:  return; //this.elseConstruct = (ASTElseConstructNode)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.endIfStmt = (ASTEndIfStmtNode)value; if (value != null) value.setParent(this); return;
        case 4:  this.elseIfConstruct = (ASTElseIfConstructNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
