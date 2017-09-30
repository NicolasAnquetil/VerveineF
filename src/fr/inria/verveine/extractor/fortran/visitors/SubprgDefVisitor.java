package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

import eu.synectique.verveine.core.gen.famix.Function;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class SubprgDefVisitor extends AbstractDispatcherVisitor {

	public SubprgDefVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating subprograms";
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction(node.getNameToken().resolveBinding().get(0), node.getName(), /*sig*/node.getName(), /*parent*/null);
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node.findFirstToken().getLine(), node.findLastToken().getLine());
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction(node.getNameToken().resolveBinding().get(0), node.getName(), /*sig*/node.getName(), /*parent*/null);
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node.findFirstToken().getLine(), node.findLastToken().getLine());
	}

}
