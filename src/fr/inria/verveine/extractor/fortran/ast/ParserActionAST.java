package fr.inria.verveine.extractor.fortran.ast;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IActionEnums;
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
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		
		pgmStmt.setLabel( asttk(label));
		pgmStmt.setASTField(ASTProgramStmtNode.TPROGRAM, asttk(programKeyword));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, asttk(eos));
		pgmStmt.setProgramName( asttk(id));

		parsingCtxt.valueStackPush(pgmStmt);
	}

	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setLabel(asttk(label));
		endPgmStmt.setEndToken(asttk(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, asttk(programKeyword));
		endPgmStmt.setEndName(asttk(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, asttk(eos));

		parsingCtxt.valueStackPush(endPgmStmt);
	}

	@Override
	public void main_program(boolean hasProgramStmt, boolean hasExecutionPart, boolean hasInternalSubprogramPart) {
		ASTMainProgramNode mainPgm = new ASTMainProgramNode();

		mainPgm.setEndProgramStmt((ASTEndProgramStmtNode) parsingCtxt.valueStackPop());
		if (hasInternalSubprogramPart) {
			//mainPgm.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExecutionPart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
		}
		mainPgm.setBody(specifications);
		if (hasProgramStmt) {
			mainPgm.setProgramStmt((ASTProgramStmtNode) parsingCtxt.valueStackPop());
		}

		// programs are top level entities
		// put it in the CompilatinUnit node
		ASTCompilationUnit parentNode = (ASTCompilationUnit) parsingCtxt.valueStackTop();
		parentNode.setProgramUnit(mainPgm);
	}


	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();

		moduleStmt.setLabel(asttk(label));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, asttk(eos));

		moduleStmt.setModuleName(asttk(id));
		
		parsingCtxt.valueStackPush(moduleStmt);
	}

	@Override
	public void module_subprogram_part(int count) {
		if (count > 0) {
			// we skip count elements to fetch the count-1 element on top of the valueStack
			// this must be a IASTListNode<IModuleBodyConstruct> pushed by specification_part
			// we add to it all (count) module_subprograms
			IASTListNode<IModuleBodyConstruct> moduleBody = (IASTListNode<IModuleBodyConstruct>) parsingCtxt.valueStackTop(-1 * (count+1));
			for (int i=0; i<count; i++) {
				moduleBody.add((IModuleBodyConstruct) parsingCtxt.valueStackPop());
			}
		}
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTEndModuleStmtNode endModule = new ASTEndModuleStmtNode();

		endModule.setLabel(asttk(label));
		endModule.setASTField(ASTEndModuleStmtNode.TEND, asttk(endKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TENDMODULE, asttk(moduleKeyword));
		endModule.setEndName(asttk(id));
		endModule.setASTField(ASTEndModuleStmtNode.TEOS, asttk(eos));

		parsingCtxt.valueStackPush(endModule);
	}

	@Override
	public void module() {
		ASTModuleNode moduleNode = new ASTModuleNode();
		
		moduleNode.setEndModuleStmt((ASTEndModuleStmtNode) parsingCtxt.valueStackPop());
		moduleNode.setModuleBody((IASTListNode<IModuleBodyConstruct>) parsingCtxt.valueStackPop());
		moduleNode.setModuleStmt((ASTModuleStmtNode) parsingCtxt.valueStackPop());
		
		// modules are top level entities
		// put it in the CompilatinUnit node
		ASTCompilationUnit parentNode = (ASTCompilationUnit) parsingCtxt.valueStackTop();
		parentNode.setProgramUnit(moduleNode);
	}


	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		
		fctStmt.setLabel(asttk(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, asttk(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, asttk(eos));
		
		fctStmt.setFunctionName(asttk(name));

		parsingCtxt.valueStackPush(fctStmt);
	}

	@Override
	public void end_function_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
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
		
		parsingCtxt.valueStackPush(endFct);
	}

	@Override
	public void function_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTFunctionSubprogramNode fctNode = new ASTFunctionSubprogramNode();

		fctNode.setEndFunctionStmt((ASTEndFunctionStmtNode) parsingCtxt.valueStackPop());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExePart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
		}
		fctNode.setBody(specifications);
		fctNode.setFunctionStmt((ASTFunctionStmtNode) parsingCtxt.valueStackPop());

		parsingCtxt.valueStackPush(fctNode);
	}

	
	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		
		pcdStmt.setLabel(asttk(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, asttk(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, asttk(eos));
		
		pcdStmt.setSubroutineName(asttk(name));
		
		parsingCtxt.valueStackPush( pcdStmt);
	}
	
	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
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
		
		parsingCtxt.valueStackPush(endPcd);
	}

	@Override
	public void subroutine_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTSubroutineSubprogramNode pcdNode = new ASTSubroutineSubprogramNode();

		pcdNode.setEndSubroutineStmt((ASTEndSubroutineStmtNode) parsingCtxt.valueStackPop());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExePart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop();
		}
		pcdNode.setBody(specifications);
		pcdNode.setSubroutineStmt((ASTSubroutineStmtNode) parsingCtxt.valueStackPop());

		parsingCtxt.valueStackPush(pcdNode);
	}


	@Override
	public void block_data_stmt(Token label, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
		parsingCtxt.valueStackPush((IASTNode) new ASTListNode<>());
	}

	@Override
	public void end_block_data_stmt(Token label, Token endKeyword, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
	}
	
	@Override
	public void block_data() {
		// pop end_block_data_stmt
		parsingCtxt.valueStackPop(); // specification_part list
		parsingCtxt.valueStackPop(); // block_data_stmt
		
		
		super.block_data();
	}



	@Override
	public void specification_part(int numUseStmts, int numImportStmts, int numImplStmts, int numDeclConstructs) {
		// numImplStmts = 0
		ASTListNode<IASTNode> specifications = new ASTListNode<IASTNode>();
		for (int i=0; i< numDeclConstructs; i++) {
			specifications.add(parsingCtxt.valueStackPop());
		}
		//for (int i=0; i< numImportStmts; i++) {
		//	specifications.add(parsingCtxt.valueStackPop());
		//}
		//for (int i=0; i< numUseStmts; i++) {
		//	specifications.add(parsingCtxt.valueStackPop());
		//}
		parsingCtxt.valueStackPush(specifications);
	}

	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		ASTTypeDeclarationStmtNode typeDecl = new ASTTypeDeclarationStmtNode();
		
		typeDecl.setLabel(asttk(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));

		typeDecl.setEntityDeclList( (IASTListNode<ASTEntityDeclNode>) parsingCtxt.valueStackPop());
		for (int i=0; i < numAttributes; i++ ) {
			ASTAttrSpecSeqNode attrSpecSeq  = new ASTAttrSpecSeqNode();
			attrSpecSeq.setAttrSpec((ASTAttrSpecNode) parsingCtxt.valueStackPop());
			typeDecl.getAttrSpecSeq().add( attrSpecSeq);
		}
		//typeDecl.setASTField(, (ASTTypeSpecNode)parsingCtxt.valueStackPop());
		
		parsingCtxt.valueStackPush(typeDecl);
	}

	@Override
	public void initialization(boolean hasExpr, boolean hasNullInit) {
		// for now pruning expressions
		if (hasExpr) {
			IASTNode top = parsingCtxt.valueStackTop();
			if (top instanceof ASTVarOrFnRefNode) {
				parsingCtxt.valueStackPop();
			}
		}
	}

