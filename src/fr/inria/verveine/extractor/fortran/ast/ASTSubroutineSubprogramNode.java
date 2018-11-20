package fr.inria.verveine.extractor.fortran.ast;

public class ASTSubroutineSubprogramNode extends ASTNode implements IInternalSubprogram, IModuleBodyConstruct, IModuleSubprogram, IModuleSubprogramPartConstruct, IProgramUnit // extends ScopingNode
{
	ASTSubroutineStmtNode subroutineStmt; // in ASTSubroutineSubprogramNode
    IASTListNode<IBodyConstruct> body; // in ASTSubroutineSubprogramNode
    //ASTContainsStmtNode containsStmt; // in ASTSubroutineSubprogramNode
    //IASTListNode<IInternalSubprogram> internalSubprograms; // in ASTSubroutineSubprogramNode
    ASTEndSubroutineStmtNode endSubroutineStmt; // in ASTSubroutineSubprogramNode

    public ASTSubroutineSubprogramNode() {
    	setBody( new ASTListNode<>());
	}

    public ASTSubroutineStmtNode getSubroutineStmt()
    {
        return this.subroutineStmt;
    }

    public void setSubroutineStmt(ASTSubroutineStmtNode newValue)
    {
        this.subroutineStmt = newValue;
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
        this.containsStmt = newValue;	public ASTSubroutineSubprogramNode(IASTNode parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	

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

    public ASTEndSubroutineStmtNode getEndSubroutineStmt()
    {
        return this.endSubroutineStmt;
    }

    public void setEndSubroutineStmt(ASTEndSubroutineStmtNode newValue)
    {
        this.endSubroutineStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTSubroutineSubprogramNode(this);
        visitor.visitIInternalSubprogram(this);
        visitor.visitIModuleBodyConstruct(this);
        visitor.visitIModuleSubprogram(this);
        visitor.visitIModuleSubprogramPartConstruct(this);
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
        case 0:  return this.subroutineStmt;
        case 1:  return this.body;
        case 2:  return new ASTNullNode(); //this.containsStmt;
        case 3:  return new ASTNullNode(); //this.internalSubprograms;
        case 4:  return this.endSubroutineStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.subroutineStmt = (ASTSubroutineStmtNode)value; if (value != null) value.setParent(this); return;
        case 1:  this.body = (IASTListNode<IBodyConstruct>)value; if (value != null) value.setParent(this); return;
        case 2:  return; //this.containsStmt = (ASTContainsStmtNode)value; if (value != null) value.setParent(this); return;
        case 3:  return; //this.internalSubprograms = (IASTListNode<IInternalSubprogram>)value; if (value != null) value.setParent(this); return;
        case 4:  this.endSubroutineStmt = (ASTEndSubroutineStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public IASTNode getParent() {
		return super.getParent().getParent();  // immediate parent is an ASTListNode
	}

	public String basename() {
		return getSubroutineStmt().getSubroutineName().getSubroutineName().getText();
	}
	
	/**
	 * Get the Token representing the name of the Node
	 * 
	 * @return
	 */
	public ASTToken getNameToken() {
		return this.getSubroutineStmt().getSubroutineName().getSubroutineName();
	}

	/**
	 * Get the string name of the node
	 * 
	 * @param force
	 * @return
	 */
	public String getName() {
		ASTToken nameToken = getNameToken();
		return nameToken == null ? null : nameToken.getText();
	}

}
