package fr.inria.verveine.extractor.fortran.ast;

import java.util.ArrayList;
import java.util.List;

import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

public class ASTMainProgramNode extends AbstractASTNamedNode {

	public ASTMainProgramNode(AbstractASTNode parent) {
		super(parent);
	}

	@Override
	public List<AbstractASTNode> getChildren() {
		return new ArrayList<>();
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitASTMainProgramNode(this);
	}

}
