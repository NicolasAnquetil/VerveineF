package fr.inria.verveine.extractor.fortran.ast;

import java.util.ArrayList;
import java.util.List;

import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

public class ASTFunctionSubprogramNode extends AbstractASTSubprogramNode {

	public ASTFunctionSubprogramNode(AbstractASTNode parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<AbstractASTNode> getChildren() {
		return new ArrayList<AbstractASTNode>();
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitASTFunctionSubprogramNode(this);
	}

}
