package fr.inria.verveine.extractor.fortran.ast;


import java.util.Iterator;
import java.util.Set;

public abstract class ASTNode implements IASTNode {
	private IASTNode parent = null;

	public IASTNode getParent()
	{
		return this.parent;
	}

	public void setParent(IASTNode parent)
	{
		this.parent = parent;
	}

	public Iterable<? extends IASTNode> getChildren()
	{
		return new Iterable<IASTNode>()
		{
			public Iterator<IASTNode> iterator()
			{
				return new ASTNodeUtil.NonNullIterator<IASTNode>(new Iterator<IASTNode>()
				{
					private int index = 0, numChildren = getNumASTFields();

					public boolean hasNext()
					{
						return index < numChildren;
					}

					public IASTNode next()
					{
						return getASTField(index++);
					}

					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				});
			}
		};
	}

    protected abstract int getNumASTFields();

    protected abstract IASTNode getASTField(int index);

    protected abstract void setASTField(int index, IASTNode value);

	public abstract void accept(IASTVisitor visitor);

	public String fullyQualifiedName() {
		return getParent().fullyQualifiedName() + "." + basename();
	}

	public String basename() {
		return "";
	}

    public <T extends IASTNode> Set<T> findAll(Class<T> targetClass)
    {
        return ASTNodeUtil.findAll(this, targetClass);
    }

    public <T extends IASTNode> T findNearestAncestor(Class<T> targetClass)
    {
        return ASTNodeUtil.findNearestAncestor(this, targetClass);
    }

    public <T extends IASTNode> T findFirst(Class<T> targetClass)
    {
        return ASTNodeUtil.findFirst(this, targetClass);
    }

    public <T extends IASTNode> T findLast(Class<T> targetClass)
    {
        return ASTNodeUtil.findLast(this, targetClass);
    }

    public ASTToken findFirstToken()
    {
        return ASTNodeUtil.findFirstToken(this);
    }

    public ASTToken findLastToken()
    {
        return ASTNodeUtil.findLastToken(this);
    }

    public boolean isFirstChildInList()
    {
        return ASTNodeUtil.isFirstChildInList(this);
    }

    public boolean isNullNode() {
    	return false;
    }

}
