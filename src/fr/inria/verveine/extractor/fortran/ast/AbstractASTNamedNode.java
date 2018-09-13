package fr.inria.verveine.extractor.fortran.ast;

import org.antlr.runtime.Token;

public abstract class AbstractASTNamedNode extends AbstractASTNode {

	protected Token name;

	public AbstractASTNamedNode(AbstractASTNode parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	public Token getName() {
		return name;
	}
	
	public void setName(Token name) {
		this.name = name;
	}

	@Override
	public String fullyQualifiedName() {
		return getParentNode().fullyQualifiedName()+"."+getName();
	}

}
