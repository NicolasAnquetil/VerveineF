package fr.inria.verveine.extractor.fortran.visitors;

import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.Program;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;


public class ScopeDefVisitor extends AbstractDispatcherVisitor {

	public ScopeDefVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "creating modules";
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		ASTToken tk = node.getProgramStmt().getProgramName().getProgramName();
		
		Program fmx = dico.ensureFamixEntity( Program.class, mkKey(tk), tk.getText());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		Module fmx = dico.ensureFamixModule( mkKey(node), node.basename(), /*owner*/null);
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);

		super.visitASTModuleNode(node);
	}

}
