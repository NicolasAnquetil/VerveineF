package fr.inria.verveine.extractor.fortran.parser.ast;


import java.util.Collection;

import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranParserActionNull;
import fortran.ofp.parser.java.FortranToken;
import fortran.ofp.parser.java.IActionEnums;
import fortran.ofp.parser.java.IFortranParser;
import fr.inria.verveine.extractor.fortran.parser.CountValidator;
import fr.inria.verveine.extractor.fortran.parser.ParsingContext;
import fr.inria.verveine.extractor.fortran.parser.UntilTopEntityValidator;
import fr.inria.verveine.extractor.fortran.parser.UntilTypeValidator;
import fr.inria.verveine.extractor.fortran.parser.Validator;
import fr.inria.verveine.extractor.fortran.parser.WhileTypeValidator;

public class ParserActionAST extends FortranParserActionNull {

	protected ParsingContext parsingCtxt;

	protected int openedFiles;
	
	
	public ParserActionAST(String[] args, IFortranParser parser, String filename) {
		super(args, parser, filename);
		openedFiles = 0;
	}

	public ASTNode getAST() {
		return (ASTNode) parsingCtxt.topValueStack();
	}


	@Override
	public void start_of_file(String filename, String path) {
		if (! path.equals("ERROR_FILE_NOT_FOUND")) {
			// if path == "ERROR_FILE_NOT_FOUND", this is a missing include file
			// and it will not be "closed" with end_of_file()
			openedFiles++;
			parsingCtxt = new ParsingContext();
			parsingCtxt.pushValueStack( new ASTCompilationUnit(filename));
		}
	}

	@Override
	public void end_of_file(String filename, String path) {
		openedFiles--;
		if (openedFiles == 0) {
			IASTListNode<IASTNode> decls;
			decls = parsingCtxt.popAllValueStack(new UntilTypeValidator(ASTCompilationUnit.class));
			ASTCompilationUnit parentNode = (ASTCompilationUnit) parsingCtxt.topValueStack();
			parentNode.setBody(decls);
			decls.setParent(parentNode);
		}
	}


	@Override
	public void	program_stmt(Token label, Token programKeyword, Token id, Token eos) {
		ASTProgramStmtNode pgmStmt = new ASTProgramStmtNode();
		
		pgmStmt.setLabel( ASTToken.with(label));
		pgmStmt.setASTField(ASTProgramStmtNode.TPROGRAM, ASTToken.with(programKeyword));
		pgmStmt.setASTField(ASTProgramStmtNode.TEOS, ASTToken.with(eos));
		pgmStmt.setProgramName( ASTToken.with(id));

		parsingCtxt.pushValueStack(pgmStmt);
	}

	@Override
	public void	end_program_stmt(Token label, Token endKeyword, Token programKeyword, Token id, Token eos) {
		ASTEndProgramStmtNode endPgmStmt = new ASTEndProgramStmtNode();
		
		endPgmStmt.setLabel(ASTToken.with(label));
		endPgmStmt.setEndToken(ASTToken.with(endKeyword));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TPROGRAM, ASTToken.with(programKeyword));
		endPgmStmt.setEndName(ASTToken.with(id));
		endPgmStmt.setASTField(ASTEndProgramStmtNode.TEOS, ASTToken.with(eos));

