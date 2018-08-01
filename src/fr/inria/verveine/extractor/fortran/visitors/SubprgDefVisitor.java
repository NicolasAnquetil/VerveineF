package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTDoConstructNode;
import org.eclipse.photran.internal.core.parser.ASTElseIfConstructNode;
import org.eclipse.photran.internal.core.parser.ASTElseIfStmtNode;
import org.eclipse.photran.internal.core.parser.ASTForallConstructNode;
import org.eclipse.photran.internal.core.parser.ASTFunctionSubprogramNode;
import org.eclipse.photran.internal.core.parser.ASTIfConstructNode;
import org.eclipse.photran.internal.core.parser.ASTIfStmtNode;
import org.eclipse.photran.internal.core.parser.ASTLoopControlNode;
import org.eclipse.photran.internal.core.parser.ASTMainProgramNode;
import org.eclipse.photran.internal.core.parser.ASTModuleNode;
import org.eclipse.photran.internal.core.parser.ASTSubroutineSubprogramNode;

import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Program;
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
	public void visitASTMainProgramNode(ASTMainProgramNode node) {
		
		Program fmx =  (Program) dico.getEntityByKey( firstDefinition(node.getProgramStmt().getProgramName().getProgramName()) );
		fmx.setCyclomaticComplexity( 1);

		context.push(fmx);
		super.visitASTMainProgramNode(node);
		context.pop();
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
		fmx.setCyclomaticComplexity( 1);
		
		context.push(fmx);
		super.visitASTFunctionSubprogramNode(node);
		context.pop();	}

	@Override
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {
		Function fmx = dico.ensureFamixFunction( firstDefinition(node.getNameToken()), node.getName(), /*sig*/node.getName(), /*parent*/(ScopingEntity)context.top());
		fmx.setIsStub(false);
		dico.addSourceAnchor(fmx, filename, node);
		fmx.setCyclomaticComplexity( 1);

		context.push(fmx);
		super.visitASTSubroutineSubprogramNode(node);
		context.pop();
	}

	
	// Handles cyclomatic complexity of subprograms
	
	@Override
	public void visitASTDoConstructNode(ASTDoConstructNode node) {
		// TODO Auto-generated method stub
		super.visitASTDoConstructNode(node);
	}

	@Override
	public void visitASTElseIfConstructNode(ASTElseIfConstructNode node) {
		super.visitASTElseIfConstructNode(node);
	}

	@Override
	public void visitASTElseIfStmtNode(ASTElseIfStmtNode node) {
		super.visitASTElseIfStmtNode(node);
	}

	@Override
	public void visitASTForallConstructNode(ASTForallConstructNode node) {
		super.visitASTForallConstructNode(node);
	}

	@Override
	public void visitASTIfConstructNode(ASTIfConstructNode node) {
		incrementSubprogCyclomatic();
		super.visitASTIfConstructNode(node);
	}

	@Override
	public void visitASTIfStmtNode(ASTIfStmtNode node) {
		super.visitASTIfStmtNode(node);
	}

	@Override
	public void visitASTLoopControlNode(ASTLoopControlNode node) {
		// TODO Auto-generated method stub
		super.visitASTLoopControlNode(node);
	}

	// UTILITIES

	private void incrementSubprogCyclomatic() {
		NamedEntity top = context.topBehaviouralEntity();
		if (top instanceof BehaviouralEntity) {
			BehaviouralEntity fmx = (BehaviouralEntity)top;

			fmx.setCyclomaticComplexity( (int)fmx.getCyclomaticComplexity() + 1);
		}
		else {
			System.out.println("oups");
		}
	}

	
}
