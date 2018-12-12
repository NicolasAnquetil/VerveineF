package fr.inria.verveine.extractor.fortran.ast;


import org.antlr.runtime.Token;

import antlr.collections.impl.ASTArray;
import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IActionEnums;
import fortran.ofp.parser.java.IFortranParser;

public class ParserActionAST extends FortranParserActionNull {

	protected ParsingContext parsingCtxt;
	
	/**
	 * Helper class for island grammar parsing: Allows to pop many entries from the parsingContext valuesStack
	 */
	protected abstract class Validator {
		abstract boolean validate(IASTNode node);
	}

	/**
	 * Helper class for island grammar parsing: Pops a given number of entries from the parsingContext valuesStack
	 */
	protected class CountValidator extends Validator {
		int count;
		CountValidator(int count) { this.count = count; }
		boolean validate(IASTNode node) { return count-- > 0; }
	}

	/**
	 * Helper class for island grammar parsing: Allows to pop all entries of a given type from the parsingContext valuesStack
	 */
	protected class WhileTypeValidator extends Validator {
		Class<? extends IASTNode> clazz;
		WhileTypeValidator(Class<? extends IASTNode> clazz) { this.clazz = clazz; }
		boolean validate(IASTNode node) { return clazz.isInstance(node); }
	}

	/**
	 * Helper class for island grammar parsing: Allows to pop all entries not of a given type from the parsingContext valuesStack
	 */
	protected class UntilTypeValidator extends Validator {
		Class<? extends IASTNode> clazz;
		UntilTypeValidator(Class<? extends IASTNode> clazz) { this.clazz = clazz; }
		boolean validate(IASTNode node) { return ! clazz.isInstance(node); }
	}

	/**
	 * Helper class for island grammar parsing: Allows to pop all entries that are not {@link #IASTNode.isTopLevel()} from the parsingContext valuesStack
	 */
	protected class UntilTopEntityValidator extends Validator {
		boolean validate(IASTNode node) { return ! node.isTopLevelNode(); }
	}

	
	
