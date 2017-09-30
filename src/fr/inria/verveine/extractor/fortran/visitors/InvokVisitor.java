package fr.inria.verveine.extractor.fortran.visitors;

import java.util.List;

import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTCallStmtNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

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

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		caller = (Function) dico.getEntityByKey(node.getNameToken().resolveBinding().get(0));
		if (caller == null) {
			System.err.println("  Function definition not found: "+ node.getName());
		}
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		caller = (Function) dico.getEntityByKey(node.getNameToken().resolveBinding().get(0));
		if (caller == null) {
			System.err.println("  Procedure definition not found: "+ node.getName());
		}
	}

	@Override
	public void visitASTCallStmtNode(ASTCallStmtNode node) {
		Invocation fmx = null;
		Token tok = node.getSubroutineName();

		List<Definition> bindings = tok.resolveBinding();
		for (Definition bnd : bindings) {
			BehaviouralEntity invoked = (BehaviouralEntity) dico.getEntityByKey(bnd);
			if (invoked != null) {
				if (fmx == null) {
					fmx = dico.addFamixInvocation( /*sender*/caller,  invoked, /*receiver*/null, /*signature*/node.toString(),  /*prev*/null);
				}
				else {
					fmx.addCandidates(invoked);
				}
				if (fmx == null) {
					System.err.println("  could not create invocation: "+ node.getSubroutineName());
				}
			}
		}
	}

}
