package fr.inria.verveine.extractor.fortran.visitors;

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
import fr.inria.verveine.extractor.fortran.parser.ast.ASTUseStmtNode;

/**
 * 
 * Visitor walks through AST and create subprograms inside top level containers
 *
 */
public class UseModuleVisitor extends AbstractDispatcherVisitor {

	public UseModuleVisitor(IRDictionary dico, String filename, boolean allLocals) {
		super(dico, filename, allLocals);
	}

	@Override
	protected String msgTrace() {
		return "Recording modules used";
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
	public void visitASTUseStmtNode(ASTUseStmtNode node) {
		IREntity use = new IREntity(context.peek(), IRKind.USEMODULE);
		use.name(node.getName().getText());
		dico.addAnonymousEntity(use);
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		// pruning visiting AST
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		// pruning visiting AST
	}

}
