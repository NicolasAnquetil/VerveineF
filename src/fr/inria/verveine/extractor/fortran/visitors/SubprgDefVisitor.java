package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.analysis.loops.ASTProperLoopConstructNode;
import org.eclipse.photran.internal.core.parser.ASTCaseConstructNode;
import org.eclipse.photran.internal.core.parser.ASTCaseStmtNode;
import org.eclipse.photran.internal.core.parser.ASTElseIfConstructNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTIfConstructNode;
import org.eclipse.photran.internal.core.parser.ASTIfStmtNode;
import org.eclipse.photran.internal.core.parser.ASTMainProgramNode;
import org.eclipse.photran.internal.core.parser.ASTModuleNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Program;
import eu.synectique.verveine.core.gen.famix.ScopingEntity;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
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
		
		Program fmx =  (Program) dico.getEntityByKey( firstDefinition(node.getProgramStmt().getProgramName().getProgramName()) );
		fmx.setCyclomaticComplexity( 1);

		context.push(fmx);
		super.visitASTMainProgramNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		Module mod = (Module) dico.getEntityByKey( firstDefinition(node.getNameToken()) );

		context.push(mod);
		super.visitASTModuleNode(node);
		context.pop();
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);	
		dico.addSourceAnchor(fmx, filename, node);
		fmx.setCyclomaticComplexity( 1);
		
		context.push(fmx);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/(ScopingEntity)context.top());
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
