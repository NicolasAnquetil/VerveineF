package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.EntityNotFoundException;
import fr.inria.verveine.extractor.fortran.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;

public class CommentVisitor extends AbstractDispatcherVisitor {

	public CommentVisitor(IRDictionary dico, String filename) {
		super(dico, filename);
	}

	@Override
	protected String msgTrace() {
		return "Adding comments to entities";
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		IREntity entity =  dico.getEntityByKey( mkKey(node.getProgramStmt().getProgramName().getProgramName()));
		createCommentIfNonBlank(node,entity);

		context.push(entity);
		super.visitASTMainProgramNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node));
		createCommentIfNonBlank(node, entity);

		context.push(entity);
		super.visitASTModuleNode(node);
	}

	@Override
	public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node) {
		createCommentIfNonBlank(node, context.peek());

		super.visitASTEndModuleStmtNode(node);

		context.pop();
	}
/*
	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = (Function) dico.getEntityByKey( mkKey(node.getNameToken()));
		createCommentIfNonBlank(node, fmx);
		
		/*context.push(fmx);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();* /
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = (Function) dico.getEntityByKey( mkKey(node.getNameToken()));
		createCommentIfNonBlank(node, fmx);

		/*context.push(fmx);
		super.visitASTSubroutineSubprogramNode(node);
		context.pop();* /
	}

	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		GlobalVariable fmx;
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			// Because of AST visit pruning, here we should only have global variables
			ASTToken tk = decl.getObjectName().getObjectName();
			fmx= (GlobalVariable) dico.getEntityByKey( mkKey(tk));
			createCommentIfNonBlank(node, fmx);
		}
	}

	
	// UTILITIES

	/**
	 * Search for a comment  preceding <code>node<code>. If found, associates the comment with the <code>entity</code> entity. <br>
	 * 
	 * TODO Deal with #ifdef / #endif that are also "white"
	 */
	private void createCommentIfNonBlank(ASTNode node, IREntity entity) {
		if (entity == null) {
			new EntityNotFoundException("Entity key not found: "+entity);
		}

		String cmtString = node.findFirstToken().getWhiteBefore();
		int cmtStart = cmtString.indexOf('!');
		if (cmtStart >= 0) {
			IREntity irCmt;

			irCmt = new IREntity(entity, IRKind.COMMENT);
			irCmt.data("content", cmtString);
			dico.addAnonymousEntity( irCmt);
			
			// adjusting start of parent entity, removing the comment from it 
			String entStart = entity.getData("anchorstart");
			if (entStart != null) {				
				entity.data("anchorstart", "" + (Integer.parseInt(entStart) - cmtString.length()) ); 
			}
		}
	}
	
}
