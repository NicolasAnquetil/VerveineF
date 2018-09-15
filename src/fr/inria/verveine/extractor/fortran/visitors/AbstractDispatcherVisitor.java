package fr.inria.verveine.extractor.fortran.visitors;

import eu.synectique.verveine.core.EntityStack;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.ast.ASTVisitor;
import fr.inria.verveine.extractor.fortran.ast.IASTNode;

/**
 * The superclass of all visitors. These visitors visit an AST to create FAMIX entities.
 */
public abstract class AbstractDispatcherVisitor extends ASTVisitor {

	protected FDictionary dico;
	
	protected String filename;
	
	protected EntityStack context;

	// CONSTRUCTOR ==========================================================================================================================

	public AbstractDispatcherVisitor(FDictionary dico) {
		super();
		this.dico = dico;

		this.context = new EntityStack();

		if (msgTrace() != null ) {
			System.out.println(msgTrace());
		}
	}

	abstract protected String msgTrace();

	protected String mkKey(IASTNode node) {
		return node.getClass().getName() + "/" + node.fullyQualifiedName();
	}

	// VISITING METODS ON ICELEMENT HIERARCHY (ICElementVisitor) ===========================================================================
/*
	public void visit(ITranslationUnit elt) {
		// this is the method merging ICElementVisitor and ASTVisitor
		filename = elt.getElementName();
		if (elt.getElementName().toLowerCase().endsWith(".f90") ) {
			IFortranAST ast =  vpg.acquireTransientAST(elt.getFile());
			ast.accept(this);
		}
	}
*/
}