	public ParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);
	}

	public ASTNode getAST() {
		return (ASTNode) parsingCtxt.topValueStack();
	}

	/**
	 * Helper method for island grammar parsing: Allows to pop many entries from the parsingContext valuesStack
	 */
	private IASTListNode<IASTNode> parsingContextPopAll(Validator valid) {
		IASTListNode<IASTNode> poped = new ASTListNode<>();

		IASTNode topNode = parsingCtxt.topValueStack();
		while ( valid.validate(topNode) ) {
			poped.add( topNode);
			parsingCtxt.popValueStack();
			topNode = parsingCtxt.topValueStack();
		}
		return poped;
	}
	
	
	
	@Override
	public void start_of_file(String filename, String path) {
		 parsingCtxt = new ParsingContext();
		 parsingCtxt.pushValueStack( new ASTCompilationUnit(filename));
	}


	@Override
	public void end_of_file(String filename, String path) {
		IASTListNode<IASTNode> decls;
		decls = parsingContextPopAll(new UntilTypeValidator(ASTCompilationUnit.class));
		ASTCompilationUnit parentNode = (ASTCompilationUnit) parsingCtxt.topValueStack();
		parentNode.setBody(decls);
	}


	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		
		pgmStmt.setLabel( asttk(label));
		pgmStmt.setASTField(ASTProgramStmtNode.TPROGRAM, asttk(programKeyword));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, asttk(eos));
		pgmStmt.setProgramName( asttk(id));

		parsingCtxt.pushValueStack(pgmStmt);
	}

	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setLabel(asttk(label));
		endPgmStmt.setEndToken(asttk(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, asttk(programKeyword));
		endPgmStmt.setEndName(asttk(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, asttk(eos));

		parsingCtxt.pushValueStack(endPgmStmt);
	}

	@Override
	public void main_program(boolean hasProgramStmt, boolean hasExecutionPart, boolean hasInternalSubprogramPart) {
		ASTMainProgramNode mainPgm = new ASTMainProgramNode();

		mainPgm.setEndProgramStmt((ASTEndProgramStmtNode) parsingCtxt.popValueStack());
		if (hasInternalSubprogramPart) {
			//mainPgm.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExecutionPart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
		}
		mainPgm.setBody(specifications);
		if (hasProgramStmt) {
			mainPgm.setProgramStmt((ASTProgramStmtNode) parsingCtxt.popValueStack());
		}
		
		parsingCtxt.pushValueStack(mainPgm);
	}


	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();

		moduleStmt.setLabel(asttk(label));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, asttk(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, asttk(eos));

		moduleStmt.setModuleName(asttk(id));
		
		parsingCtxt.pushValueStack(moduleStmt);
	}

	@Override
	public void module_subprogram_part(int count) {
		if (count > 0) {
			// we skip count elements to fetch the count-1 element on top of the valueStack
			// this must be a IASTListNode<IModuleBodyConstruct> pushed by specification_part
			// we add to it all (count) module_subprograms
			IASTListNode<IModuleBodyConstruct> moduleBody = (IASTListNode<IModuleBodyConstruct>) parsingCtxt.valueStackTop(-1 * (count+1));
			for (int i=0; i<count; i++) {
				moduleBody.add((IModuleBodyConstruct) parsingCtxt.popValueStack());
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

		parsingCtxt.pushValueStack(endModule);
	}

	@Override
	public void module() {
		ASTModuleNode moduleNode = new ASTModuleNode();
		
		try {
			moduleNode.setEndModuleStmt((ASTEndModuleStmtNode) parsingCtxt.popValueStack());
			moduleNode.setModuleBody((IASTListNode<IModuleBodyConstruct>) parsingCtxt.popValueStack());
			moduleNode.setModuleStmt((ASTModuleStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			// try to recover from error ...
			parsingContextPopAll(new UntilTopEntityValidator());
			System.err.println("Parsing error "+moduleNode.getEndModuleStmt().getEndName()+", ignoring all since  " + parsingCtxt.topValueStack());
			return;
		}

		parsingCtxt.pushValueStack(moduleNode);
	}

	

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		
		fctStmt.setLabel(asttk(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, asttk(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, asttk(eos));
		
		fctStmt.setFunctionName(asttk(name));

		parsingCtxt.pushValueStack(fctStmt);
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
		
		parsingCtxt.pushValueStack(endFct);
	}

	@Override
	public void function_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTFunctionSubprogramNode fctNode = new ASTFunctionSubprogramNode();

		fctNode.setEndFunctionStmt((ASTEndFunctionStmtNode) parsingCtxt.popValueStack());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExePart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
		}
		fctNode.setBody(specifications);
		try {
			fctNode.setFunctionStmt((ASTFunctionStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			// try to recover from error ...
			parsingContextPopAll(new UntilTopEntityValidator());
			System.err.println("Parsing error after "+fctNode.getEndFunctionStmt().getEndName()+", ignoring all since  " + parsingCtxt.topValueStack());
			return;
		}

		parsingCtxt.pushValueStack(fctNode);
	}

	
	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		
		pcdStmt.setLabel(asttk(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, asttk(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, asttk(eos));
		
		pcdStmt.setSubroutineName(asttk(name));
		
		parsingCtxt.pushValueStack( pcdStmt);
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
		
		parsingCtxt.pushValueStack(endPcd);
	}

	@Override
	public void subroutine_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTSubroutineSubprogramNode pcdNode = new ASTSubroutineSubprogramNode();

		pcdNode.setEndSubroutineStmt((ASTEndSubroutineStmtNode) parsingCtxt.popValueStack());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}
		IASTListNode<IBodyConstruct> specifications;
		if (hasExePart) {
			IASTListNode<IBodyConstruct> execList = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
			specifications.addAll(execList);
		}
		else {
			specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
		}
		pcdNode.setBody(specifications);
		try {
			pcdNode.setSubroutineStmt((ASTSubroutineStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			// try to recover from error ...
			parsingContextPopAll(new UntilTopEntityValidator());
			System.err.println("Parsing error after "+ pcdNode.getEndSubroutineStmt().getEndName()+", ignoring all since  " + parsingCtxt.topValueStack());
			return;
		}

		parsingCtxt.pushValueStack(pcdNode);
	}


	@Override
	public void block_data_stmt(Token label, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
		parsingCtxt.pushValueStack((IASTNode) new ASTListNode<>());
	}

	@Override
	public void end_block_data_stmt(Token label, Token endKeyword, Token blockKeyword, Token dataKeyword, Token id, Token eos) {
	}
	
	@Override
	public void block_data() {
		// pop end_block_data_stmt
		parsingCtxt.popValueStack(); // specification_part list
		parsingCtxt.popValueStack(); // block_data_stmt
		
		
		super.block_data();
	}



	@Override
	public void specification_part(int numUseStmts, int numImportStmts, int numImplStmts, int numDeclConstructs) {
		// numImplStmts = 0
		ASTListNode<IASTNode> specifications = new ASTListNode<IASTNode>();
		specifications.addAll( parsingContextPopAll(new CountValidator(numDeclConstructs)));
		//specifications.addAll( parsingContextPopAll(new CountValidator(numImportStmts)));
		//specifications.addAll( parsingContextPopAll(new CountValidator(numUseStmts)));
		parsingCtxt.pushValueStack(specifications);
	}

	
	
	@Override
	public void data_stmt(Token label, Token keyword, Token eos, int count) {
		parsingCtxt.pushValueStack(new ASTNullNode());  // counts as a declaration_construct in specification_part(...)
	}

	@Override
	public void save_stmt(Token label, Token keyword, Token eos, boolean hasSavedEntityList) {
		parsingCtxt.pushValueStack(new ASTNullNode());  // counts as a declaration_construct in specification_part(...)
	}


	@Override
	public void declaration_type_spec(Token udtKeyword, int type) {
		IASTNode top = parsingCtxt.topValueStack();
		if (top instanceof ASTVarOrFnRefNode) {
			parsingCtxt.popValueStack();
		}
	}

	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		ASTTypeDeclarationStmtNode typeDecl = new ASTTypeDeclarationStmtNode();
		
		typeDecl.setLabel(asttk(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, asttk(eos));

		typeDecl.setEntityDeclList( (IASTListNode<ASTEntityDeclNode>) parsingCtxt.popValueStack());
		for (int i=0; i < numAttributes; i++ ) {
			ASTAttrSpecSeqNode attrSpecSeq  = new ASTAttrSpecSeqNode();
			bugLocator(ASTVarOrFnRefNode.class);
			attrSpecSeq.setAttrSpec((ASTAttrSpecNode) parsingCtxt.popValueStack());
			typeDecl.getAttrSpecSeq().add( attrSpecSeq);
		}
		//typeDecl.setASTField(, (ASTTypeSpecNode)parsingCtxt.valueStackPop());
		
		parsingCtxt.pushValueStack(typeDecl);
	}

	@Override
	public void derived_type_stmt(Token label, Token keyword, Token id, Token eos, boolean hasTypeAttrSpecList, boolean hasGenericNameList) {
		ASTDerivedTypeStmtNode typeStmt = new ASTDerivedTypeStmtNode();
		typeStmt.setLabel(asttk(label));
		typeStmt.setTypeName(asttk(id));
		
		parsingCtxt.pushValueStack(typeStmt);
	}

	@Override
	public void derived_type_def() {
		ASTDerivedTypeDefNode derivedType = new ASTDerivedTypeDefNode();
		// ignore everything in the derived_type_def 
		parsingContextPopAll( new UntilTypeValidator(ASTDerivedTypeStmtNode.class));
		derivedType.setDerivedTypeStmt((ASTDerivedTypeStmtNode) parsingCtxt.popValueStack());
		parsingCtxt.pushValueStack(derivedType);
	}


	@Override
	public void initialization(boolean hasExpr, boolean hasNullInit) {
		// for now pruning expressions
		if (hasExpr) {
			parsingContextPopAll(new WhileTypeValidator(ASTVarOrFnRefNode.class));
		}
	}


	/*
	@Override
	public void intrinsic_type_spec(Token keyword1, Token keyword2, int type, boolean hasKindSelector) {
		// for now pruning expressions
		if (hasKindSelector) {
			parsingCtxt.valueStackPop();
		}
	}

	@Override
	public void kind_selector(Token token1, Token token2, boolean hasExpression) {
		if (hasExpression) {
			parsingCtxt.valueStackPop();
		}
	}

	@Override
	public void char_selector(Token tk1, Token tk2, int kindOrLen1, int kindOrLen2, boolean hasAsterisk) {
		super.char_selector(arg0, arg1, arg2, arg3, arg4);
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

	/*
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
*/

	@Override
	public void execution_part(int execution_part_count) {
		IASTListNode<IASTNode> exec_parts = new ASTListNode<>();
		exec_parts.addAll(
				parsingContextPopAll( new Validator() {
					@Override
					boolean validate(IASTNode node) {
						return (node instanceof ASTVarOrFnRefNode) || (node instanceof ASTCallStmtNode);
					}
				})
			);

		parsingCtxt.pushValueStack(exec_parts);
	}

/*
	@Override
	public void execution_part_construct(boolean is_executable_construct) {
		if (! is_executable_construct) {
			parsingCtxt.valueStackPush(new ASTNullNode());
		}
	}
*/

	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		ASTEntityDeclNode entityDecl = new ASTEntityDeclNode();
		entityDecl.setObjectName(asttk(id));
		if (hasArraySpec) {
			parsingContextPopAll( new UntilTypeValidator(ASTVarOrFnRefNode.class));
		}
		parsingCtxt.pushValueStack( entityDecl);
	}

	@Override
	public void entity_decl_list(int count) {
		IASTListNode<ASTEntityDeclNode> declList = new ASTListNode<ASTEntityDeclNode>();
		declList.addAll( (IASTListNode<? extends ASTEntityDeclNode>) parsingContextPopAll( new CountValidator(count)));
		parsingCtxt.pushValueStack( declList);
	}


	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		ASTAttrSpecNode attrSpec = new ASTAttrSpecNode();
		ASTToken tk = null;

		if (attrKeyword != null) {
			tk = asttk(attrKeyword);
		}
		
		switch (attr) {
		case IActionEnums.AttrSpec_access: attrSpec.setAccessSpec((ASTAccessSpecNode) parsingCtxt.popValueStack()); break;
		case IActionEnums.AttrSpec_ALLOCATABLE: attrSpec.setIsAllocatable(tk); break;
		case IActionEnums.AttrSpec_ASYNCHRONOUS: attrSpec.setIsAsync(tk); break;
		case IActionEnums.AttrSpec_CODIMENSION: attrSpec.setIsCodimension(tk); break;
		case IActionEnums.AttrSpec_CONTIGUOUS: attrSpec.setIsContiguous(tk); break;
		case IActionEnums.AttrSpec_DIMENSION: attrSpec.setIsDimension(tk); break;
		case IActionEnums.AttrSpec_EXTERNAL: attrSpec.setIsExternal(tk); break;
		case IActionEnums.AttrSpec_INTENT: attrSpec.setIsIntent(tk); break;
		case IActionEnums.AttrSpec_INTRINSIC: attrSpec.setIsIntrinsic(tk); break;
		case IActionEnums.AttrSpec_OPTIONAL: attrSpec.setIsOptional(tk); break;
		case IActionEnums.AttrSpec_PARAMETER: attrSpec.setIsParameter(tk); break;
		case IActionEnums.AttrSpec_POINTER: attrSpec.setIsPointer(tk); break;
		case IActionEnums.AttrSpec_PROTECTED: attrSpec.setIsProtected(tk); break;
		case IActionEnums.AttrSpec_SAVE: attrSpec.setIsSave(tk); break;
		case IActionEnums.AttrSpec_TARGET: attrSpec.setIsTarget(tk); break;
		case IActionEnums.AttrSpec_VALUE: attrSpec.setIsValue(tk); break;
		case IActionEnums.AttrSpec_VOLATILE: attrSpec.setIsVolatile(tk); break;
		//case FortranLexer.T_KIND: attrSpec.; break;
		//case FortranLexer.T_LEN: attrSpec.; break;
		default:
			System.err.println("Unknown attr_spec:"+attr+"("+attrKeyword+")");
		}

		parsingCtxt.pushValueStack(attrSpec);
	}

	@Override
	public void access_stmt(Token label, Token eos, boolean hasList) {
		ASTAccessStmtNode accessStmt = new ASTAccessStmtNode();
		accessStmt.setLabel(asttk(label));
		accessStmt.setASTField(ASTAccessStmtNode.T_EOS,asttk(eos));
		
		accessStmt.setAccessSpec((ASTAccessSpecNode) parsingCtxt.popValueStack());
		
		parsingCtxt.pushValueStack(accessStmt);
	}


	@Override
	public void array_spec_element(int type) {
		if ((type == IActionEnums.ArraySpecElement_expr) ||
				(type == IActionEnums.ArraySpecElement_expr_colon) ||
				(type == IActionEnums.ArraySpecElement_expr_colon_expr) ||
				(type == IActionEnums.ArraySpecElement_expr_colon_asterisk) ) {
			if (parsingCtxt.topValueStack() instanceof ASTVarOrFnRefNode) {
				parsingCtxt.popValueStack();				
			}
			if (type == IActionEnums.ArraySpecElement_expr_colon_expr) {
				if (parsingCtxt.topValueStack() instanceof ASTVarOrFnRefNode) {
					parsingCtxt.popValueStack();				
				}
			}
		}
	}

	@Override
	public void access_spec(Token keyword, int type) {
		ASTAccessSpecNode accessSpec = new ASTAccessSpecNode();
		switch (type) {
		case IActionEnums.AttrSpec_PUBLIC: accessSpec.setIsPublic(asttk(keyword)); break;
		case IActionEnums.AttrSpec_PRIVATE: accessSpec.setIsPrivate(asttk(keyword)); break;
		default:
			System.err.println("Unknown access_spec:"+type+"("+keyword+")");
		}
		parsingCtxt.pushValueStack(accessSpec);
	}

	@Override
	public void call_stmt(Token label, Token callKeyword, Token eos, boolean hasActualArgSpecList) {
		ASTCallStmtNode call = new ASTCallStmtNode();
		
		call.setLabel( asttk(label));
		call.setASTField( ASTCallStmtNode.TCALL, asttk(callKeyword));
		call.setSubroutineName( asttk((Token) parsingCtxt.valueGet("part_ref.id")) );
		call.setASTField( ASTCallStmtNode.TEOS, asttk(eos));

		parsingCtxt.pushValueStack(call);
	}

	@Override
	public void designator_or_func_ref() {
		ASTVarOrFnRefNode invok = new ASTVarOrFnRefNode();
		
		invok.setName(asttk((Token) parsingCtxt.valueRetreive("part_ref.id")));   // TODO change for valueStack
		
		parsingCtxt.pushValueStack(invok);
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
	
	/**
	 * helps debugging ClassCastException bugs when poping nodes from the context stack<br>
	 * For this, there should always be a breakpoint set inside the if
	 */
	private void bugLocator(Class<? extends IASTNode> clazz) {
		IASTNode top = parsingCtxt.topValueStack();
		if (clazz.isInstance(top)) {
			System.out.println();
		}
	}

}
