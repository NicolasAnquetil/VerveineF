package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.Options;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCaseConstructNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCaseStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTElseIfConstructNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndFunctionStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndProgramStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndSubroutineStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTIfConstructNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTIfStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTProperLoopConstructNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;

/**
 * 
 * Visitor walks through AST and create subprograms inside top level containers
 *
 */
public class SubprgDefVisitor extends AbstractDispatcherVisitor {

	public SubprgDefVisitor(IRDictionary dico, String filename, Options options) {
		super(dico, filename, options);
	}

	@Override
	protected String msgTrace() {
		return "Creating subprograms";
	}

	@Override
	public void visitASTCompilationUnit(ASTCompilationUnit node) {
		IREntity entity = dico.getEntityByKey(mkKey(node));
		context.push(entity);

		super.visitASTCompilationUnit(node);
	}

	/**
	 * Retrieve previously added Program. Done with MainProgram/ProgramStmt/ProgramName
	 */
	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		ASTToken tk = node.getProgramStmt().getProgramName();
		
		IREntity entity = dico.getEntityByKey( mkKey(tk) );
		entity.data(IREntity.CYCLOMATIC_COMPLEXITY, 1);

		context.push(entity);
		super.visitASTMainProgramNode(node);
	}

	@Override
	public void visitASTEndProgramStmtNode(ASTEndProgramStmtNode node) {
		super.visitASTEndProgramStmtNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node) );

		context.push(entity);
		super.visitASTModuleNode(node);
	}

	@Override
	public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node) {
		super.visitASTEndModuleStmtNode(node);
		context.pop();
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		IREntity entity = dico.addEntity( mkKey(node), IRKind.FUNCTION, /*parent*/context.peek());
		entity.name(node.basename());
		entity.data(IREntity.IS_STUB, false);	
		entity.addSourceAnchor( filename, node);
		entity.data(IREntity.CYCLOMATIC_COMPLEXITY, 1);

		traceEntityCreation(entity);

		context.push(entity);
		super.visitASTFunctionSubprogramNode(node);
	}

	@Override
	public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node) {
		super.visitASTEndFunctionStmtNode(node);
		context.pop();
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		IREntity entity = dico.addEntity( mkKey(node), IRKind.SUBPROGRAM, /*parent*/context.peek());
		entity.name(node.basename());
		entity.data(IREntity.IS_STUB, false);
		entity.addSourceAnchor( filename, node);
		entity.data(IREntity.CYCLOMATIC_COMPLEXITY, 1);

		traceEntityCreation(entity);

		context.push(entity);
		super.visitASTSubroutineSubprogramNode(node);
	}

	@Override
	public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node) {
		super.visitASTEndSubroutineStmtNode(node);
		context.pop();
	}

	
	// Handles cyclomatic complexity metric

	@Override
	public void visitASTIfConstructNode(ASTIfConstructNode node) {
		incrementCyclomatic();
		super.visitASTIfConstructNode(node);
	}

	@Override
	public void visitASTElseIfConstructNode(ASTElseIfConstructNode node) {
		incrementCyclomatic();
		super.visitASTElseIfConstructNode(node);
	}

	@Override
	public void visitASTIfStmtNode(ASTIfStmtNode node) {
		incrementCyclomatic();
		super.visitASTIfStmtNode(node);
	}

	@Override
	public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node) {
		incrementCyclomatic();
		super.visitASTProperLoopConstructNode(node);
	}

	@Override
	public void visitASTCaseConstructNode(ASTCaseConstructNode node) {
		/* how should we compute CASE statement ?
		 * for now only add 1 for the entire SELECT statement,
		 * theoretically should add 1 for each CASE
		 */
		incrementCyclomatic();
		super.visitASTCaseConstructNode(node);
	}

	@Override
	public void visitASTCaseStmtNode(ASTCaseStmtNode node) {
		/* how should we compute CASE statement ?
		 * for now only add 1 for the entire SELECT statement,
		 * theoretically should add 1 for each CASE
		 */
		//incrementSubprogCyclomatic();
		super.visitASTCaseStmtNode(node);
	}

	// UTILITIES

	private void incrementCyclomatic() {
		IREntity behavioural = context.peek();
		int cc = (int) behavioural.getData(IREntity.CYCLOMATIC_COMPLEXITY);
		cc++;
		behavioural.data(IREntity.CYCLOMATIC_COMPLEXITY, cc);
	}

	/*
	private void incrementNbOfStmts() {
		NamedEntity top = context.topBehaviouralEntity();
		if (top instanceof BehaviouralEntity) {
			BehaviouralEntity fmx = (BehaviouralEntity)top;

			fmx.setNumberOfStatements( (int)fmx.getNumberOfStatements() + 1);
		}
	}
	 */
	
}
