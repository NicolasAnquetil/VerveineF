package fr.inria.verveine.extractor.fortran.parser;


import fr.inria.verveine.extractor.fortran.ast.ASTCaseConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTCaseStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.ast.ASTElseIfConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTIfConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTIfStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProgramStmtNode;
import fr.inria.verveine.extractor.fortran.ast.AbstractASTNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProperLoopConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTTypeDeclarationStmtNode;

public abstract class ASTVisitor {

	protected void visitChildren(AbstractASTNode node) {
		for (AbstractASTNode child : node.getChildren()) {
			child.accept(this);
		}
	}

	protected void visitASTNode(AbstractASTNode node) {
		this.visitChildren(node);
	}

	
	
	public void visitASTCompilationUnit(ASTCompilationUnit node) {
		this.visitASTNode(node);
	}

	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		this.visitASTNode(node);
	}

	public void visitASTModuleNode(ASTModuleNode node) {
		this.visitASTNode(node);
	}

	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		this.visitASTNode(node);
	}

	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		this.visitASTNode(node);
	}

	public void visitASTIfConstructNode(ASTIfConstructNode node) {
		this.visitASTNode(node);
	}

	public void visitASTElseIfConstructNode(ASTElseIfConstructNode node) {
		this.visitASTNode(node);
	}

	public void visitASTIfStmtNode(ASTIfStmtNode node) {
		this.visitASTNode(node);
	}

	public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node) {
		this.visitASTNode(node);
	}

	public void visitASTCaseConstructNode(ASTCaseConstructNode node) {
		this.visitASTNode(node);
	}

	public void visitASTCaseStmtNode(ASTCaseStmtNode node) {
		this.visitASTNode(node);
	}

	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		this.visitASTNode(node);	
	}

	public void visitASTProgramStmtNode(ASTProgramStmtNode node) {
		this.visitASTNode(node);
	}

}
