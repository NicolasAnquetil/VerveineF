package fr.inria.verveine.extractor.fortran.parser.ast;


import java.util.Iterator;
import java.util.Set;

public abstract class ASTNode implements IASTNode {
	private IASTNode parent = null;

    @Override
	public IASTNode getParent()
	{
		return this.parent;
	}

    @Override
	public void setParent(IASTNode parent)
	{
		this.parent = parent;
	}

    @Override
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

    @Override
	public abstract void accept(IASTVisitor visitor);

    @Override
	public String fullyQualifiedName() {
		return getParent().fullyQualifiedName() + "." + basename();
	}

	public String basename() {
		return "";
	}

    @Override
    public <T extends IASTNode> Set<T> findAll(Class<T> targetClass)
    {
        return ASTNodeUtil.findAll(this, targetClass);
    }

    @Override
    public <T extends IASTNode> T findNearestAncestor(Class<T> targetClass)
    {
        return ASTNodeUtil.findNearestAncestor(this, targetClass);
    }

    @Override
    public <T extends IASTNode> T findFirst(Class<T> targetClass)
    {
        return ASTNodeUtil.findFirst(this, targetClass);
    }

    @Override
    public <T extends IASTNode> T findLast(Class<T> targetClass)
    {
        return ASTNodeUtil.findLast(this, targetClass);
    }

    @Override
    public ASTToken findFirstToken()
    {
        return ASTNodeUtil.findFirstToken(this);
    }

    @Override
    public ASTToken findLastToken()
    {
        return ASTNodeUtil.findLastToken(this);
    }

    @Override
   public boolean isFirstChildInList()
    {
        return ASTNodeUtil.isFirstChildInList(this);
    }

    @Override
    public boolean isNullNode() {
    	return false;
    }

    @Override
    public boolean isTopLevelNode() {
    	return false;
    }

}
