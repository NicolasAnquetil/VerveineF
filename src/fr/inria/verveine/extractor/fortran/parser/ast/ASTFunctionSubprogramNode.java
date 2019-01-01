package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTFunctionSubprogramNode extends ASTNode implements IInternalSubprogram, IModuleBodyConstruct,
		IModuleSubprogram, IModuleSubprogramPartConstruct, IProgramUnit // extends ScopingNode
{
	ASTFunctionStmtNode functionStmt;
	IASTListNode<IBodyConstruct> body;
	// ASTContainsStmtNode containsStmt;
	// IASTListNode<IInternalSubprogram> internalSubprograms;
	ASTEndFunctionStmtNode endFunctionStmt;

	public ASTFunctionSubprogramNode() {
		setBody(new ASTListNode<>());
	}

	public ASTFunctionStmtNode getFunctionStmt() {
		return this.functionStmt;
	}

	public void setFunctionStmt(ASTFunctionStmtNode newValue) {
		this.functionStmt = newValue;
		if (newValue != null)
			newValue.setParent(this);
	}

	public IASTListNode<IBodyConstruct> getBody() {
		return this.body;
	}

	public void setBody(IASTListNode<IBodyConstruct> newValue) {
		this.body = newValue;
		if (newValue != null)
			newValue.setParent(this);
	}

	/*
	 * public ASTContainsStmtNode getContainsStmt() { return this.containsStmt; }
	 * 
	 * public void setContainsStmt(ASTContainsStmtNode newValue) { this.containsStmt
	 * = newValue; if (newValue != null) newValue.setParent(this); }
	 * 
	 * 
	 * public IASTListNode<IInternalSubprogram> getInternalSubprograms() { return
	 * this.internalSubprograms; }
	 * 
	 * public void setInternalSubprograms(IASTListNode<IInternalSubprogram>
	 * newValue) { this.internalSubprograms = newValue; if (newValue != null)
	 * newValue.setParent(this); }
	 */

	@Override
	public IASTNode getParent() {
		return super.getParent().getParent(); // immediate parent is an ASTListNode
	}

	public ASTEndFunctionStmtNode getEndFunctionStmt() {
		return this.endFunctionStmt;
	}

	public void setEndFunctionStmt(ASTEndFunctionStmtNode newValue) {
		this.endFunctionStmt = newValue;
		if (newValue != null)
			newValue.setParent(this);
	}

	@Override
	public void accept(IASTVisitor visitor) {
		visitor.visitASTFunctionSubprogramNode(this);
//		visitor.visitIInternalSubprogram(this);
//		visitor.visitIModuleBodyConstruct(this);
//		visitor.visitIModuleSubprogram(this);
//		visitor.visitIModuleSubprogramPartConstruct(this);
//		visitor.visitIProgramUnit(this);
//		visitor.visitASTNode(this);
	}

	@Override
	protected int getNumASTFields() {
		return 5;
	}

	@Override
	protected IASTNode getASTField(int index) {
		switch (index) {
		case 0:
			return this.functionStmt;
		case 1:
			return this.body;
		case 2:
			return new ASTNullNode(); // this.containsStmt;
		case 3:
			return new ASTNullNode(); // this.internalSubprograms;
		case 4:
			return this.endFunctionStmt;
		default:
			throw new IllegalArgumentException("Invalid index");
		}
	}

	@Override
	protected void setASTField(int index, IASTNode value) {
		switch (index) {
		case 0:
			this.functionStmt = (ASTFunctionStmtNode) value;
			if (value != null)
				value.setParent(this);
			return;
		case 1:
			this.body = (IASTListNode<IBodyConstruct>) value;
			if (value != null)
				value.setParent(this);
			return;
		case 2:
			return; // this.containsStmt = (ASTContainsStmtNode)value; if (value != null)
					// value.setParent(this); return;
		case 3:
			return; // this.internalSubprograms = (IASTListNode<IInternalSubprogram>)value; if
					// (value != null) value.setParent(this); return;
		case 4:
			this.endFunctionStmt = (ASTEndFunctionStmtNode) value;
			if (value != null)
				value.setParent(this);
			return;
		default:
			throw new IllegalArgumentException("Invalid index");
		}
	}

	public String basename() {
		return getNameToken().getText();
	}

	/**
	 * Get the Token representing the name of the Node
	 * 
	 * @return
	 */
	public ASTToken getNameToken() {
		return this.getFunctionStmt().getFunctionName();
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

	@Override
	public boolean isTopLevelNode() {
		return true;
	}
    
}