package fr.inria.verveine.extractor.fortran.ast;

public class ASTCompilationUnit extends ASTNode {

	protected String filename;
	
    IASTListNode<IASTNode> compilationUnitBody;

	public ASTCompilationUnit(String filename) {
		this.filename = filename;
	}

	public void add(IASTNode node) {
		compilationUnitBody.add(node);
	}

	public void addAll(IASTListNode<IASTNode> list) {
		compilationUnitBody.addAll( list);
	}

	@Override
	public void accept(IASTVisitor visitor) {
		visitor.visitASTCompilationUnit(this);
		visitor.visitASTNode(this);
	}

    @Override 
    protected int getNumASTFields()
    {
        return 1;
    }

    @Override 
    protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.compilationUnitBody;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

	@Override
	public String fullyQualifiedName() {
		return "["+basename()+"]";
	}

	@Override
	public String basename() {
		return filename;
	}

	@Override
	protected void setASTField(int index, IASTNode value) {
		
	}

}
