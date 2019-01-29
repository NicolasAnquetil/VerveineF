package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.FortranLanguage;
import fr.inria.verveine.extractor.fortran.Options;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTAllocateStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTAssignmentStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCallStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTDataRefNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTDeallocateStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndFunctionStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndProgramStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndSubroutineStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;

public class InvokAccessVisitor extends AbstractDispatcherVisitor {

	public InvokAccessVisitor(IRDictionary dico, String filename, Options options) {
		super(dico, filename, options);
	}

	@Override
	protected String msgTrace() {
		return "Creating variable accesses and subprograms calls";
	}

	@Override
	public void visitASTCompilationUnit(ASTCompilationUnit node) {
		IREntity entity = dico.getEntityByKey(mkKey(node));
		context.push(entity);

		super.visitASTCompilationUnit(node);
	}

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
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		IREntity entity = dico.getEntityByKey( mkKey(node) );

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
		IREntity entity = dico.getEntityByKey( mkKey(node) );

		context.push(entity);
		super.visitASTSubroutineSubprogramNode(node);
	}

	@Override
	public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node) {
		super.visitASTEndSubroutineStmtNode(node);
		context.pop();
	}

	@Override
	public void visitASTCallStmtNode(ASTCallStmtNode node) {
		String name = node.getSubroutineName().getText();

		if ( FortranLanguage.isIntrinsicSubroutine(name) && (! options.withIntrinsics()))  {
			return;
		}

		IREntity call = dico.addAnonymousEntity(IRKind.SUBPRGCALL, context.peek());
		call.name(name);

		traceEntityCreation(call);
	}

	@Override
	public void visitASTAllocateStmtNode(ASTAllocateStmtNode node) {
		IREntity call = dico.addAnonymousEntity(IRKind.SUBPRGCALL, context.peek());
		call.name(IREntity.ALLOCATE_NAME);

		traceEntityCreation(call);

		context.push(call);
		super.visitASTAllocateStmtNode(node);
		context.pop();
	}


	@Override
	public void visitASTDeallocateStmtNode(ASTDeallocateStmtNode node) {
		IREntity call = dico.addAnonymousEntity(IRKind.SUBPRGCALL, context.peek());
		call.name(IREntity.DEALLOCATE_NAME);

		traceEntityCreation(call);

		context.push(call);
		super.visitASTDeallocateStmtNode(node);
		context.pop();
	}


	@Override
	public void visitASTDataRefNode(ASTDataRefNode node) {
		String name = node.getName().getText();

		if ( FortranLanguage.isIntrinsicFunction(name) && (! options.withIntrinsics()))  {
			return;
		}
		IREntity ref = dico.addAnonymousEntity(IRKind.NAMEREF, context.peek());
		ref.name(name);

		traceEntityCreation(ref);
	}

	@Override
	public void visitASTAssignmentStmtNode(ASTAssignmentStmtNode node) {
		// actually, here we know it is an access and not an invocation
		IREntity ref = dico.addAnonymousEntity(IRKind.VARREF, context.peek());
		ref.data(IREntity.ASSIGN_LHS, node.getLhsVariable().fortranNameToString());
		ref.data(IREntity.IS_WRITE, "true");

		traceEntityCreation(ref);

		node.getRhs().accept(this);
	}

	/*
	@Override
	public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node) {
		// actually, here we know it is an access and not an invocation
		ASTToken varI = node.getIndexVariable();
		if (varI != null) {
			Access acc = (Access) invokOrAccessFromNode(varI);
			if (acc != null) {
				acc.setIsWrite(true);
			}			
		}
	}
	 */

}
