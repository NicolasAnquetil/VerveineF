package fr.inria.verveine.extractor.fortran.visitors;

import java.util.Stack;

import eu.synectique.verveine.core.EntityStack;
import fr.inria.verveine.extractor.fortran.ast.ASTVisitor;
import fr.inria.verveine.extractor.fortran.ast.IASTNode;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;

/**
 * The superclass of all visitors. These visitors visit an AST to create FAMIX entities.
 */
public abstract class AbstractDispatcherVisitor extends ASTVisitor {

	protected IRDictionary dico;
	
	protected String filename;
	
	protected  Stack<IREntity> context;

	// CONSTRUCTOR ==========================================================================================================================

	public AbstractDispatcherVisitor(IRDictionary dico, String filename) {
		super();
		this.dico = dico;
		this.filename = filename;

		this.context = new Stack<IREntity>();

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