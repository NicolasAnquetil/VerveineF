package fr.inria.verveine.extractor.fortran.ast;

import java.util.ArrayList;
import java.util.List;

import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

public class ASTCompilationUnit extends AbstractASTNode {

	protected String filename;
	
	protected List<AbstractASTNode> definitions;

	public ASTCompilationUnit(String filename) {
		super(null);
		this.filename = filename;
		definitions = new ArrayList<>();
	}

	@Override
	public List<AbstractASTNode> getChildren() {
		return definitions;
	}

	public void addDefinition(AbstractASTNode node) {
		definitions.add(node);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitASTCompilationUnit(this);
	}

}
