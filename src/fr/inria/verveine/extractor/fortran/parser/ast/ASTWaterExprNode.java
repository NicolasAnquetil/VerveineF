package fr.inria.verveine.extractor.fortran.parser.ast;

/**
 * An Expression node that gather a list of referred names without really caring how they are referred or what they are.
 * <p>
 * This ultimately should be replaced by a full AST of expressions
 * @author anquetil
 */
public class ASTWaterExprNode extends ASTNode implements IExpr {
	IASTListNode<IASTNode> exprMembers;
	
	@Override
	protected int getNumASTFields() {
		return 1;
	}

    public IASTListNode<IASTNode> getExprMembers()
    {
        return this.exprMembers;
    }

    public void setExprMembers(IASTListNode<IASTNode> newValue)
    {
        this.exprMembers = newValue;
        if (newValue != null) newValue.setParent(this);
    }

	@Override
	protected IASTNode getASTField(int index) {
        switch (index)
        {
        case 0:  return this.exprMembers;
        default: throw new IllegalArgumentException("Invalid index");
        }
	}

	@Override
	protected void setASTField(int index, IASTNode value) {
        switch (index)
        {
        case 0:  this.exprMembers = (IASTListNode<IASTNode>) value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
	}

	@Override
	public void accept(IASTVisitor visitor) {
        visitor.visitASTWaterExprNode(this);
	}

}