		parsingCtxt.pushValueStack(endPgmStmt);
	}

	@Override
	public void main_program(boolean hasProgramStmt, boolean hasExecutionPart, boolean hasInternalSubprogramPart) {
		ASTMainProgramNode mainPgm = new ASTMainProgramNode();

		mainPgm.setEndProgramStmt((ASTEndProgramStmtNode) parsingCtxt.popValueStack());
		if (hasInternalSubprogramPart) {
			//mainPgm.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}

		IASTListNode<IBodyConstruct> specifications = popBodyAsList(hasExecutionPart);
		mainPgm.setBody(specifications);
		specifications.setParent(mainPgm);

		if (hasProgramStmt) {
			mainPgm.setProgramStmt((ASTProgramStmtNode) parsingCtxt.popValueStack());
		}
		
		parsingCtxt.pushValueStack(mainPgm);
	}


	@Override
	public void	module_stmt(Token label, Token moduleKeyword, Token id, Token eos) {
		ASTModuleStmtNode moduleStmt = new ASTModuleStmtNode();

		moduleStmt.setLabel(ASTToken.with(label));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, ASTToken.with(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TMODULE, ASTToken.with(moduleKeyword));
		moduleStmt.setASTField(ASTModuleStmtNode.TEOS, ASTToken.with(eos));

		moduleStmt.setModuleName(ASTToken.with(id));
		
		parsingCtxt.pushValueStack(moduleStmt);
	}

	@Override
	public void module_subprogram_part(int count) {
		if (count > 0) {
			IASTListNode<IASTNode> lConstruct = parsingCtxt.popAllValueStack( new CountValidator(count));
			IASTListNode<IModuleBodyConstruct> moduleBody = (IASTListNode<IModuleBodyConstruct>) parsingCtxt.topValueStack();
			moduleBody.addAll((Collection<? extends IModuleBodyConstruct>) lConstruct);
		}
	}

	@Override
	public void	end_module_stmt(Token label, Token endKeyword, Token moduleKeyword, Token id, Token eos) {
		ASTEndModuleStmtNode endModule = new ASTEndModuleStmtNode();

		endModule.setLabel(ASTToken.with(label));
		endModule.setASTField(ASTEndModuleStmtNode.TEND, ASTToken.with(endKeyword));
		endModule.setASTField(ASTEndModuleStmtNode.TENDMODULE, ASTToken.with(moduleKeyword));
		endModule.setEndName(ASTToken.with(id));
		endModule.setASTField(ASTEndModuleStmtNode.TEOS, ASTToken.with(eos));

		parsingCtxt.pushValueStack(endModule);
	}

	@Override
	public void module() {
		ASTModuleNode moduleNode = new ASTModuleNode();
		
		try {
			moduleNode.setEndModuleStmt((ASTEndModuleStmtNode) parsingCtxt.popValueStack());
			IASTListNode<IModuleBodyConstruct> moduleBody = (IASTListNode<IModuleBodyConstruct>) parsingCtxt.popValueStack();
			moduleNode.setModuleBody(moduleBody);
			moduleBody.setParent(moduleNode);
			moduleNode.setModuleStmt((ASTModuleStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			// try to recover from error ...
			parsingCtxt.popAllValueStack(new UntilTopEntityValidator());
			ASTToken lastToken = (ASTToken) moduleNode.getEndModuleStmt().getASTField(ASTEndModuleStmtNode.TEOS);
			System.err.println("Parsing error "+lastToken+", ignoring all since  " + parsingCtxt.topValueStack());
			return;
		}

		parsingCtxt.pushValueStack(moduleNode);
	}

	@Override
	public void function_stmt(Token label, Token keyword, Token name, Token eos, boolean hasGenericNameList, boolean hasSuffix) {
		ASTFunctionStmtNode fctStmt = new ASTFunctionStmtNode();
		
		fctStmt.setLabel(ASTToken.with(label));
		fctStmt.setASTField(ASTFunctionStmtNode.TFUNCTION, ASTToken.with(keyword));
		fctStmt.setASTField(ASTFunctionStmtNode.TEOS, ASTToken.with(eos));
		
		fctStmt.setFunctionName(ASTToken.with(name));

		parsingCtxt.pushValueStack(fctStmt);
	}

	@Override
	public void end_function_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTEndFunctionStmtNode endFct = new ASTEndFunctionStmtNode();

		endFct.setLabel(ASTToken.with(label));
		if ( ((FortranToken)keyword1).getTokenIndex() == FortranLexer.T_ENDFUNCTION) {
			endFct.setASTField(ASTEndFunctionStmtNode.TENDFUNCTION, ASTToken.with(keyword1));
		}
		else {
			endFct.setASTField(ASTEndFunctionStmtNode.TEND, ASTToken.with(keyword1));
		}
		endFct.setASTField(ASTEndFunctionStmtNode.TFUNCTION, ASTToken.with(keyword2));
		endFct.setASTField(ASTEndFunctionStmtNode.TEOS, ASTToken.with(eos));
		
		parsingCtxt.pushValueStack(endFct);
	}

	@Override
	public void function_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTFunctionSubprogramNode fctNode = new ASTFunctionSubprogramNode();

		fctNode.setEndFunctionStmt((ASTEndFunctionStmtNode) parsingCtxt.popValueStack());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}

		IASTListNode<IBodyConstruct> specifications = popBodyAsList(hasExePart);
		fctNode.setBody(specifications);
		specifications.setParent(fctNode);

		try {
			fctNode.setFunctionStmt((ASTFunctionStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			traceSubprogramErrorAndRecover( fctNode.getEndFunctionStmt().getASTField(ASTEndSubroutineStmtNode.TEOS));
			return;
		}

		parsingCtxt.pushValueStack(fctNode);
	}

	
	@Override
	public void subroutine_stmt(Token label, Token keyword, Token name, Token eos, boolean hasPrefix, boolean hasDummyArgList, boolean hasBindingSpec, boolean hasArgSpecifier) {
		ASTSubroutineStmtNode pcdStmt = new ASTSubroutineStmtNode();
		
		pcdStmt.setLabel(ASTToken.with(label));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TSUBROUT, ASTToken.with(keyword));
		pcdStmt.setASTField(ASTSubroutineStmtNode.TEOS, ASTToken.with(eos));
		
		pcdStmt.setSubroutineName(ASTToken.with(name));
		
		parsingCtxt.pushValueStack( pcdStmt);
	}
	
	@Override
	public void end_subroutine_stmt(Token label, Token keyword1, Token keyword2, Token name, Token eos) {
		ASTEndSubroutineStmtNode endPcd = new ASTEndSubroutineStmtNode();
		
		endPcd.setLabel(ASTToken.with(label));
		if ( ((FortranToken)keyword1).getTokenIndex() == FortranLexer.T_ENDSUBROUTINE) {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TENDSUBROUT, ASTToken.with(keyword1));
		}
		else {
			endPcd.setASTField(ASTEndSubroutineStmtNode.TEND, ASTToken.with(keyword1));
		}
		endPcd.setASTField(ASTEndSubroutineStmtNode.TSUBROUT, ASTToken.with(keyword2));
		endPcd.setASTField(ASTEndSubroutineStmtNode.TEOS, ASTToken.with(eos));
		
		parsingCtxt.pushValueStack(endPcd);
	}

	@Override
	public void subroutine_subprogram(boolean hasExePart, boolean hasIntSubProg) {
		ASTSubroutineSubprogramNode pcdNode = new ASTSubroutineSubprogramNode();
		
		pcdNode.setEndSubroutineStmt((ASTEndSubroutineStmtNode) parsingCtxt.popValueStack());
		if (hasIntSubProg) {
			//fctNode.setInternalSubprograms((IASTListNode<IBodyConstruct>) parsingCtxt.valueStackPop());
		}

		IASTListNode<IBodyConstruct> specifications = popBodyAsList(hasExePart);
		pcdNode.setBody(specifications);
		specifications.setParent(pcdNode);

		try {
			pcdNode.setSubroutineStmt((ASTSubroutineStmtNode) parsingCtxt.popValueStack());
		}
		catch (ClassCastException e) {
			traceSubprogramErrorAndRecover( pcdNode.getEndSubroutineStmt().getASTField(ASTEndSubroutineStmtNode.TEOS));
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
	public void interface_block() {
		parsingCtxt.pushValueStack(new ASTInterfaceBodyNode());
	}

	@Override
	public void specification_part(int numUseStmts, int numImportStmts, int numImplStmts, int numDeclConstructs) {
		ASTListNode<IASTNode> specifications = (ASTListNode<IASTNode>) parsingCtxt.popAllValueStack(new CountValidator(numDeclConstructs));
		//specifications.addAll( parsingContextPopAll(new CountValidator(numImportStmts)));
		specifications.addAll( parsingCtxt.popAllValueStack(new CountValidator(numUseStmts)));
		parsingCtxt.pushValueStack(specifications);
	}


	@Override
	public void use_stmt(Token label, Token useKeyword, Token id, Token onlyKeyword, Token eos, boolean hasModuleNature,
			boolean hasRenameList, boolean hasOnly) {
		ASTUseStmtNode useStmt = new ASTUseStmtNode();

		useStmt.setLabel(ASTToken.with(label));
		useStmt.setName(ASTToken.with(id));
		useStmt.setASTField(ASTUseStmtNode.TUSE, ASTToken.with(useKeyword));
		useStmt.setASTField(ASTUseStmtNode.TEOS, ASTToken.with(eos));

		parsingCtxt.pushValueStack(useStmt);
	}


	@Override
	public void data_stmt(Token label, Token keyword, Token eos, int count) {
		parsingCtxt.popAllValueStack(new WhileTypeValidator(ASTVariableNode.class));
		parsingCtxt.pushValueStack(new ASTNullNode());  // counts as a declaration_construct in specification_part(...)
	}

	@Override
	public void save_stmt(Token label, Token keyword, Token eos, boolean hasSavedEntityList) {
		parsingCtxt.pushValueStack(new ASTNullNode());  // counts as a declaration_construct in specification_part(...)
	}


	@Override
	public void declaration_type_spec(Token udtKeyword, int type) {
		IASTNode top = parsingCtxt.topValueStack();
		if (top instanceof ASTDataRefNode) {
			parsingCtxt.popValueStack();
		}
	}

	@Override
	public void type_declaration_stmt(Token label, int numAttributes, Token eos) {
		ASTTypeDeclarationStmtNode typeDecl = new ASTTypeDeclarationStmtNode();
		
		typeDecl.setLabel(ASTToken.with(label));
		typeDecl.setASTField(ASTTypeDeclarationStmtNode.TEOS, ASTToken.with(eos));

		IASTListNode<ASTEntityDeclNode> typeMembers = (IASTListNode<ASTEntityDeclNode>) parsingCtxt.popValueStack();
		typeDecl.setEntityDeclList( typeMembers);
		typeMembers.setParent(typeDecl);
		for (int i=0; i < numAttributes; i++ ) {
			ASTAttrSpecSeqNode attrSpecSeq  = new ASTAttrSpecSeqNode();
			attrSpecSeq.setAttrSpec((ASTAttrSpecNode) parsingCtxt.popValueStack());
			typeDecl.getAttrSpecSeq().add( attrSpecSeq);
		}
		typeDecl.setTypeSpec( (ASTTypeSpecNode)parsingCtxt.popValueStack());

		parsingCtxt.pushValueStack(typeDecl);
	}

	@Override
	public void derived_type_spec(Token typeName, boolean hasTypeParamSpecList) {
		ASTTypeSpecNode typeSpec = new ASTTypeSpecNode();
		typeSpec.setTypeName(ASTToken.with(typeName));
		parsingCtxt.pushValueStack(typeSpec);
	}

	@Override
	public void intrinsic_type_spec(Token keyword1, Token keyword2, int type, boolean hasKindSelector) {
		ASTTypeSpecNode typeSpec = new ASTTypeSpecNode();
		if (hasKindSelector) {
			parsingCtxt.popAllValueStack(new WhileTypeValidator(ASTDataRefNode.class));
		}
		typeSpec.setTypeName(ASTToken.with(keyword1));
		parsingCtxt.pushValueStack(typeSpec);
	}

	@Override
	public void derived_type_stmt(Token label, Token keyword, Token id, Token eos, boolean hasTypeAttrSpecList, boolean hasGenericNameList) {
		ASTDerivedTypeStmtNode typeStmt = new ASTDerivedTypeStmtNode();
		typeStmt.setLabel(ASTToken.with(label));
		typeStmt.setTypeName(ASTToken.with(id));
		
		parsingCtxt.pushValueStack(typeStmt);
	}

	@Override
	public void derived_type_def() {
		ASTDerivedTypeDefNode derivedType = new ASTDerivedTypeDefNode();
		// ignore everything in the derived_type_def 
		parsingCtxt.popAllValueStack( new UntilTypeValidator(ASTDerivedTypeStmtNode.class));
		derivedType.setDerivedTypeStmt((ASTDerivedTypeStmtNode) parsingCtxt.popValueStack());
		parsingCtxt.pushValueStack(derivedType);
	}


	@Override
	public void initialization(boolean hasExpr, boolean hasNullInit) {
		// for now pruning expressions
		if (hasExpr) {
			parsingCtxt.popAllValueStack(new WhileTypeValidator(ASTDataRefNode.class));
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
	public void execution_part_construct(boolean is_executable_construct) {
		if (! is_executable_construct) {
			parsingCtxt.valueStackPush(new ASTNullNode());
		}
	}
*/

	@Override
	public void execution_part(int execution_part_count) {
		IASTListNode<IASTNode> exec_parts = new ASTListNode<>();
		// grab everything that could appear in a body and make a list of it
		// because we do not built full AST, execution_part_count is not useful
		// there are typically more elements in the context stack than statements as indicated by execution_part_count
		exec_parts.addAll(
				parsingCtxt.popAllValueStack( new Validator() {
					@Override
					public boolean validate(IASTNode node) {
						return (
								(node instanceof ASTCallStmtNode) ||
								(node instanceof ASTAllocateStmtNode) ||
								(node instanceof ASTDeallocateStmtNode) ||
								(node instanceof ASTToken) ||
								(node instanceof ASTVariableNode) ||
								(node instanceof ASTDataRefNode) ||
								(node instanceof ASTAssignmentStmtNode)
								);
					}
				})
			);

		parsingCtxt.pushValueStack(exec_parts);
	}


	@Override
	public void entity_decl(Token id, boolean hasArraySpec, boolean hasCoarraySpec, boolean hasCharLength, boolean hasInitialization) {
		ASTEntityDeclNode entityDecl = new ASTEntityDeclNode();
		entityDecl.setObjectName(ASTToken.with(id));
		if (hasArraySpec) {
			parsingCtxt.popAllValueStack( new WhileTypeValidator(ASTDataRefNode.class));
		}
		parsingCtxt.pushValueStack( entityDecl);
	}

	@Override
	public void entity_decl_list(int count) {
		// should be IASTListNode<ASTEntityDeclNode> but this gives a compilation error in Eclipse for incompatible type ?!?!
		IASTListNode<IASTNode> declList = parsingCtxt.popAllValueStack( new CountValidator(count));
		parsingCtxt.pushValueStack( declList);
	}


	@Override
	public void attr_spec(Token attrKeyword, int attr) {
		ASTAttrSpecNode attrSpec = new ASTAttrSpecNode();
		ASTToken tk = null;

		if (attrKeyword != null) {
			tk = ASTToken.with(attrKeyword);
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

		// "parsing water": ignore anything that is not ASTTypeSpecNode or ASTAttrSpecNode
		parsingCtxt.popAllValueStack(new Validator() {
			public boolean validate(IASTNode node) {
				return ! (node instanceof ASTAttrSpecNode) && ! (node instanceof ASTTypeSpecNode) ;
			}
		});
		parsingCtxt.pushValueStack(attrSpec);
	}

	@Override
	public void access_stmt(Token label, Token eos, boolean hasList) {
		ASTAccessStmtNode accessStmt = new ASTAccessStmtNode();
		accessStmt.setLabel(ASTToken.with(label));
		accessStmt.setASTField(ASTAccessStmtNode.T_EOS,ASTToken.with(eos));
		
		accessStmt.setAccessSpec((ASTAccessSpecNode) parsingCtxt.popValueStack());
		
		parsingCtxt.pushValueStack(accessStmt);
	}


	@Override
	public void intent_spec(Token intentKeyword1, Token intentKeyword2, int intent) {
		ASTIntentSpecNode intent_spec = new ASTIntentSpecNode();
		switch (intent) {
		case IActionEnums.IntentSpec_IN: intent_spec.setIsIntentIn(ASTToken.with(intentKeyword1)); break;
		case IActionEnums.IntentSpec_OUT: intent_spec.setIsIntentOut(ASTToken.with(intentKeyword1)); break;
		case IActionEnums.IntentSpec_INOUT: intent_spec.setIsIntentInOut(ASTToken.with(intentKeyword1)); break;
		default:
			System.err.println("Unknown intent_spec:"+intent+"("+intentKeyword1+")");
		}
	}


	@Override
	public void array_spec_element(int type) {
		if ((type == IActionEnums.ArraySpecElement_expr) ||
				(type == IActionEnums.ArraySpecElement_expr_colon) ||
				(type == IActionEnums.ArraySpecElement_expr_colon_expr) ||
				(type == IActionEnums.ArraySpecElement_expr_colon_asterisk) ) {
			parsingCtxt.popAllValueStack(new WhileTypeValidator(ASTDataRefNode.class));				
		}
	}

	@Override
	public void access_spec(Token keyword, int type) {
		ASTAccessSpecNode accessSpec = new ASTAccessSpecNode();
		switch (type) {
		case IActionEnums.AttrSpec_PUBLIC: accessSpec.setIsPublic(ASTToken.with(keyword)); break;
		case IActionEnums.AttrSpec_PRIVATE: accessSpec.setIsPrivate(ASTToken.with(keyword)); break;
		default:
			System.err.println("Unknown access_spec:"+type+"("+keyword+")");
		}
		parsingCtxt.pushValueStack(accessSpec);
	}

	@Override
	public void call_stmt(Token label, Token callKeyword, Token eos, boolean hasActualArgSpecList) {
		ASTCallStmtNode call = new ASTCallStmtNode();
		
		call.setLabel( ASTToken.with(label));
		call.setASTField( ASTCallStmtNode.TCALL, ASTToken.with(callKeyword));
		// ofp grammar says procedure name can be a DataRef (thus can have TypeComponentSelector)
		// but other grammars consider it can only be a token
		// -> go with token for now
		ASTDataRefNode procName = (ASTDataRefNode) parsingCtxt.popValueStack(); 
		call.setSubroutineName( procName.getName() );
		call.setASTField( ASTCallStmtNode.TEOS, ASTToken.with(eos));

		parsingCtxt.pushValueStack(call);
	}

	
	@Override
	public void allocate_stmt(Token label, Token allocateKeyword, Token eos, boolean hasTypeSpec, boolean hasAllocOptList) {
		ASTAllocateStmtNode alloc = new ASTAllocateStmtNode();
		
		alloc.setLabel( ASTToken.with(label));
		alloc.setASTField( ASTAllocateStmtNode.TALLOC, ASTToken.with(allocateKeyword));
		alloc.setASTField( ASTCallStmtNode.TEOS, ASTToken.with(eos));
		if (hasAllocOptList) {
			alloc.setStatusVariable((ASTVariableNode) parsingCtxt.popValueStack());
		}
		alloc.setAllocationList( (IASTListNode<ASTAllocationNode>) parsingCtxt.popValueStack());
		
		parsingCtxt.pushValueStack(alloc);
	}

	@Override
	public void alloc_opt(Token allocOpt) {

	}

	@Override
	public void alloc_opt_list__begin() {
		parsingCtxt.pushValueStack(new ASTWaterExprNode());
		// used as a marker to delimit possible expression
		// see void alloc_opt_list(int count)
	}

	@Override
	public void alloc_opt_list(int count) {
		IASTListNode<IASTNode> water = parsingCtxt.popAllValueStack(new UntilTypeValidator(ASTWaterExprNode.class));
		((ASTWaterExprNode)parsingCtxt.topValueStack()).setExprMembers(water);
	}

	@Override
	public void allocation(boolean hasAllocateShapeSpecList, boolean hasAllocateCoarraySpec) {
		// TODO Auto-generated method stub
		super.allocation(hasAllocateShapeSpecList, hasAllocateCoarraySpec);
	}

	@Override
	public void allocation_list(int count) {
		super.allocation_list(count);
	}


	@Override
	public void deallocate_stmt(Token label, Token deallocateKeyword, Token eos, boolean hasDeallocOptList) {
		ASTDeallocateStmtNode dealloc = new ASTDeallocateStmtNode();
		
		dealloc.setLabel( ASTToken.with(label));
		dealloc.setASTField( ASTDeallocateStmtNode.TDEALLOC, ASTToken.with(deallocateKeyword));
		dealloc.setASTField( ASTCallStmtNode.TEOS, ASTToken.with(eos));
		
		parsingCtxt.pushValueStack(dealloc);
	}


	@Override
	public void dealloc_opt(Token id) {
		// TODO Auto-generated method stub
		super.dealloc_opt(id);
	}

	@Override
	public void dealloc_opt_list__begin() {
		// TODO Auto-generated method stub
		super.dealloc_opt_list__begin();
	}

	@Override
	public void dealloc_opt_list(int count) {
		// TODO Auto-generated method stub
		super.dealloc_opt_list(count);
	}

	@Override
	public void designator_or_func_ref() {
		//ASTDataRefNode ref = new ASTDataRefNode();
		assert(parsingCtxt.topValueStack() instanceof ASTDataRefNode);
		//ref.setName( (ASTToken) parsingCtxt.popValueStack());
		
		//parsingCtxt.pushValueStack(ref);
	}
	
	@Override
	public void data_ref(int numPartRefs) {
		ASTDataRefNode dataRef = new ASTDataRefNode();
		dataRef.setName((ASTToken) parsingCtxt.popValueStack());

		for (int i=1; i < numPartRefs; i++) {
			ASTDataRefNode fieldSelector = dataRef;
			dataRef = new ASTDataRefNode();
			dataRef.setName((ASTToken) parsingCtxt.popValueStack());
			dataRef.setComponentName(fieldSelector);
		}
		parsingCtxt.pushValueStack(dataRef);
	}


	@Override
	public void part_ref(Token id, boolean hasSectionSubscriptList, boolean hasImageSelector) {
		parsingCtxt.pushValueStack(ASTToken.with(id));
	}


	@Override
	public void variable() {
		ASTVariableNode var = new ASTVariableNode();
		var.setDataRef( (ASTDataRefNode) parsingCtxt.popValueStack());
		parsingCtxt.pushValueStack(var);
	}


	@Override
	public void assignment_stmt(Token label, Token eos) {
		ASTAssignmentStmtNode assign = new ASTAssignmentStmtNode();
		IASTListNode<IASTNode> members = parsingCtxt.popAllValueStack(new UntilTypeValidator(ASTVariableNode.class));
		ASTWaterExprNode exprMembers = new ASTWaterExprNode();

		assign.setLhsVariable((ASTVariableNode) parsingCtxt.popValueStack());
		exprMembers.setExprMembers(members);
		assign.setRhs((IExpr) exprMembers);

		parsingCtxt.pushValueStack(assign);
	}

	// UTILITIES ---


	/**
	 * Pops the body of a subprogram/program and put it in a IASTListNode
	 * <p>
	 * If hasExecutionPart, then the stack contains ExecutionPart+SpecificationPart
	 * otherwise, only SpecificationPart
	 */
	protected IASTListNode<IBodyConstruct> popBodyAsList(boolean hasExecutionPart) {
		IASTListNode<IBodyConstruct> specifications = (IASTListNode<IBodyConstruct>) parsingCtxt.popValueStack();
		if (hasExecutionPart) {
			specifications.addAll((Collection<IBodyConstruct>) parsingCtxt.popValueStack());
		}
		return specifications;
	}


	/**
	 * Trace error and try recovering by going back to previous Subprogram/Module/... entity
	 */
	protected void traceSubprogramErrorAndRecover(IASTNode lastToken) {
		parsingCtxt.popAllValueStack(new UntilTopEntityValidator());
		System.err.println("Parsing error after "+ lastToken+", ignoring all since  " + parsingCtxt.topValueStack());
	}

}
