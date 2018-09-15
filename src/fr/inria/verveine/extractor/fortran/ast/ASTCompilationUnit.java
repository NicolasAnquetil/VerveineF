package fr.inria.verveine.extractor.fortran.ast;

public class ASTCompilationUnit extends ASTNode {

	protected String filename;
	
	protected IASTNode programUnit;

	public ASTCompilationUnit(String filename) {
		this.filename = filename;
	}

	public IASTNode getProgramUnit() {
		return programUnit;
	}

	public void setProgramUnit(IASTNode node) {
		programUnit = node;
		if (programUnit != null) {
			node.setParent(this);
		}
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
        case 0:  return this.programUnit;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override 
    protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.programUnit = (IASTNode)value; if (value != null) value.setParent(this); return;
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

}
