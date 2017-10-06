package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTModuleNode;

import eu.synectique.verveine.core.gen.famix.Module;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class ScopeDefVisitor extends AbstractDispatcherVisitor {

	public ScopeDefVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "creating modules";
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		Token tk = node.getNameToken();
		Module fmx = dico.ensureFamixModule( firstDefinition(tk), node.getName(), /*owner*/null);
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);

		super.visitASTModuleNode(node);
	}

}