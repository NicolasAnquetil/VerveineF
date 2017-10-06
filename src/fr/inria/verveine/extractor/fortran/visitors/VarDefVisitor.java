package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTEntityDeclNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTProgramStmtNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTTypeDeclarationStmtNode;

import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class VarDefVisitor extends AbstractDispatcherVisitor {

	public VarDefVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Creating variables";
	}

	@Override
	public void visitASTProgramStmtNode(ASTProgramStmtNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		// pruning AST Visit (Avoids visiting local variable declarations)
	}

	/*
	 * Because of AST visit pruning, here we should only have global variables
	 */
	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		GlobalVariable fmx;
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			Token tk = decl.getObjectName().getObjectName();
			fmx= dico.ensureFamixGlobalVariable( firstDefinition(tk), tk.getText(), /*parent*/null);
			fmx.setIsStub(false);
			dico.addSourceAnchor(fmx, filename, node);
		}
	}

}