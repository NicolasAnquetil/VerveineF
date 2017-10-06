package fr.inria.verveine.extractor.fortran.visitors;

import java.util.List;

import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTCallStmtNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTProgramStmtNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTVarOrFnRefNode;

import eu.synectique.verveine.core.gen.famix.Access;
import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Invocation;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Program;
import eu.synectique.verveine.core.gen.famix.StructuralEntity;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class InvokAccessVisitor extends AbstractDispatcherVisitor {

	private Function caller;

	public InvokAccessVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating subprograms calls";
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
		// actually, here we know it is an invocatin and not an access
		invokOrAccessFromNode( node.getSubroutineName());
	}

	@Override
	public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) {
		invokOrAccessFromNode( node.getName().getName());
	}

	
	// ================  U T I L I T I E S  =======================

	private void invokOrAccessFromNode( Token nameTok) {
		Invocation invok = null;
		Access acc = null;

		if ( caller==null ) {
			System.err.println("  "+nameTok.getText()+": no caller, giving up");
			return;
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
					// heuuu don' know what it is
					System.err.println("  "+nameTok.getText()+": varOrFnRef to unknow thing");
				}
			}
			else {
				System.err.println("  "+nameTok.getText()+": no entity for definition");
			}
		}
		if ( (acc==null) && (invok==null) ) {
			System.err.println("  "+nameTok.getText()+": no access/invocation created");
		}
	}

	
}
