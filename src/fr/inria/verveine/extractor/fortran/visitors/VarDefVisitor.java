package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.Options;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTAttrSpecSeqNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndFunctionStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndModuleStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndProgramStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndSubroutineStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEntityDeclNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTTypeDeclarationStmtNode;

public class VarDefVisitor extends AbstractDispatcherVisitor {

	public VarDefVisitor(IRDictionary dico, String filename, Options options) {
		super(dico, filename, options);
	}

	@Override
	protected String msgTrace() {
		return "Creating variables";
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
		if (options.withAllLocals()) {
			IREntity entity = dico.getEntityByKey( mkKey(node) );
			
			context.push(entity);
			super.visitASTFunctionSubprogramNode(node);
		}
		// else pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node) {
		super.visitASTEndFunctionStmtNode(node);
		if (options.withAllLocals()) {
			context.pop();
		}
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		if (options.withAllLocals()) {
			IREntity entity = dico.getEntityByKey( mkKey(node) );

			context.push(entity);
			super.visitASTSubroutineSubprogramNode(node);
		}
		// else pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node) {
		super.visitASTEndSubroutineStmtNode(node);
		if (options.withAllLocals()) {
			context.pop();
		}
	}


	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			ASTToken tk = decl.getObjectName();
			IREntity entity = dico.addEntity( mkKey(tk), IRKind.VARIABLE, /*parent*/context.peek());
			entity.name( tk.getText());
			entity.data(IREntity.IS_STUB, false);
			entity.data( IREntity.DECLARED_PARAM, varIsDeclaredParameter( node));
			entity.data( IREntity.DECLARED_TYPENAME, node.getTypeSpec().getTypeName().getText());
			entity.addSourceAnchor( filename, node);

			traceEntityCreation(entity);
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
