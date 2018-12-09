package fr.inria.verveine.extractor.fortran.ast;

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
    @Override public void visitToken(ASTToken node) {}

    @Override public void visitIProgramUnit(IProgramUnit node) {}
    @Override public void visitIInternalSubprogram(IInternalSubprogram node) {}
    @Override public void visitIModuleBodyConstruct(IModuleBodyConstruct node) {}
    @Override public void visitIModuleSubprogram(IModuleSubprogram node) {}
    @Override public void visitIModuleSubprogramPartConstruct(IModuleSubprogramPartConstruct node) {}
    @Override public void visitIExecutableConstruct(IExecutableConstruct node) {}
    @Override public void visitIActionStmt(IActionStmt node) {}
    @Override public void visitIBlockDataBodyConstruct(IBlockDataBodyConstruct node) {}
    @Override public void visitISpecificationPartConstruct(ISpecificationPartConstruct node) {}
    @Override public void visitIHPField(IHPField node) {}
    @Override public void visitIDeclarationConstruct(IDeclarationConstruct node) {}
    @Override public void visitIBodyConstruct(IBodyConstruct node) {}
    @Override public void visitIInterfaceSpecification(IInterfaceSpecification node) { }
	
	
	@Override public void visitASTCompilationUnit(ASTCompilationUnit node) {	}
	@Override public void visitASTMainProgramNode(ASTMainProgramNode node) {	}
	@Override public void visitASTModuleNode(ASTModuleNode node) {	}
	@Override public void visitASTFunctionSubprogramNode(ASTFunctionSubprogramNode node) {}
	@Override public void visitASTSubroutineSubprogramNode(ASTSubroutineSubprogramNode node) {}
	@Override public void visitASTIfConstructNode(ASTIfConstructNode node) {}
	@Override public void visitASTElseIfConstructNode(ASTElseIfConstructNode node) {}
	@Override public void visitASTIfStmtNode(ASTIfStmtNode node) {}
	@Override public void visitASTProperLoopConstructNode(ASTProperLoopConstructNode node) {}
	@Override public void visitASTCaseConstructNode(ASTCaseConstructNode node) {}
	@Override public void visitASTCaseStmtNode(ASTCaseStmtNode node) {}
	@Override public void visitASTTypeDeclarationStmtNode(ASTTypeDeclarationStmtNode node) {}
	@Override public void visitASTProgramStmtNode(ASTProgramStmtNode node) {}
	@Override public void visitASTAttrSpecSeqNode(ASTAttrSpecSeqNode astAttrSpecNode) {}
	@Override public void visitASTAttrSpecNode(ASTAttrSpecNode astAttrSpecNode) {}
	@Override public void visitASTEntityDeclNode(ASTEntityDeclNode node) {}
	@Override public void visitASTEndProgramStmtNode(ASTEndProgramStmtNode node) {}
	@Override public void visitASTModuleStmtNode(ASTModuleStmtNode node) {}
	@Override public void visitASTEndModuleStmtNode(ASTEndModuleStmtNode node) {}
	@Override public void visitASTFunctionStmtNode(ASTFunctionStmtNode node) {}
	@Override public void visitASTEndFunctionStmtNode(ASTEndFunctionStmtNode node) {}
	@Override public void visitASTSubroutineStmtNode(ASTSubroutineStmtNode node) {}
	@Override public void visitASTEndSubroutineStmtNode(ASTEndSubroutineStmtNode node) {}
	@Override public void visitASTInterfaceBodyNode(ASTInterfaceBodyNode node) { }
	@Override public void visitASTVarOrFnRefNode(ASTVarOrFnRefNode node) { }	
	@Override public void visitASTCallStmtNode(ASTCallStmtNode node) { }

}
