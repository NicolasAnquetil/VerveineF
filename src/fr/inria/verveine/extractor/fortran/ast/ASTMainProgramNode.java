package fr.inria.verveine.extractor.fortran.ast;

public class ASTMainProgramNode extends ASTNode implements IProgramUnit // extends ScopingNode
{
    ASTProgramStmtNode programStmt; // in ASTMainProgramNode
    IASTListNode<IBodyConstruct> body; // in ASTMainProgramNode
    //ASTContainsStmtNode containsStmt; // in ASTMainProgramNode
    //IASTListNode<IInternalSubprogram> internalSubprograms; // in ASTMainProgramNode
    ASTEndProgramStmtNode endProgramStmt; // in ASTMainProgramNode

    public ASTMainProgramNode() {
		setBody( new ASTListNode<>());
	}

    public ASTProgramStmtNode getProgramStmt()
    {
        return this.programStmt;
    }

    public void setProgramStmt(ASTProgramStmtNode newValue)
    {
        this.programStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }

    public IASTListNode<IBodyConstruct> getBody()
    {
        return this.body;
    }

    public void setBody(IASTListNode<IBodyConstruct> newValue)
    {
        this.body = newValue;
        if (newValue != null) newValue.setParent(this);
    }


/*
    public ASTContainsStmtNode getContainsStmt()
    {
        return this.containsStmt;
    }

    public void setContainsStmt(ASTContainsStmtNode newValue)
    {
        this.containsStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<IInternalSubprogram> getInternalSubprograms()
    {
        return this.internalSubprograms;
    }

    public void setInternalSubprograms(IASTListNode<IInternalSubprogram> newValue)
    {
        this.internalSubprograms = newValue;
        if (newValue != null) newValue.setParent(this);
    }
 */


    public ASTEndProgramStmtNode getEndProgramStmt()
    {
        return this.endProgramStmt;
    }

    public void setEndProgramStmt(ASTEndProgramStmtNode newValue)
    {
        this.endProgramStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }

    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTMainProgramNode(this);
        visitor.visitIProgramUnit(this);
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
        case 0:  return this.programStmt;
        case 1:  return this.body;
        case 2:  return new ASTNullNode(); //thisthis.containsStmt;
        case 3:  return new ASTNullNode(); //thisthis.internalSubprograms;
        case 4:  return this.endProgramStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.programStmt = (ASTProgramStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.body = (IASTListNode<IBodyConstruct>)value; if (value != null) value.setParent(this); return;
        case 2:  return; //this.containsStmt = (ASTContainsStmtNode)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.internalSubprograms = (IASTListNode<IInternalSubprogram>)value; if (value != null) value.setParent(this); return;
        case 4:  this.endProgramStmt = (ASTEndProgramStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public boolean isTopLevelNode() {
		return true;
	}
    
}
