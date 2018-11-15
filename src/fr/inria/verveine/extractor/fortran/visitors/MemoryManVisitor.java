package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTAllocateStmtNode;


public class MemoryManVisitor extends AbstractDispatcherVisitor {

	public MemoryManVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "creating allocations";
	}

	
	@Override public void visitASTAllocateStmtNode(ASTAllocateStmtNode node) {
		//Corresponding Famix enity is absent
		/*ASTToken tk = node.getProgramStmt().getProgramName().getProgramName();
		
		Program fmx = dico.ensureFamixEntity( Program.class, mkKey(tk), tk.getText());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);*/
	}
	


}
