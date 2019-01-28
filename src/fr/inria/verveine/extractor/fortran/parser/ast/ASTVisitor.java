package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTVisitor implements IASTVisitor {

	private void traverseNodeList( Iterable<? extends IASTNode> nodes) {
		for (IASTNode n : nodes) {
			n.accept(this);
		}
	}

    protected void traverseChildren(IASTNode node) {
    	traverseNodeList( node.getChildren());
    }

    @Override public void visitASTNode(ASTNode node) {
    	this.traverseChildren(node);
    }
    @Override public <T extends IASTNode> void visitASTListNode(ASTListNode<T> astListNode) {
    	traverseNodeList(astListNode);
    }
    @Override public void visitToken(ASTToken node) { }

    @Override public void visitIActionStmt(IActionStmt node) { }
    @Override public void visitIBlockDataBodyConstruct(IBlockDataBodyConstruct node) { }
    @Override public void visitIBodyConstruct(IBodyConstruct node) { }
    @Override public void visitIDeclarationConstruct(IDeclarationConstruct node) { }
    @Override public void visitIExecutableConstruct(IExecutableConstruct node) { }
    @Override public void visitIHPField(IHPField node) { }
    @Override public void visitIInterfaceSpecification(IInterfaceSpecification node) { }
    @Override public void visitIInternalSubprogram(IInternalSubprogram node) { }
    @Override public void visitIModuleBodyConstruct(IModuleBodyConstruct node) { }
    @Override public void visitIModuleSubprogram(IModuleSubprogram node) { }
    @Override public void visitIModuleSubprogramPartConstruct(IModuleSubprogramPartConstruct node) { }
    @Override public void visitIProgramUnit(IProgramUnit node) { }
    @Override public void visitISpecificationPartConstruct(ISpecificationPartConstruct node) { }

	@Override public void visitASTAccessSpecNode(ASTAccessSpecNode node) { visitASTNode(node); }
	@Override public void visitASTAccessStmtNode(ASTAccessStmtNode node) { visitASTNode(node); }
	@Override public void visitASTAttrSpecNode(ASTAttrSpecNode node) { visitASTNode(node); }
	@Override public void visitASTAttrSpecSeqNode(ASTAttrSpecSeqNode node) { visitASTNode(node); }
	@Override public void visitASTBlockDataSubprogramNode(ASTBlockDataSubprogramNode node) { visitASTNode(node); }
	@Override public void visitASTCallStmtNode(ASTCallStmtNode node) { visitASTNode(node); }
	@Override public void visitASTCaseConstructNode(ASTCaseConstructNode node) { visitASTNode(node); }
	@Override public void visitASTCaseStmtNode(ASTCaseStmtNode node) { visitASTNode(node); }
	@Override public void visitASTCompilationUnit(ASTCompilationUnit node) { visitASTNode(node); }
	@Override public void visitASTDerivedTypeDefNode(ASTDerivedTypeDefNode node) { visitASTNode(node); }
	@Override public void visitASTDerivedTypeStmtNode(ASTDerivedTypeStmtNode node) { visitASTNode(node); }
	@Override public void visitASTElseIfConstructNode(ASTElseIfConstructNode node) { visitASTNode(node); }
	@Override public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node) { visitASTNode(node); }
	@Override public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node) { visitASTNode(node); }
	@Override public void visitASTEndProgramStmtNode(ASTEndProgramStmtNode node) { visitASTNode(node); }
	@Override public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node) { visitASTNode(node); }
	@Override public void visitASTEntityDeclNode(ASTEntityDeclNode node) { visitASTNode(node); }
	@Override public void visitASTFunctionArgListNode(ASTFunctionArgListNode node) { visitASTNode(node); }
	@Override public void visitASTFunctionStmtNode(ASTFunctionStmtNode node) { visitASTNode(node); }
	@Override public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) { visitASTNode(node); }
	@Override public void visitASTIfConstructNode(ASTIfConstructNode node) { visitASTNode(node); }
	@Override public void visitASTIfStmtNode(ASTIfStmtNode node) { visitASTNode(node); }
	@Override public void visitASTIntentSpecNode(ASTIntentSpecNode node) { visitASTNode(node); }
	@Override public void visitASTInterfaceBodyNode(ASTInterfaceBodyNode node) { visitASTNode(node); }
	@Override public void visitASTMainProgramNode(ASTMainProgramNode node) { visitASTNode(node); }
	@Override public void visitASTModuleNode(ASTModuleNode node) { visitASTNode(node); }
	@Override public void visitASTModuleStmtNode(ASTModuleStmtNode node) { visitASTNode(node); }
	@Override public void visitASTProgramStmtNode(ASTProgramStmtNode node) { visitASTNode(node); }
	@Override public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node) { visitASTNode(node); }
	@Override public void visitASTSeparateModuleSubprogramNode(ASTSeparateModuleSubprogramNode node) { visitASTNode(node); }
	@Override public void visitASTSubmoduleNode(ASTSubmoduleNode node) { visitASTNode(node); }
	@Override public void visitASTSubroutineStmtNode(ASTSubroutineStmtNode node) { visitASTNode(node); }
	@Override public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) { visitASTNode(node); }
	@Override public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) { visitASTNode(node); }
	@Override public void visitASTUseStmtNode(ASTUseStmtNode node) { visitASTNode(node); }
	@Override public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) { visitASTNode(node); }
	@Override public void visitASTTypeSpecNode(ASTTypeSpecNode node) { visitASTNode(node);	}
	@Override public void visitASTAssignmentStmtNode(ASTAssignmentStmtNode node) { visitASTNode(node); }
	@Override public void visitASTVariableNameNode(ASTVariableNameNode node) { visitASTNode(node); }
	@Override public void visitASTWaterExprNode(ASTWaterExprNode node) { visitASTNode(node); }
	@Override public void visitASTAllocateStmtNode(ASTAllocateStmtNode node) { visitASTNode(node); }
	@Override public void visitASTDeallocateStmtNode(ASTDeallocateStmtNode node) { visitASTNode(node); }

}
