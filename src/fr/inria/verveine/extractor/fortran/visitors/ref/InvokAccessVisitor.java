package fr.inria.verveine.extractor.fortran.visitors.ref;

import java.util.List;

import eu.synectique.verveine.core.gen.famix.Access;
import eu.synectique.verveine.core.gen.famix.Association;
import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Invocation;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Program;
import eu.synectique.verveine.core.gen.famix.StructuralEntity;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTCallStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProgramStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProperLoopConstructNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTToken;
import fr.inria.verveine.extractor.fortran.visitors.AbstractDispatcherVisitor;

@SuppressWarnings("restriction")
public class InvokAccessVisitor extends AbstractDispatcherVisitor {

	private Function caller;

	public InvokAccessVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating variable accesses and subprograms calls";
	}
	
	// ================  V I S I T O R  =======================

	@Override
	public void visitASTProgramStmtNode(ASTProgramStmtNode node) {
		caller = (Program) dico.getEntityByKey( firstDefinition(node.getProgramName().getProgramName()) );
		if (caller == null) {
			System.err.println("  Program definition not found: "+ node.getProgramName().getProgramName());
		}
		super.visitASTProgramStmtNode(node);
	}

	

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		caller = (Function) dico.getEntityByKey( firstDefinition(node.getNameToken()) );
		if (caller == null) {
			System.err.println("  Function definition not found: "+ node.getName());
		}
		super.visitASTFunctionSubprogramNode(node);
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		caller = (Function) dico.getEntityByKey( firstDefinition(node.getNameToken()) );
		if (caller == null) {
			System.err.println("  Procedure definition not found: "+ node.getName());
		}
		super.visitASTSubroutineSubprogramNode(node);
	}

	@Override
	public void visitASTCallStmtNode(ASTCallStmtNode node) {
		// actually, here we know it is an invocation and not an access
		invokOrAccessFromNode( node.getSubroutineName());

		super.visitASTCallStmtNode(node);
	}

	@Override
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
	}

	// ================  U T I L I T I E S  =======================

	private Association invokOrAccessFromNode( ASTToken nameTok) {
		Invocation invok = null;
		Access acc = null;

		if ( caller==null ) {
			//System.err.println("  "+nameTok.getText()+": no caller, giving up");
			return null;
		}

		List<Definition> bindings = nameTok.resolveBinding();

		for (Definition bnd : bindings) {
			NamedEntity target = dico.getEntityByKey(bnd);
			if (target != null) {
				if (target instanceof BehaviouralEntity) {
					if (invok == null) {
						invok = dico.addFamixInvocation( /*sender*/caller,  (BehaviouralEntity)target, /*receiver*/null, /*signature*/nameTok.getText(),  /*prev*/null);
					}
					else {
						invok.addCandidates((BehaviouralEntity) target);
					}
				}
				else if (target instanceof StructuralEntity) {
					// what if we have candidate function and variables ...
					acc = dico.addFamixAccess(/*accessor*/caller, (StructuralEntity)target, /*isWrite*/false, /*prev*/null);					
				}
				else {
					// heuuu don't know what it is
					//System.err.println("  "+nameTok.getText()+": varOrFnRef to unknow thing");
				}
			}
			else {
				//System.err.println("  "+nameTok.getText()+": no entity for definition");
			}
		}

		if (acc != null) {
			return acc;
		}
		else if (invok != null) {
			return invok;
		}
		else {
			// maybe a local variable (not created)?
			//System.err.println("  "+nameTok.getText()+": no access/invocation created");
			return null;
		}
	}

	
}
