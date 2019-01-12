package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;

/**
 * Visitor walks through the AST and create top level Containers (Program and Modules)
 */
public class ScopeDefVisitor extends AbstractDispatcherVisitor {

	public ScopeDefVisitor(IRDictionary dico, String filename, boolean allLocals, int verbose) {
		super(dico, filename, allLocals, verbose);
	}

	@Override
	protected String msgTrace() {
		return "Creating modules";
	}

	@Override
	public void visitASTCompilationUnit(ASTCompilationUnit node) {
		IREntity entity = dico.addEntity(mkKey(node), IRKind.COMPILATION_UNIT, /*parent*/null);
		entity.name(node.fullyQualifiedName());
		context.push(entity);

		traceEntityCreation(entity);

		super.visitASTCompilationUnit(node);
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		ASTToken tk = node.getProgramStmt().getProgramName();

		//Create the Famix Program from the ProgramNameNode which contains the name instead of using MainProgramNode
		IREntity entity = dico.addEntity(mkKey(tk), IRKind.PROGRAM, /*parent*/context.peek());
		entity.name(tk.getText());
		entity.data(IREntity.IS_STUB, false);
		entity.addSourceAnchor( filename, node);

		traceEntityCreation(entity);

		super.visitASTMainProgramNode(node);
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.addEntity( mkKey(node), IRKind.MODULE, /*parent*/context.peek());
		entity.name(node.basename());
		entity.data(IREntity.IS_STUB, false);
		entity.addSourceAnchor( filename, node);

		traceEntityCreation(entity);

		super.visitASTModuleNode(node);
	}

}
