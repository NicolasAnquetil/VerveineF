package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTProgramStmtNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Program;
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
	public void visitASTProgramStmtNode(ASTProgramStmtNode node) {
		Token tk = node.getProgramName().getProgramName();
		
		Program fmx = dico.ensureFamixEntity( Program.class, firstDefinition(tk), tk.getText());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/null);
		fmx.setIsStub(false);	
		dico.addSourceAnchor(fmx, filename, node);
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/null);
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
	}

}
