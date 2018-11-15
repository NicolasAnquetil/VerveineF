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
	public void visitASTProgramNameNode(ASTProgramNameNode node);
	public void visitASTEndProgramStmtNode(ASTEndProgramStmtNode node);
	public void visitASTModuleStmtNode(ASTModuleStmtNode node);
	public void visitASTModuleNameNode(ASTModuleNameNode node);
	public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node);
	public void visitASTFunctionStmtNode(ASTFunctionStmtNode node);
	public void visitASTFunctionNameNode(ASTFunctionNameNode node);
	public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node);
	public void visitASTSubroutineNameNode(ASTSubroutineNameNode node);
	public void visitASTSubroutineStmtNode(ASTSubroutineStmtNode node);
	public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node);
	public void visitASTInterfaceBodyNode(ASTInterfaceBodyNode node);
	public void visitASTObjectNameNode(ASTObjectNameNode node);
	public void visitASTDeferredShapeSpecListNode(ASTDeferredShapeSpecListNode node);
	public void visitASTArrayAllocationNode(ASTArrayAllocationNode node);
	public void visitASTAllocatableStmtNode(ASTAllocatableStmtNode node);
	public void visitISpecificationStmt(ASTAllocatableStmtNode node);
	public void visitASTDeferredShapeSpecNode(ASTDeferredShapeSpecNode node);
	public void visitASTVariableNameNode(ASTVariableNameNode node);
	public void visitIBindEntity(ASTVariableNameNode node);
	public void visitASTSubscriptTripletNode(ASTSubscriptTripletNode node);
	public void visitASTSectionSubscriptNode(ASTSectionSubscriptNode node);
	public void visitASTImageSelectorNode(ASTImageSelectorNode node);
	public void visitASTFieldSelectorNode(ASTFieldSelectorNode node);
	public void visitASTAllocationNode(ASTAllocationNode node);
	public void visitASTVariableNode(ASTVariableNode node);
	public void visitIDataStmtObject(ASTVariableNode node);
	public void visitIInputItem(ASTVariableNode node);
	public void visitASTSubstringRangeNode(ASTSubstringRangeNode node);
	public void visitASTDataRefNode(ASTDataRefNode node);
	public void visitASTNameNode(ASTNameNode node);
	public void visitASTAllocateStmtNode(ASTAllocateStmtNode node);
	public void visitASTAllocateObjectNode(ASTAllocateObjectNode node);
	public void visitASTAllocateCoarraySpecNode(ASTAllocateCoarraySpecNode node);
	public void visitASTAssociateStmtNode(ASTAssociateStmtNode node);
	public void visitASTAssociationNode(ASTAssociationNode node);

}
