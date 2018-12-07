package fr.inria.verveine.extractor.fortran.ast;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IFortranParser;

public class ParserActionAST extends FortranParserActionNull {

	protected ParsingContext parsingCtxt = new ParsingContext();
	
	public ParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);

		parsingCtxt.valueStackPush( new ASTCompilationUnit(filename));
	}

	public ASTNode getAST() {
		return (ASTNode) parsingCtxt.valueStackTop();
	}

	@Override
	public void main_program__begin() {
		parsingCtxt.valueStackPush( new ASTMainProgramNode());
	}

	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTMainProgramNode mainPgm = (ASTMainProgramNode)parsingCtxt.valueStackTop();
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		
		pgmStmt.setLabel( asttk(label));
		pgmStmt.setASTField(ASTProgramStmtNode.TPROGRAM, asttk(programKeyword));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, asttk(eos));

		pgmStmt.setProgramName( asttk(id));
		mainPgm.setProgramStmt(pgmStmt);

		parsingCtxt.valueStackPush((IASTNode) mainPgm.getBody());
	}


	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode;
		ASTMainProgramNode mainPgm;
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setLabel(asttk(label));
		endPgmStmt.setEndToken(asttk(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, asttk(programKeyword));
		endPgmStmt.setEndName(asttk(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, asttk(eos));

		parsingCtxt.valueStackPop();  // program body 
		
		mainPgm = (ASTMainProgramNode)parsingCtxt.valueStackPop();
		mainPgm.setEndProgramStmt(endPgmStmt);
		
		parentNode = (ASTCompilationUnit) parsingCtxt.valueStackTop();
		parentNode.setProgramUnit(mainPgm);
	}


	@Override
	public void module_stmt__begin() {
		parsingCtxt.valueStackPush( new ASTModuleNode());
	}

	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleNode moduleNode = (ASTModuleNode)parsingCtxt.valueStackTop();
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();

		moduleStmt.setLabel(asttk(label));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, asttk(eos));

		moduleStmt.setModuleName(asttk(id));
		moduleNode.setModuleStmt(moduleStmt);
		
		parsingCtxt.valueStackPush((IASTNode) moduleNode.getModuleBody());
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTCompilationUnit parentNode;
		ASTModuleNode moduleNode;
		ASTEndModuleStmtNode endModule = new ASTEndModuleStmtNode();

		endModule.setLabel(asttk(label));
		endModule.setASTField(ASTEndModuleStmtNode.TEND, asttk(endKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TENDMODULE, asttk(moduleKeyword));
		endModule.setEndName(asttk(id));
		endModule.setASTField(ASTEndModuleStmtNode.TEOS, asttk(eos));

		parsingCtxt.valueStackPop(); // module content 

		moduleNode = (ASTModuleNode) parsingCtxt.valueStackPop();
		moduleNode.setEndModuleStmt(endModule);
		
		parentNode = (ASTCompilationUnit) parsingCtxt.valueStackTop();
		parentNode.setProgramUnit(moduleNode);
	}


	@Override
	public void function_stmt__begin() {
		parsingCtxt.valueStackPush( new ASTFunctionSubprogramNode());
	}

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionSubprogramNode fctNode = (ASTFunctionSubprogramNode) parsingCtxt.valueStackTop();
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		
		fctStmt.setLabel(asttk(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, asttk(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, asttk(eos));
		
		fctStmt.setFunctionName(asttk(name));
		fctNode.setFunctionStmt(fctStmt);
		
		parsingCtxt.valueStackPush((IASTNode) fctNode.getBody());
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

		parsingCtxt.valueStackPop();  // function body

		fctNode = (ASTFunctionSubprogramNode) parsingCtxt.valueStackPop();
		fctNode.setEndFunctionStmt(endFct);

		parentNode = (ASTModuleNode) parsingCtxt.valueStackTop(-1);
		parentNode.addModuleBody(fctNode);
	}


	@Override
	public void subroutine_stmt__begin() {
		parsingCtxt.valueStackPush( new ASTSubroutineSubprogramNode());
	}

	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineSubprogramNode pcdNode = (ASTSubroutineSubprogramNode) parsingCtxt.valueStackTop();
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		ASTSubroutineNameNode pcdName = new ASTSubroutineNameNode();
		
		pcdName.setSubroutineName(asttk(name));
		pcdStmt.setLabel(asttk(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, asttk(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, asttk(eos));
		
		pcdStmt.setSubroutineName(pcdName);
		pcdNode.setSubroutineStmt(pcdStmt);
		
		parsingCtxt.valueStackPush((IASTNode) pcdNode.getBody());
	}
	
	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTModuleNode parentNode;
		ASTSubroutineSubprogramNode pcdNode;

		ASTEndSubroutineStmtNode endPcd = new ASTEndSubroutineStmtNode();
		
		endPcd.setLabel(asttk(label));
		if ( ((FortranToken)keyword1).getTokenIndex() == FortranLexer.T_ENDSUBROUTINE) {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TENDSUBROUT, asttk(keyword1));
		}
		else {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TEND, asttk(keyword1));
		}
		endPcd.setASTField(ASTEndSubroutineStmtNode.TSUBROUT, asttk(keyword2));
		endPcd.setASTField(ASTEndSubroutineStmtNode.TEOS, asttk(eos));
		
		parsingCtxt.valueStackPop(); // subroutine body

		pcdNode = (ASTSubroutineSubprogramNode) parsingCtxt.valueStackPop();
		pcdNode.setEndSubroutineStmt(endPcd);
		
		parentNode = (ASTModuleNode) parsingCtxt.valueStackTop(-1);
		parentNode.addModuleBody(pcdNode);
	}

	

	@Override
	public void block_data_stmt(Token label, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
		parsingCtxt.valueStackPush((IASTNode) new ASTListNode<>());  // see specificationPart
	}


	@Override
	public void declaration_type_spec(Token declarationKeyword, int declarationTypeEnumValue) {
		
		ASTListNode<ASTNode> parentList = (ASTListNode<ASTNode>) parsingCtxt.valueStackTop();
		ASTTypeDeclarationStmtNode typeDecl = new ASTTypeDeclarationStmtNode();
		parentList.add(typeDecl);
		parsingCtxt.valueStackPush(typeDecl);
	}

	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		ASTListNode<ASTNode> listDeclarationConstruct;
		ASTTypeDeclarationStmtNode typeDecl;
		
		typeDecl = (ASTTypeDeclarationStmtNode) parsingCtxt.valueStackPop();
		typeDecl.setLabel(asttk(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));

		listDeclarationConstruct = (ASTListNode<ASTNode>)parsingCtxt.valueStackTop();
		listDeclarationConstruct.add( typeDecl);
	}


	@Override
	public void entity_decl_list__begin() {		
		//valueStackPush( (IASTNode) new ASTListNode<ASTEntityDeclNode>());
	}

	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		//IASTListNode<ASTEntityDeclNode> declList = (IASTListNode<ASTEntityDeclNode>) valueStackTop();
		ASTTypeDeclarationStmtNode typeDecl = (ASTTypeDeclarationStmtNode) parsingCtxt.valueStackTop();
		ASTEntityDeclNode entityDecl = new ASTEntityDeclNode();
		ASTObjectNameNode objName = new ASTObjectNameNode();
		
		objName.setObjectName(asttk(id));
		entityDecl.setObjectName(objName);
		typeDecl.getEntityDeclList().add( entityDecl);
	}

	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		ASTTypeDeclarationStmtNode typeDecl = (ASTTypeDeclarationStmtNode) parsingCtxt.valueStackTop();
		ASTAttrSpecNode attrSpec = new ASTAttrSpecNode();
		ASTAttrSpecSeqNode attrSpecSeq;
		
		if (attrKeyword == null) {
			return;
		}
		
		ASTToken tk = asttk(attrKeyword);
		
		switch (tk.getType()) {
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

	@Override
	public void call_stmt(Token label, Token callKeyword, Token eos, boolean hasActualArgSpecList) {
		ASTCallStmtNode call = new ASTCallStmtNode();
		
		call.setLabel( asttk(label));
		call.setASTField( ASTCallStmtNode.TCALL, asttk(callKeyword));
		call.setSubroutineName( asttk((Token) parsingCtxt.valueGet("part_ref.id")) );
		call.setASTField( ASTCallStmtNode.TEOS, asttk(eos));
		
		IASTNode parentBodyStmt = parsingCtxt.valueStackTop();
		assert( parentBodyStmt instanceof ASTListNode); 
		((ASTListNode)parentBodyStmt).add(call);
	}

	@Override
	public void designator_or_func_ref() {
		ASTVarOrFnRefNode invok = new ASTVarOrFnRefNode();
		
		invok.setName(asttk((Token) parsingCtxt.valueGet("part_ref.id")));

		IASTNode parentBodyStmt = parsingCtxt.valueStackTop();
		assert( parentBodyStmt instanceof ASTListNode); 
		((ASTListNode)parentBodyStmt).add(invok);
	}
	
	@Override
	public void part_ref(Token id, boolean hasSectionSubscriptList, boolean hasImageSelector) {
		parsingCtxt.valueSet("part_ref.id",id);
	}
	
	// UTILITIES ---
	

	protected ASTToken asttk(Token tok) {
		if (tok != null) {
			return new ASTToken( (FortranToken)tok);
		}
		else {
			return null;
		}
	}
	
}
