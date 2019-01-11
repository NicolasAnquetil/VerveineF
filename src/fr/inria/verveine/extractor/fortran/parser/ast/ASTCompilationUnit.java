package fr.inria.verveine.extractor.fortran.parser.ast;

public class ASTCompilationUnit extends ASTNode {

	public static final int BODY = 0;

	protected String filename;
	
    IASTListNode<IASTNode> compilationUnitBody;

	public ASTCompilationUnit(String filename) {
		this.filename = filename;
	}

	@Override
	public void accept(IASTVisitor visitor) {
		visitor.visitASTCompilationUnit(this);
//		visitor.visitASTNode(this);
	}

	public IASTListNode<IASTNode> getBody() {
		return this.compilationUnitBody;
	}

	public void setBody(IASTListNode<IASTNode> newValue) {
		this.compilationUnitBody = newValue;
		if (newValue != null)
			newValue.setParent(this);
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
        case BODY:  return this.compilationUnitBody;
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
        switch (index)
        {
        case BODY:  this.compilationUnitBody = (IASTListNode<IASTNode>) value;
        default: throw new IllegalArgumentException("Invalid index");
        }
		
	}

	@Override
	public String toString() {
    	return "ASTCompilationUnit(" + filename + ")";
	}

	@Override
	public boolean isTopLevelNode() {
		return true;
	}
    
}
