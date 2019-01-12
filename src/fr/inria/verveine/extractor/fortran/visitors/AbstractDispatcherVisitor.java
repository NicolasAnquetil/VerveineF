package fr.inria.verveine.extractor.fortran.visitors;

import java.util.Stack;

import fr.inria.verveine.extractor.fortran.VerveineFParser;
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

	protected boolean allLocals;
	
	protected int verbose;


	public AbstractDispatcherVisitor(IRDictionary dico, String filename, boolean allLocals, int verbose) {
		super();
		this.dico = dico;
		this.filename = filename;
		this.allLocals = allLocals;
		this.verbose = verbose;

		this.context = new Stack<IREntity>();

		if ( (verbose > VerveineFParser.TRACE_NOTHING) && (msgTrace() != null) ) {
			System.out.println(msgTrace());
		}
	}

	abstract protected String msgTrace();

	protected String mkKey(IASTNode node) {
		return node.getClass().getSimpleName() + "/" + node.fullyQualifiedName();
	}

	protected void traceEntityCreation(IREntity entity) {
		if (verbose >= VerveineFParser.TRACE_ENTITIES) {
			System.out.println(" ++"+entity.toString());
		}
	}

}