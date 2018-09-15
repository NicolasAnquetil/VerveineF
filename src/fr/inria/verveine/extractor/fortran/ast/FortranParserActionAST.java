package fr.inria.verveine.extractor.fortran.ast;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IFortranParser;

public class FortranParserActionAST extends FortranParserActionNull {

	protected ASTNode currentNode;


	public FortranParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);
		currentNode = new ASTCompilationUnit(filename);
	}


	public ASTNode getAST() {
		return currentNode;
	}

	
	@Override
	public void main_program__begin() {
		ASTCompilationUnit parentNode = (ASTCompilationUnit) currentNode;
		currentNode = new ASTMainProgramNode();
		parentNode.setProgramUnit(currentNode);
	}

	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTMainProgramNode mainPgm = (ASTMainProgramNode)currentNode;
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		ASTProgramNameNode pgmName = new ASTProgramNameNode();
		
		pgmName.setProgramName( asttk(id));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, asttk(eos));
		pgmStmt.setLabel( asttk(label));

		pgmStmt.setProgramName( pgmName);
		mainPgm.setProgramStmt(pgmStmt);
	}


	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTMainProgramNode mainPgm = (ASTMainProgramNode)currentNode;
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setEndToken(asttk(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, asttk(programKeyword));
		endPgmStmt.setEndName(asttk(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, asttk(eos));
		endPgmStmt.setLabel(asttk(label));

		mainPgm.setEndProgramStmt(endPgmStmt);

		currentNode = (ASTNode) mainPgm.getParent();
	}


	@Override
	public void module_stmt__begin() {
		ASTCompilationUnit parentNode = (ASTCompilationUnit) currentNode;
		currentNode = new ASTModuleNode();
		parentNode.setProgramUnit(currentNode);
	}

	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleNode moduleNode = (ASTModuleNode)currentNode;
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();
		ASTModuleNameNode moduleName = new ASTModuleNameNode();

		moduleName.setModuleName(asttk(id));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, asttk(eos));
		moduleStmt.setLabel(asttk(label));

		moduleStmt.setModuleName(moduleName);
		moduleNode.setModuleStmt(moduleStmt);
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTModuleNode moduleNode = (ASTModuleNode)currentNode;
		ASTEndModuleStmtNode endModule = new ASTEndModuleStmtNode();

		endModule.setEndName(asttk(id));
		endModule.setASTField(ASTEndModuleStmtNode.TEND, asttk(endKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TENDMODULE, asttk(moduleKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TEOS, asttk(eos));
		endModule.setLabel(asttk(label));

		moduleNode.setEndModuleStmt(endModule);

		currentNode = (ASTNode) moduleNode.getParent();
	}


	@Override
	public void function_stmt__begin() {
		ASTModuleNode parentNode = (ASTModuleNode) currentNode;
		currentNode = new ASTFunctionSubprogramNode();
		parentNode.addModuleBody((ASTFunctionSubprogramNode) currentNode);
	}

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionSubprogramNode fctNode = (ASTFunctionSubprogramNode) currentNode;
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		ASTFunctionNameNode fctName = new ASTFunctionNameNode();
		
		fctName.setFunctionName(asttk(name));
		fctStmt.setLabel(asttk(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, asttk(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, asttk(eos));
		
		fctStmt.setFunctionName(fctName);
		fctNode.setFunctionStmt(fctStmt);
	}

	@Override
	public void end_function_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTFunctionSubprogramNode fctNode = (ASTFunctionSubprogramNode) currentNode;
		ASTEndFunctionStmtNode endFct = new ASTEndFunctionStmtNode();

		endFct.setLabel(asttk(label));
		if ( ((FortranToken)keyword1).getTokenIndex() == FortranLexer.T_ENDFUNCTION) {
			endFct.setASTField(ASTEndFunctionStmtNode.TENDFUNCTION, asttk(keyword1));
		}
		else {
			endFct.setASTField(ASTEndFunctionStmtNode.TEND, asttk(keyword1));
		}
		endFct.setASTField(ASTEndFunctionStmtNode.TFUNCTION, asttk(keyword2));
		endFct.setASTField(ASTEndFunctionStmtNode.TEOS, asttk(eos));

		fctNode.setEndFunctionStmt(endFct);

		currentNode = (ASTNode) fctNode.getParent();
	}


	@Override
	public void subroutine_stmt__begin() {
		ASTModuleNode parentNode = (ASTModuleNode) currentNode;
		currentNode = new ASTSubroutineSubprogramNode();
		parentNode.addModuleBody((ASTSubroutineSubprogramNode) currentNode);
	}

	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineSubprogramNode pcdNode = (ASTSubroutineSubprogramNode) currentNode;
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		ASTSubroutineNameNode pcdName = new ASTSubroutineNameNode();
		
		pcdName.setSubroutineName(asttk(name));
		pcdStmt.setLabel(asttk(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, asttk(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, asttk(eos));
		
		pcdStmt.setSubroutineName(pcdName);
		pcdNode.setSubroutineStmt(pcdStmt);
	}

	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTSubroutineSubprogramNode pcdNode = (ASTSubroutineSubprogramNode) currentNode;
		ASTEndSubroutineStmtNode endPcd = new ASTEndSubroutineStmtNode();

		endPcd.setLabel(asttk(label));
		if ( ((FortranToken)keyword1).getTokenIndex() == FortranLexer.T_ENDFUNCTION) {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TENDSUBROUT, asttk(keyword1));
		}
		else {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TEND, asttk(keyword1));
		}
		endPcd.setASTField(ASTEndSubroutineStmtNode.TSUBROUT, asttk(keyword2));
		endPcd.setASTField(ASTEndSubroutineStmtNode.TEOS, asttk(eos));

		pcdNode.setEndSubroutineStmt(endPcd);

		currentNode = (ASTNode) pcdNode.getParent();
	}





/*
	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		((ASTTypeDeclarationStmtNode)currentNode).addAttrSpec( new ASTAttrSpecSeqNode(currentNode, attrKeyword));
	}
	
	/* beginning of declaration_type_spec
	 * /
	@Override
	public void declaration_type_spec(Token udtKeyword, int type) {
		currentNode = new ASTTypeDeclarationStmtNode(currentNode);
	}

	/* end of declaration_type_spec
	 * /
	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		AbstractASTSubprogramNode parentNode = (AbstractASTSubprogramNode) currentNode.getParent();
		currentNode.setLastToken((FortranToken)eos);
		parentNode.addDeclaration((ASTTypeDeclarationStmtNode) currentNode);
		currentNode = parentNode;
	}

	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		((ASTTypeDeclarationStmtNode)currentNode).addEntityDecl( new ASTEntityDeclNode(currentNode, (FortranToken) id));
	}
*/
	
	protected ASTToken asttk(Token tok) {
		if (tok != null) {
			return new ASTToken( (FortranToken)tok);
		}
		else {
			return null;
		}
	}

}
