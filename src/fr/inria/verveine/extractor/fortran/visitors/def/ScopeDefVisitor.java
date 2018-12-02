package fr.inria.verveine.extractor.fortran.visitors.def;

import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.visitors.AbstractDispatcherVisitor;

/**
 * First Pass
 * 
 * Visitor walks through the AST and create top level Containers (Program and Modules)
 *
 */
public class ScopeDefVisitor extends AbstractDispatcherVisitor {

	public ScopeDefVisitor(IRDictionary dico, String filename) {
		super(dico, filename);
	}

	@Override
	protected String msgTrace() {
		return "creating modules";
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		ASTToken tk = node.getProgramStmt().getProgramName().getProgramName();
		
		//Create the Famix Program from the ProgramNameNode which contains the name instead of using MAinProgramNode
		IREntity entity = dico.addEntity(mkKey(tk), IRKind.PROGRAM, /*parent*/null);
		entity.stub(false);
		entity.addSourceAnchor( filename, node);
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.addEntity( mkKey(node), IRKind.MODULE, /*parent*/null);
		entity.name(node.basename());
		entity.stub(false);
		entity.addSourceAnchor( filename, node);

		super.visitASTModuleNode(node);
	}

}
