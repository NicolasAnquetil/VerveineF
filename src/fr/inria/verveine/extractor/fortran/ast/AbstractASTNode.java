package fr.inria.verveine.extractor.fortran.ast;

import java.util.List;

import fortran.ofp.parser.java.FortranToken;
import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

public abstract class AbstractASTNode {

	protected AbstractASTNode parent;
	
	protected FortranToken firstToken;
	
	protected FortranToken lastToken;

	public AbstractASTNode(AbstractASTNode parent) {
		this.parent = parent;
	}
	
	public FortranToken getFirstToken() {
		return firstToken;
	}
	
	public void setFirstToken(FortranToken firstToken) {
		this.firstToken = firstToken;
	}
	
	public FortranToken getLastToken() {
		return lastToken;
	}
	
	public void setLastToken(FortranToken lastToken) {
		this.lastToken = lastToken;
	}
	
	public AbstractASTNode getParentNode() {
		return parent;
	}

	public abstract <T extends AbstractASTNode> List<T> getChildren();
	
	public boolean hasChildren() {
		return ! getChildren().isEmpty();
	}

	public abstract void accept(ASTVisitor visitor);

	public String fullyQualifiedName() {
		return "[]";
	}

}
