package fr.inria.verveine.extractor.fortran.visitors;

import java.util.Stack;

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
		return node.getClass().getSimpleName() + "/" + node.fullyQualifiedName();
	}

}