package fr.inria.verveine.extractor.fortran.parser;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IFortranParser;
import fr.inria.verveine.extractor.fortran.ast.ASTCompilationUnit;
import fr.inria.verveine.extractor.fortran.ast.ASTFunctionSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTMainProgramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTModuleNode;
import fr.inria.verveine.extractor.fortran.ast.AbstractASTNode;
import fr.inria.verveine.extractor.fortran.ast.AbstractASTSubprogramNode;
import fr.inria.verveine.extractor.fortran.ast.ASTSubroutineSubprogramNode;

public class FortranParserActionAST extends FortranParserActionNull {

	protected AbstractASTNode currentNode;


	public FortranParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);
		currentNode = new ASTCompilationUnit(filename);
	}


	public AbstractASTNode getAST() {
		return currentNode;
	}

	
	@Override
	public void main_program__begin() {
		currentNode = new ASTMainProgramNode(currentNode);
	}

	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		((ASTMainProgramNode)currentNode).setName(id);
		if (label != null) {
			currentNode.setFirstToken( (FortranToken)label);
		}
		else {
			currentNode.setFirstToken((FortranToken)programKeyword);
		}
	}

	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode = (ASTCompilationUnit) currentNode.getParentNode();
		currentNode.setLastToken((FortranToken)eos);
		parentNode.addDefinition(currentNode);
		currentNode = parentNode;
	}


	@Override
	public void module_stmt__begin() {
		currentNode = new ASTModuleNode(currentNode);
	}

	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		((ASTModuleNode)currentNode).setName(id);
		if (label != null) {
			currentNode.setFirstToken( (FortranToken)label);
		}
		else {
			currentNode.setFirstToken((FortranToken)moduleKeyword);
		}
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode = (ASTCompilationUnit) currentNode.getParentNode();
		currentNode.setLastToken((FortranToken)eos);
		parentNode.addDefinition(currentNode);
		currentNode = parentNode;
	}


	@Override
	public void function_stmt__begin() {
		currentNode = new ASTFunctionSubprogramNode(currentNode);
	}

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList,
			boolean hasSuffix) {
		((ASTFunctionSubprogramNode)currentNode).setName(name);
		if (label != null) {
			currentNode.setFirstToken( (FortranToken)label);
		}
		else {
			currentNode.setFirstToken((FortranToken)keyword);
		}
	}

	@Override
	public void end_function_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTModuleNode parentNode = (ASTModuleNode) currentNode.getParentNode();
		currentNode.setLastToken((FortranToken)eos);
		parentNode.addDeclaration((AbstractASTSubprogramNode) currentNode);
		currentNode = parentNode;
	}


	@Override
	public void subroutine_stmt__begin() {
		currentNode = new ASTSubroutineSubprogramNode(currentNode);
	}

	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix,
			boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		((ASTSubroutineSubprogramNode)currentNode).setName(name);
		if (label != null) {
			currentNode.setFirstToken( (FortranToken)label);
		}
		else {
			currentNode.setFirstToken((FortranToken)keyword);
		}
	}

	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTModuleNode parentNode = (ASTModuleNode) currentNode.getParentNode();
		currentNode.setLastToken((FortranToken)eos);
		parentNode.addDeclaration((AbstractASTSubprogramNode) currentNode);
		currentNode = parentNode;
	}

}
