package fr.inria.verveine.extractor.fortran.ast;

public interface IASTVisitor {

	public void visitASTNode(ASTNode node);
	void visitToken(ASTToken node);
	public <T extends IASTNode> void visitASTListNode(ASTListNode<T> node);
	
	public void visitIProgramUnit(IProgramUnit node);
	public void visitIInternalSubprogram(IInternalSubprogram node);
	public void visitIModuleBodyConstruct(IModuleBodyConstruct node);
	public void visitIModuleSubprogram(IModuleSubprogram node);
	public void visitIModuleSubprogramPartConstruct(IModuleSubprogramPartConstruct node);
	public void visitIExecutableConstruct(IExecutableConstruct node);
	public void visitIActionStmt(IActionStmt node);
	public void visitIBlockDataBodyConstruct(IBlockDataBodyConstruct node);
	public void visitISpecificationPartConstruct(ISpecificationPartConstruct node);
	public void visitIHPField(IHPField node);
	public void visitIDeclarationConstruct(IDeclarationConstruct node);
	public void visitIBodyConstruct(IBodyConstruct node);
	public void visitIInterfaceSpecification(IInterfaceSpecification node);
	public void visitASTCompilationUnit(ASTCompilationUnit node);
	public void visitASTMainProgramNode(ASTMainProgramNode node);
	public void visitASTModuleNode(ASTModuleNode node);
	public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node);
	public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node);
	public void visitASTIfConstructNode(ASTIfConstructNode node);
	public void visitASTElseIfConstructNode(ASTElseIfConstructNode node);
	public void visitASTIfStmtNode(ASTIfStmtNode node);
	public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node);
	public void visitASTCaseConstructNode(ASTCaseConstructNode node);
	public void visitASTCaseStmtNode(ASTCaseStmtNode node);
	public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node);
	public void visitASTProgramStmtNode(ASTProgramStmtNode node);
	public void visitASTEntityDeclNode(ASTEntityDeclNode node);
	public void visitASTAttrSpecSeqNode(ASTAttrSpecSeqNode node);
	public void visitASTAttrSpecNode(ASTAttrSpecNode node);
	public void visitASTEndProgramStmtNode(ASTEndProgramStmtNode node);
	public void visitASTModuleStmtNode(ASTModuleStmtNode node);
	public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node);
	public void visitASTFunctionStmtNode(ASTFunctionStmtNode node);
	public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node);
	public void visitASTSubroutineStmtNode(ASTSubroutineStmtNode node);
	public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node);
	public void visitASTInterfaceBodyNode(ASTInterfaceBodyNode node);
	public void visitASTCallStmtNode(ASTCallStmtNode node);
	public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node);

}
