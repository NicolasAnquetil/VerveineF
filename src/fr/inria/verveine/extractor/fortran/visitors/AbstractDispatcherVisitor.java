package fr.inria.verveine.extractor.fortran.visitors;

import eu.synectique.verveine.core.EntityStack;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.AbstractASTNamedNode;
import fr.inria.verveine.extractor.fortran.parser.ASTVisitor;

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

	protected String mkKey(AbstractASTNamedNode node) {
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
	// UTILITIES ======================================================================================================

}