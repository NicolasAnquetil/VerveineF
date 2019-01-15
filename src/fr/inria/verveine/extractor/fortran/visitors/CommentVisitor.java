package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.EntityNotFoundException;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEntityDeclNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTTypeDeclarationStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;

public class CommentVisitor extends AbstractDispatcherVisitor {

	public CommentVisitor(IRDictionary dico, String filename, boolean allLocals, int verbose) {
		super(dico, filename, allLocals, verbose);
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

		context.push(entity);
		super.visitASTMainProgramNode(node);  // visit children first to let them grab their tokens

		IREntity cmt = createCommentIfAny(node,entity);
		adjustStartAnchor(entity, cmt);
		
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node));

		context.push(entity);
		super.visitASTModuleNode(node);
		context.pop();
		
		IREntity cmt = createCommentIfAny(node, entity);
		adjustStartAnchor(entity, cmt);
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node));

		context.push(entity);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();

		createCommentIfAny(node, entity);
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node));

		context.push(entity);  // visit children first to let them grab their tokens
		super.visitASTSubroutineSubprogramNode(node);
		context.pop();

		createCommentIfAny(node, entity);
	}


	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			ASTToken tk = decl.getObjectName();
			IREntity entity = dico.getEntityByKey( mkKey(tk));
			createCommentIfAny(node, entity);
		}
	}


	// UTILITIES

	/**
	 * Search for a comment inside or preceding <code>node<code>.<br>
	 * If found, associates the comment with the <code>entity</code>.
	 * @return 
	 */
	protected IREntity createCommentIfAny(ASTNode node, IREntity entity) {
		IREntity firstCmt = null;
		int posFirstCmt = -1;

		if (entity == null) {
			// missing commented entity (e.g. a local var)
			// we take its parent (e.g. a subprogram)
			entity = context.peek();
		}

		for ( ASTToken tk : node.findAll(ASTToken.class) ) {
			String cmtString = tk.getWhiteBefore().trim();
			
			if ( (cmtString.length() > 0) && (cmtString.charAt(0) == '!') ) {
				
				// remember first comment for this entity (to return it)
				IREntity tmpCmt = createCommentForEntity(entity, tk.getWhiteBefore());
				if ( (posFirstCmt == -1) || (posFirstCmt > tk.getStartIndex()) ) {
					posFirstCmt = tk.getStartIndex();
					firstCmt = tmpCmt;
				}

				// "remove" whiteBefore so that this comment is not assigned to another entity
				tk.setWhiteBefore("");
			}

		}
		return firstCmt;
	}

	protected IREntity createCommentForEntity(IREntity entity, String cmtString) {
		IREntity irCmt;

		irCmt = dico.addAnonymousEntity(IRKind.COMMENT, entity);
		irCmt.data(IREntity.COMMENT_CONTENT, cmtString);

		traceEntityCreation(irCmt);

		return irCmt;
	}

	protected void adjustStartAnchor(IREntity entity, IREntity cmt) {
		if (cmt != null) {
			Integer entStart = (Integer) entity.getData(IREntity.ANCHOR_START);
			if (entStart != null) {				
				entity.data(IREntity.ANCHOR_START, entStart - ((String)cmt.getData(IREntity.COMMENT_CONTENT)).length() ); 
			}
		}
	}

}
