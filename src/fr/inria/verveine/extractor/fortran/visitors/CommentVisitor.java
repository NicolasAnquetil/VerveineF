package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTEntityDeclNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTMainProgramNode;
import org.eclipse.photran.internal.core.parser.ASTModuleNode;
import org.eclipse.photran.internal.core.parser.ASTNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTTypeDeclarationStmtNode;

import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import eu.synectique.verveine.core.gen.famix.IndexedFileAnchor;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.Program;
import eu.synectique.verveine.core.gen.famix.SourcedEntity;
import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

@SuppressWarnings("restriction")
public class CommentVisitor extends AbstractDispatcherVisitor {

	public CommentVisitor(FDictionary dico) {
		super(dico);
	}

	@Override
	protected String msgTrace() {
		return "Adding comments to entities";
	}

	@Override
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		Program fmx =  (Program) dico.getEntityByKey( firstDefinition(node.getProgramStmt().getProgramName().getProgramName()) );
		createCommentIfNonBlank(node, fmx);

		context.push(fmx);
		super.visitASTMainProgramNode(node);
		context.pop();
	}

	@Override
	public void visitASTModuleNode(ASTModuleNode node) {
		Module fmx = (Module) dico.getEntityByKey( firstDefinition(node.getNameToken()) );
		createCommentIfNonBlank(node, fmx);

		context.push(fmx);
		super.visitASTModuleNode(node);
		context.pop();
	}

	@Override
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {
		Function fmx = (Function) dico.getEntityByKey( firstDefinition(node.getNameToken()));
		createCommentIfNonBlank(node, fmx);
		
		/*context.push(fmx);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();*/
	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = (Function) dico.getEntityByKey( firstDefinition(node.getNameToken()));
		createCommentIfNonBlank(node, fmx);

		/*context.push(fmx);
		super.visitASTSubroutineSubprogramNode(node);
		context.pop();*/
	}

	@Override
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {
		GlobalVariable fmx;
		for (ASTEntityDeclNode decl : node.getEntityDeclList()) {
			// Because of AST visit pruning, here we should only have global variables
			Token tk = decl.getObjectName().getObjectName();
			fmx= (GlobalVariable) dico.getEntityByKey( firstDefinition(tk));
			createCommentIfNonBlank(node, fmx);
		}
	}

	
	// UTILITIES

	/**
	 * Search for a comment  preceding <code>node<code>. If found, associates the comment with the <code>fmx</code> entity. <br>
	 * 
	 * TODO Deal with #ifdef / #endif that are also "white"
	 */
	private void createCommentIfNonBlank(ASTNode node, SourcedEntity fmx) {
		String cmt = node.findFirstToken().getWhiteBefore();
		int start = cmt.indexOf('!');
		if (start >= 0) {
			dico.createFamixComment(cmt, fmx);
			if (fmx.getSourceAnchor() != null) {
				IndexedFileAnchor fmxAnchor = (IndexedFileAnchor) fmx.getSourceAnchor();
				
				fmxAnchor.setStartPos( (int)fmxAnchor.getStartPos() - cmt.length()); 
			}
		}
	}

	
}