/*	@Override
	public void intrinsic_type_spec(Token keyword1, Token keyword2, int type, boolean hasKindSelector) {
		// for now pruning expressions
		if ( (type == IActionEnums.IntrinsicTypeSpec_CHARACTER) ||
			 (type == IActionEnums.IntrinsicTypeSpec_LOGICAL) ) {
			if (hasKindSelector) {
				parsingCtxt.valueStackPop();
			}
		}
	}

	
/*

	@Override
	public void component_def_stmt(int type) {
System.out.println("component_def_stmt");
		super.component_def_stmt(type);
	}

	@Override
	public void component_decl(Token id, boolean hasComponentArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasComponentInitialization) {
System.out.println("component_decl @"+id.getLine()+":"+id.getCharPositionInLine()+ " -- "+id.getText() +" --> POP(list)");
		ASTListNode<ASTToken> component_decl_list = (ASTListNode<ASTToken>) parsingCtxt.valueStackPop();
		component_decl_list.add(asttk(id));
	}

	@Override
	public void component_decl_list__begin() {
System.out.println("component_decl_list__begin --> PUSH(list)");
		parsingCtxt.valueStackPush( new ASTListNode<ASTToken>());
		super.component_decl_list__begin();
	}

	@Override
	public void component_decl_list(int count) {
System.out.println("component_decl_list --> POP(list)");
		parsingCtxt.valueStackPop( );  // ASTListNode<ASTEntityDeclNode>
		super.component_decl_list(count);
	}

	@Override
	public void data_component_def_stmt(Token label, Token eos, boolean hasSpec) {
		ASTListNode<ASTNode> listDeclarationConstruct;
		ASTTypeDeclarationStmtNode typeDecl;

		typeDecl = (ASTTypeDeclarationStmtNode) parsingCtxt.valueStackPop();
		typeDecl.setLabel(asttk(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));
System.out.println("data_component_def_stmt @"+eos.getLine()+":"+eos.getCharPositionInLine()+" --> POP(TypeDecl), TOP(list)");

		listDeclarationConstruct = (ASTListNode<ASTNode>)parsingCtxt.valueStackTop();
		listDeclarationConstruct.add( typeDecl);
	}
*/

	
	@Override
	public void executable_construct() {
		if (parsingCtxt.valueStackTop() instanceof ASTVarOrFnRefNode) {
		}
		else if(parsingCtxt.valueStackTop() instanceof ASTCallStmtNode) {
		}
		else {
			parsingCtxt.valueStackPush(new ASTNullNode());
		}
	}


	@Override
	public void execution_part(int execution_part_count) {
		IASTListNode<IASTNode> exec_parts = new ASTListNode<>();
		IASTNode executable = parsingCtxt.valueStackPop();
		if (! executable.isNullNode()) {
			exec_parts.add( executable);
		}
		for (int i=0; i< execution_part_count; i++) {
			executable = parsingCtxt.valueStackPop();
			if (! executable.isNullNode()) {
				exec_parts.add( executable);
			}
		}
		parsingCtxt.valueStackPush(exec_parts);
	}

	@Override
	public void execution_part_construct(boolean is_executable_construct) {
		if (! is_executable_construct) {
			parsingCtxt.valueStackPush(new ASTNullNode());
		}
	}


	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		ASTEntityDeclNode entityDecl = new ASTEntityDeclNode();
		entityDecl.setObjectName(asttk(id));
		parsingCtxt.valueStackPush( entityDecl);
	}

	@Override
	public void entity_decl_list(int count) {
		IASTListNode<ASTEntityDeclNode> declList = new ASTListNode<ASTEntityDeclNode>();
		
		for (int i=0; i<count; i++) {
			declList.add((ASTEntityDeclNode) parsingCtxt.valueStackPop());
		}
		parsingCtxt.valueStackPush( declList);
	}


	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		ASTAttrSpecNode attrSpec = new ASTAttrSpecNode();
		
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
		default: return;
		}

		parsingCtxt.valueStackPush(attrSpec);
	}

	@Override
	public void call_stmt(Token label, Token callKeyword, Token eos, boolean hasActualArgSpecList) {
		ASTCallStmtNode call = new ASTCallStmtNode();
		
		call.setLabel( asttk(label));
		call.setASTField( ASTCallStmtNode.TCALL, asttk(callKeyword));
		call.setSubroutineName( asttk((Token) parsingCtxt.valueGet("part_ref.id")) );
		call.setASTField( ASTCallStmtNode.TEOS, asttk(eos));

		parsingCtxt.valueStackPush(call);
	}

	@Override
	public void designator_or_func_ref() {
		ASTVarOrFnRefNode invok = new ASTVarOrFnRefNode();
		
		invok.setName(asttk((Token) parsingCtxt.valueGet("part_ref.id")));   // TODO change for valueStack
		
		parsingCtxt.valueStackPush(invok);
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
