package fr.inria.verveine.extractor.fortran.visitors;

import java.util.Stack;

import fr.inria.verveine.extractor.fortran.Options;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTVisitor;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

/**
 * The superclass of all visitors. These visitors visit an AST to create FAMIX entities.
 */
public abstract class AbstractDispatcherVisitor extends ASTVisitor {

	protected IRDictionary dico;
	
	protected String filename;
	
	protected  Stack<IREntity> context;

	protected Options options;


	public AbstractDispatcherVisitor(IRDictionary dico, String filename, Options options) {
		super();
		this.dico = dico;
		this.filename = filename;
		this.options = options;

		this.context = new Stack<IREntity>();

		if ( (options.getVerbose() > Options.TRACE_NOTHING) && (msgTrace() != null) ) {
			System.out.println(msgTrace());
		}
	}

	abstract protected String msgTrace();

	protected String mkKey(IASTNode node) {
		return node.getClass().getSimpleName() + "/" + node.fullyQualifiedName();
	}

	protected void traceEntityCreation(IREntity entity) {
		if (options.getVerbose() > Options.TRACE_ENTITIES) {
			System.out.println(" ++"+entity.toString());
		}
	}

}