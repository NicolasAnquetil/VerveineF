package fr.inria.verveine.extractor.fortran.visitors;

import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Program;
import eu.synectique.verveine.core.gen.famix.ScopingEntity;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTCaseConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTCaseStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTElseIfConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTIfConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTIfStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProperLoopConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;

public class SubprgDefVisitor extends AbstractDispatcherVisitor {

	public SubprgDefVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating subprograms";
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		
		Program fmx =  (Program) dico.getEntityByKey( mkKey(node) );
		fmx.setCyclomaticComplexity( 1);

		context.push(fmx);
		super.visitASTMainProgramNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		Module mod = (Module) dico.getEntityByKey( mkKey(node) );

		context.push(mod);
		super.visitASTModuleNode(node);
		context.pop();
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( mkKey(node), node.basename(), /*sig*/node.basename(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);	
		dico.addSourceAnchor(fmx, filename, node);
		fmx.setCyclomaticComplexity( 1);
		
		context.push(fmx);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( mkKey(node), node.basename(), /*sig*/node.basename(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
		fmx.setCyclomaticComplexity( 1);

		context.push(fmx);
		super.visitASTSubroutineSubprogramNode(node);
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
		NamedEntity top = context.topBehaviouralEntity();
		if (top instanceof BehaviouralEntity) {
			BehaviouralEntity fmx = (BehaviouralEntity)top;

			fmx.setCyclomaticComplexity( (int)fmx.getCyclomaticComplexity() + 1);
		}
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
