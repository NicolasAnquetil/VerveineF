package fr.inria.verveine.extractor.fortran.ast;

import java.util.ArrayList;
import java.util.List;


import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

public class ASTModuleNode extends AbstractASTNamedNode {

	protected List<AbstractASTSubprogramNode> declarations;
	
	public ASTModuleNode(AbstractASTNode parent) {
		super(parent);
		declarations = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public  <T extends AbstractASTNode> List<T> getChildren() {
		return (List<T>)getDeclarations();
	}

	public void addDeclaration(AbstractASTSubprogramNode node) {
		declarations.add(node);
	}

	public List<AbstractASTSubprogramNode> getDeclarations() {
		return declarations;
	}

	public void clearDeclarations() {
		declarations.clear();
	}

	public void removeDeclaration(AbstractASTSubprogramNode node) {
		declarations.remove(node);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitASTModuleNode(this);
	}

}
