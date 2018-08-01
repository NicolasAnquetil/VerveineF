package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTModuleNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.ScopingEntity;
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
	public void visitASTModuleNode(ASTModuleNode node) {
		Module mod = (Module) dico.getEntityByKey( firstDefinition(node.getNameToken()) );

		context.push(mod);
		super.visitASTModuleNode(node);
		context.pop();
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);	
		dico.addSourceAnchor(fmx, filename, node);
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
	}

}
