package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTModuleNode extends ASTNode implements IProgramUnit // extends ScopingNode
{
    ASTModuleStmtNode moduleStmt;
    IASTListNode<IModuleBodyConstruct> moduleBody;
    ASTEndModuleStmtNode endModuleStmt;

    public ASTModuleNode() {
		setModuleBody( new ASTListNode<>());
	}

	public ASTModuleStmtNode getModuleStmt()
    {
        return this.moduleStmt;
    }

    public void setModuleStmt(ASTModuleStmtNode newValue)
    {
        this.moduleStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<IModuleBodyConstruct> getModuleBody()
    {
        return this.moduleBody;
    }

    public void setModuleBody(IASTListNode<IModuleBodyConstruct> newValue)
    {
        this.moduleBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }

    public void addModuleBody(IModuleBodyConstruct newValue)
    {
    	this.moduleBody.add(newValue);
    }


    public ASTEndModuleStmtNode getEndModuleStmt()
    {
        return this.endModuleStmt;
    }

    public void setEndModuleStmt(ASTEndModuleStmtNode newValue)
    {
        this.endModuleStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


	@Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTModuleNode(this);
//        visitor.visitIProgramUnit(this);
//        visitor.visitASTNode(this);
    }

    @Override 
    protected int getNumASTFields()
    {
        return 3;
    }

    @Override 
    protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.moduleStmt;
        case 1:  return this.moduleBody;
        case 2:  return this.endModuleStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override 
    protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.moduleStmt = (ASTModuleStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.moduleBody = (IASTListNode<IModuleBodyConstruct>)value; if (value != null) value.setParent(this); return;
        case 2: this.endModuleStmt = (ASTEndModuleStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public String basename() {
		return getModuleStmt().getModuleName().getText();
	}

	/**
	 * Get the Token representing the name of the Node
	 * 
	 * @return
	 */
	public ASTToken getNameToken() {
		return this.getModuleStmt().getModuleName();
	}

	@Override
	public boolean isTopLevelNode() {
		return true;
	}
    
}
