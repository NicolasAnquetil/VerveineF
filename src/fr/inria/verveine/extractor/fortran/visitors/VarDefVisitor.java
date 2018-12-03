package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.ast.ASTAttrSpecSeqNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEntityDeclNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProgramStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.ast.ASTTypeDeclarationStmtNode;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;

public class VarDefVisitor extends AbstractDispatcherVisitor {

	public VarDefVisitor(IRDictionary dico, String filename) {
		super(dico, filename);
	}

	@Override
	protected String msgTrace() {
		return "Creating variables";
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
	public void visitASTProgramStmtNode(ASTProgramStmtNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	/*
	 * Because of AST visit pruning, here we should only have global variables
	 */
	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			ASTToken tk = decl.getObjectName().getObjectName();
			IREntity entity = dico.addEntity( mkKey(tk), IRKind.VARIABLE, /*parent*/context.peek());
			entity.name( tk.getText());
			entity.data(IREntity.IS_STUB, false);
			entity.data( IREntity.DECLARED_PARAM, varIsDeclaredParameter( node));
			entity.addSourceAnchor( filename, node);
		}
	}

	private boolean varIsDeclaredParameter(ASTTypeDeclarationStmtNode node) {
		if (node.getAttrSpecSeq() == null) {
			return false;
		}

		for ( ASTAttrSpecSeqNode spec : node.getAttrSpecSeq() ) {
			if ( (spec.getAttrSpec() != null) && (spec.getAttrSpec().isParameter()) ) {
				return true;
			}
		}
		return false;
	}

}
