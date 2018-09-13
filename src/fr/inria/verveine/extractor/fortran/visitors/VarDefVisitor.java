package fr.inria.verveine.extractor.fortran.visitors;

import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.ScopingEntity;
import fortran.ofp.parser.java.FortranToken;
import fr.inria.verveine.extractor.fortran.FDictionary;
import fr.inria.verveine.extractor.fortran.ast.ASTAttrSpecSeqNode;
import fr.inria.verveine.extractor.fortran.ast.ASTEntityDeclNode;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.ASTProgramStmtNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTTypeDeclarationStmtNode;

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
	public void visitASTModuleNode(ASTModuleNode node) {
		Module mod = (Module) dico.getEntityByKey( firstDefinition(node.getNameToken()) );

		context.push(mod);
		super.visitASTModuleNode(node);
		context.pop();
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
			FortranToken tk = decl.getObjectName().getObjectName();
			fmx= dico.ensureFamixGlobalVariable( firstDefinition(tk), tk.getText(), /*parent*/(ScopingEntity)context.top());
			fmx.setIsStub(false);
			fmx.setIsDeclaredFortranParameter( varIsDeclaredParameter( node));
			dico.addSourceAnchor(fmx, filename, node);
		}
	}

	private boolean varIsDeclaredParameter(ASTTypeDeclarationStmtNode node) {
		if (node.getAttrSpecSeq() == null) {
			return false;
		}

		for ( ASTAttrSpecSeqNode spec : node.getAttrSpecSeq() ) {
			if ( (spec.getAttrSpec() != null) && (spec.getAttrSpec().isParameter()) ) {
				return true;
			}
		}
		return false;
	}

}
