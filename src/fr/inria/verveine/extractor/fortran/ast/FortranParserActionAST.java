package fr.inria.verveine.extractor.fortran.ast;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IActionEnums;
import fortran.ofp.parser.java.IFortranParser;

public class FortranParserActionAST extends FortranParserActionNull {

	protected ASTListNode<? extends ASTNode> valueStack;
	protected int valueStackTop;

	public FortranParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);

		valueStack = new ASTListNode<>();
		valueStackTop = -1;
		valueStackPush( new ASTCompilationUnit(filename));
	}


	protected void valueStackPush(IASTNode node) {
		// casting ASTListNode to avoid strange type checking error
		((ASTListNode)valueStack).add( ++valueStackTop, node);
	}

	protected IASTNode valueStackTop() {
		return valueStack.get(valueStackTop);
	}

	protected IASTNode valueStackPop() {
		return valueStack.get(valueStackTop--);
	}

	
	public ASTNode getAST() {
		return (ASTNode) valueStackTop();
	}
	
	
	@Override
	public void main_program__begin() {
		valueStackPush( new ASTMainProgramNode());
	}

	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTMainProgramNode mainPgm = (ASTMainProgramNode)valueStackTop();
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		ASTProgramNameNode pgmName = new ASTProgramNameNode();
		
		pgmName.setProgramName( asttk(id));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, asttk(eos));
		pgmStmt.setLabel( asttk(label));

		pgmStmt.setProgramName( pgmName);
		mainPgm.setProgramStmt(pgmStmt);

		valueStackPush((IASTNode) mainPgm.getBody());  // see specificationPart
	}


	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode;
		ASTMainProgramNode mainPgm;
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setEndToken(asttk(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, asttk(programKeyword));
		endPgmStmt.setEndName(asttk(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, asttk(eos));
		endPgmStmt.setLabel(asttk(label));

		mainPgm = (ASTMainProgramNode)valueStackPop();
		mainPgm.setEndProgramStmt(endPgmStmt);

		parentNode = (ASTCompilationUnit) valueStackTop();
		parentNode.setProgramUnit(mainPgm);
	}


	@Override
	public void module_stmt__begin() {
		valueStackPush( new ASTModuleNode());
	}

	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleNode moduleNode = (ASTModuleNode)valueStackTop();
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();
		ASTModuleNameNode moduleName = new ASTModuleNameNode();

		moduleName.setModuleName(asttk(id));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, asttk(eos));
		moduleStmt.setLabel(asttk(label));

		moduleStmt.setModuleName(moduleName);
		moduleNode.setModuleStmt(moduleStmt);
		
		valueStackPush((IASTNode) moduleNode.getModuleBody());  // see specificationPart
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode;
		ASTModuleNode moduleNode;
		ASTEndModuleStmtNode endModule = new ASTEndModuleStmtNode();

		endModule.setEndName(asttk(id));
		endModule.setASTField(ASTEndModuleStmtNode.TEND, asttk(endKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TENDMODULE, asttk(moduleKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TEOS, asttk(eos));
		endModule.setLabel(asttk(label));

		moduleNode = (ASTModuleNode) valueStackPop();
		moduleNode.setEndModuleStmt(endModule);
		
		parentNode = (ASTCompilationUnit) valueStackTop();
		parentNode.setProgramUnit(moduleNode);
	}


	@Override
	public void function_stmt__begin() {
		valueStackPush( new ASTFunctionSubprogramNode());
	}

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionSubprogramNode fctNode = (ASTFunctionSubprogramNode) valueStackTop();
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		ASTFunctionNameNode fctName = new ASTFunctionNameNode();
		
		fctName.setFunctionName(asttk(name));
		fctStmt.setLabel(asttk(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, asttk(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, asttk(eos));
		
		fctStmt.setFunctionName(fctName);
		fctNode.setFunctionStmt(fctStmt);
		
		valueStackPush((ASTNode) fctNode.getBody());  // see specificationPart
	}

	@Override
	public void end_function_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTModuleNode parentNode;
		ASTFunctionSubprogramNode fctNode;

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

		fctNode = (ASTFunctionSubprogramNode) valueStackPop();
		fctNode.setEndFunctionStmt(endFct);

		parentNode = (ASTModuleNode) valueStackTop();
		parentNode.addModuleBody(fctNode);
	}


	@Override
	public void subroutine_stmt__begin() {
		valueStackPush( new ASTSubroutineSubprogramNode());
	}

	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineSubprogramNode pcdNode = (ASTSubroutineSubprogramNode) valueStackTop();
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		ASTSubroutineNameNode pcdName = new ASTSubroutineNameNode();
		
		pcdName.setSubroutineName(asttk(name));
		pcdStmt.setLabel(asttk(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, asttk(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, asttk(eos));
		
		pcdStmt.setSubroutineName(pcdName);
		pcdNode.setSubroutineStmt(pcdStmt);
		
		valueStackPush((IASTNode) pcdNode.getBody());  // see specificationPart
	}


	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTModuleNode parentNode;
		ASTSubroutineSubprogramNode pcdNode;

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

		pcdNode = (ASTSubroutineSubprogramNode) valueStackPop();
		pcdNode.setEndSubroutineStmt(endPcd);

		parentNode = (ASTModuleNode) valueStackTop();
		parentNode.addModuleBody(pcdNode);
	}

	

	@Override
	public void block_data_stmt(Token label, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
		valueStackPush((IASTNode) new ASTListNode<>());  // see specificationPart
	}

	@Override
	public void specification_part(int numUseStmts, int numImportStmts, int numImplStmts, int numDeclConstructs) {
	    // pops ASTListNode that collected the specificationParts
		// this list is not lost as it was already within a parentNode
		valueStackPop();
	}



	@Override
	public void type_spec() {
		// TODO Auto-generated method stub
		super.type_spec();
	}


	@Override
	public void declaration_type_spec(Token arg0, int arg1) {
		int i = IActionEnums.DeclarationTypeSpec_INTRINSIC;// used as a marker of the beginning of type_declaration_stmt (see below)
		ASTListNode<ASTNode> parentList = (ASTListNode<ASTNode>) valueStackTop();
		ASTTypeDeclarationStmtNode typeDecl = new ASTTypeDeclarationStmtNode();
		parentList.add(typeDecl);
		valueStackPush(typeDecl);
	}

	@Override
	public void kind_param(Token arg0) {
		// TODO Auto-generated method stub
		super.kind_param(arg0);
	}


	@Override
	public void real_literal_constant(Token arg0, Token arg1) {
		// TODO Auto-generated method stub
		super.real_literal_constant(arg0, arg1);
	}


	@Override
	public void part_ref(Token arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		super.part_ref(arg0, arg1, arg2);
	}


	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		ASTListNode<ASTNode> listDeclarationConstruct;
		ASTTypeDeclarationStmtNode typeDecl;
		
		typeDecl = (ASTTypeDeclarationStmtNode) valueStackPop();
		typeDecl.setLabel(asttk(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));

		listDeclarationConstruct = (ASTListNode<ASTNode>)valueStackTop();
		listDeclarationConstruct.add( typeDecl);
	}

	public void allocate_stmt(Token label, Token allocateKeyword, Token eos, boolean hasTypeSpec, boolean hasAllocOptList) {
		ASTAllocateStmtNode allocate;
		
		allocate = (ASTAllocateStmtNode) valueStackPop();
		allocate.setLabel(asttk(label));
		allocate.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));
	}

	@Override
	public void expr() {
		// TODO Auto-generated method stub
		super.expr();
	}


	@Override
	public void level_5_expr(int arg0) {
		// TODO Auto-generated method stub
		super.level_5_expr(arg0);
	}


	@Override
	public void level_5_expr__defined_binary_op(Token arg0) {
		// TODO Auto-generated method stub
		super.level_5_expr__defined_binary_op(arg0);
	}


	@Override
	public void entity_decl_list__begin() {		
		//valueStackPush( (IASTNode) new ASTListNode<ASTEntityDeclNode>());
	}

	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		//IASTListNode<ASTEntityDeclNode> declList = (IASTListNode<ASTEntityDeclNode>) valueStackTop();
		ASTTypeDeclarationStmtNode typeDecl = (ASTTypeDeclarationStmtNode) valueStackTop();
		ASTEntityDeclNode entityDecl = new ASTEntityDeclNode();
		ASTObjectNameNode objName = new ASTObjectNameNode();
		
		objName.setObjectName(asttk(id));
		entityDecl.setObjectName(objName);
		typeDecl.getEntityDeclList().add( entityDecl);
	}

	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		ASTTypeDeclarationStmtNode typeDecl = (ASTTypeDeclarationStmtNode) valueStackTop();
		ASTAttrSpecNode attrSpec = new ASTAttrSpecNode();
		ASTAttrSpecSeqNode attrSpecSeq;
		
		if (attrKeyword == null) {
			return;
		}
		
		ASTToken tk = asttk(attrKeyword);
		
		switch (tk.getTokenIndex()) {
		case FortranLexer.T_ALLOCATABLE: attrSpec.setIsAllocatable(tk); break;
		case FortranLexer.T_ASYNCHRONOUS: attrSpec.setIsAsync(tk); break;
		case FortranLexer.T_CODIMENSION: attrSpec.setIsCodimension(tk); break;
		case FortranLexer.T_CONTIGUOUS: attrSpec.setIsContiguous(tk); break;
		case FortranLexer.T_DIMENSION: attrSpec.setIsDimension(tk); break;
		case FortranLexer.T_EXTERNAL: attrSpec.setIsExternal(tk); break;
		case FortranLexer.T_INTENT: attrSpec.setIsIntent(tk); break;
		case FortranLexer.T_INTRINSIC: attrSpec.setIsIntrinsic(tk); break;
		case FortranLexer.T_OPTIONAL: attrSpec.setIsOptional(tk); break;
		case FortranLexer.T_PARAMETER: attrSpec.setIsParameter(tk); break;
		case FortranLexer.T_POINTER: attrSpec.setIsPointer(tk); break;
		case FortranLexer.T_PROTECTED: attrSpec.setIsProtected(tk); break;
		case FortranLexer.T_SAVE: attrSpec.setIsSave(tk); break;
		case FortranLexer.T_TARGET: attrSpec.setIsTarget(tk); break;
		case FortranLexer.T_VALUE: attrSpec.setIsValue(tk); break;
		case FortranLexer.T_VOLATILE: attrSpec.setIsVolatile(tk); break;
		//case FortranLexer.T_KIND: attrSpec.; break;
		//case FortranLexer.T_LEN: attrSpec.; break;
		default: tk = null;
		}
		
		if (tk == null) {
			return;
		}

		attrSpecSeq  = new ASTAttrSpecSeqNode();
		attrSpecSeq.setAttrSpec(attrSpec);
		typeDecl.getAttrSpecSeq().add( attrSpecSeq);
	}
	
	
	// UTILITIES ---
	
	@Override
	public void intrinsic_type_spec(Token arg0, Token arg1, int arg2, boolean arg3) {
		// TODO Auto-generated method stub 
		super.intrinsic_type_spec(arg0, arg1, arg2, arg3);
	}


	@Override
	public void kind_selector(Token arg0, Token arg1, boolean arg2) {
		// TODO Auto-generated method stub
		super.kind_selector(arg0, arg1, arg2);
	}


	@Override
	public void level_1_expr(Token arg0) {
		// TODO Auto-generated method stub
		super.level_1_expr(arg0);
	}


	@Override
	public void level_2_expr(int arg0) {
		// TODO Auto-generated method stub
		super.level_2_expr(arg0);
	}


	@Override
	public void level_3_expr(Token arg0) {
		// TODO Auto-generated method stub
		super.level_3_expr(arg0);
	}


	protected ASTToken asttk(Token tok) {
		if (tok != null) {
			return new ASTToken( (FortranToken)tok);
		}
		else {
			return null;
		}
	}
	
}
