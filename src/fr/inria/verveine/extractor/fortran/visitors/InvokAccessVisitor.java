package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCallStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndFunctionStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndProgramStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTEndSubroutineStmtNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.parser.ast.ASTVarOrFnRefNode;

public class InvokAccessVisitor extends AbstractDispatcherVisitor {

	public InvokAccessVisitor(IRDictionary dico, String filename, boolean allLocals) {
		super(dico, filename, allLocals);
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
		IREntity call = new IREntity(context.peek(), IRKind.SUBPRGCALL);
		call.name(node.getSubroutineName().getText());
		dico.addAnonymousEntity(call);

		super.visitASTCallStmtNode(node);
	}

	@Override
	public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) {
		IREntity ref = new IREntity(context.peek(), IRKind.NAMEREF);
		ref.name(node.getName().getText());
		dico.addAnonymousEntity(ref);

		super.visitASTVarOrFnRefNode(node);
	}
/*
	@Override
	public void visitASTAssignmentStmtNode(ASTAssignmentStmtNode node) {
		// actually, here we know it is an access and not an invocation
		ASTNameNode lhs = node.getLhsVariable();
		if (lhs != null) {
			Access acc = (Access) invokOrAccessFromNode(lhs.getName());
			if (acc != null) {
				acc.setIsWrite(true);
			}
		}

		super.visitASTAssignmentStmtNode(node);
	}

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
