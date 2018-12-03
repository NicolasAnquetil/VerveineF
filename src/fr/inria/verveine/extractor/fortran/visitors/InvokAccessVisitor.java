package fr.inria.verveine.extractor.fortran.visitors;

import fr.inria.verveine.extractor.fortran.ast.ASTCallStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEndFunctionStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEndProgramStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEndSubroutineStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.ir.IRDictionary;
import fr.inria.verveine.extractor.fortran.ir.IREntity;
import fr.inria.verveine.extractor.fortran.ir.IRKind;

public class InvokAccessVisitor extends AbstractDispatcherVisitor {

	public InvokAccessVisitor(IRDictionary dico, String filename) {
		super(dico, filename);
	}

	@Override
	protected String msgTrace() {
		return "Creating variable accesses and subprograms calls";
	}
	
	// ================  V I S I T O R  =======================
	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		ASTToken tk = node.getProgramStmt().getProgramName().getProgramName();
		
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
		// actually, here we know it is an invocation and not an access
		
		IREntity call = new IREntity(context.peek(), IRKind.SUBPRGCALL);
		call.name(node.getSubroutineName().getText());
		dico.addAnonymousEntity(call);

		super.visitASTCallStmtNode(node);
	}

	/*@Override
	public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) {
		// actually, here we know it is an access and not an invocation
		invokOrAccessFromNode( node.getName().getName());

		super.visitASTVarOrFnRefNode(node);
	}

	@Override
	public void visitASTAssignmentStmtNode(ASTAssignmentStmtNode node) {
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
		ASTToken varI = node.getIndexVariable();
		if (varI != null) {
			Access acc = (Access) invokOrAccessFromNode(varI);
			if (acc != null) {
				acc.setIsWrite(true);
			}			
		}
	}*/

}
