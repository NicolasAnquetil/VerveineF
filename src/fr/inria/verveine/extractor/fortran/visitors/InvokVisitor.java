package fr.inria.verveine.extractor.fortran.visitors;

import java.util.List;

import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTCallStmtNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTVarOrFnRefNode;

import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Invocation;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class InvokVisitor extends AbstractDispatcherVisitor {

	private Function caller;

	public InvokVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating subprograms calls";
	}
	
	// ================  V I S I T O R  =======================

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
		invocationFromNode( node.getSubroutineName());
	}

	@Override
	public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) {
		invocationFromNode( node.getName().getName());
	}

	
	// ================  U T I L I T I E S  =======================

	private Invocation invocationFromNode( Token nameTok) {
		Invocation fmx = null;
		List<Definition> bindings = nameTok.resolveBinding();
		for (Definition bnd : bindings) {
			BehaviouralEntity invoked = (BehaviouralEntity) dico.getEntityByKey(bnd);
			if (invoked != null) {
				if (fmx == null) {
					fmx = dico.addFamixInvocation( /*sender*/caller,  invoked, /*receiver*/null, /*signature*/nameTok.getText(),  /*prev*/null);
				}
				else {
					fmx.addCandidates(invoked);
				}
				if (fmx == null) {
					System.err.println("  could not create invocation: "+ nameTok.getText());
				}
			}
		}
		return fmx;
	}

	
}
