package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.EntityNotFoundException;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTNode;

public class CommentVisitor extends AbstractDispatcherVisitor {

	public CommentVisitor(IRDictionary dico, String filename, boolean allLocals) {
		super(dico, filename, allLocals);
	}

	@Override
	protected String msgTrace() {
		return "Adding comments to entities";
	}

	@Override
	public void visitASTCompilationUnit(ASTCompilationUnit node) {
		IREntity entity = dico.getEntityByKey(mkKey(node));
		context.push(entity);

		super.visitASTCompilationUnit(node);
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		IREntity entity =  dico.getEntityByKey( mkKey(node.getProgramStmt().getProgramName()));
		createCommentIfAny(node,entity, /*isEntityHeadComment*/true);

		context.push(entity);
		super.visitASTMainProgramNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node));
		createCommentIfAny(node, entity, /*isEntityHeadComment*/true);

		context.push(entity);
		super.visitASTModuleNode(node);
	}

	@Override
	public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node) {
		createCommentIfAny(node, context.peek(), /*isEntityHeadComment*/false);

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
	private void createCommentIfAny(ASTNode node, IREntity entity, boolean isEntityHeadComment) {
		if (entity == null) {
			new EntityNotFoundException("Entity key not found: "+entity);
		}

		String cmtString = node.findFirstToken().getWhiteBefore();
		int cmtStart = cmtString.indexOf('!');
		if (cmtStart >= 0) {
			IREntity irCmt;

			irCmt = new IREntity(entity, IRKind.COMMENT);
			irCmt.data(IREntity.COMMENT_CONTENT, cmtString);
			dico.addAnonymousEntity( irCmt);

			if (isEntityHeadComment) {
				// adjusting start of parent entity, removing the comment from it 
				Integer entStart = (Integer) entity.getData(IREntity.ANCHOR_START);
				if (entStart != null) {				
					entity.data(IREntity.ANCHOR_START, entStart - cmtString.length() ); 
				}
			}
		}
	}

}
