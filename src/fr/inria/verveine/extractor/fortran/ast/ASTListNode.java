package fr.inria.verveine.extractor.fortran.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ASTListNode<T extends IASTNode> extends ArrayList<T> implements IASTListNode<T>
{
	private static final long serialVersionUID = 1L;

	private IASTNode parent = null;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public ASTListNode()
    {
        super();
    }

    public ASTListNode(int initialCapacity)
    {
        super(initialCapacity);
    }

    public ASTListNode(T singletonElement)
    {
        super(1);
        add(singletonElement);
    }

    public ASTListNode(T... elements)
    {
        super(elements.length);
        for (T e : elements)
            add(e);
    }

    public ASTListNode(Collection<? extends T> copyFrom)
    {
        super(copyFrom);
    }

    public ASTListNode(T first, Collection<? extends T> rest)
    {
        super(rest.size()+1);
        add(first);
        addAll(rest);
    }

    public ASTListNode(Collection<? extends T> firsts, T last)
    {
        super(firsts.size()+1);
        addAll(firsts);
        add(last);
    }

    ///////////////////////////////////////////////////////////////////////////
    // IASTListNode Insertion Methods
    ///////////////////////////////////////////////////////////////////////////

    public void insertBefore(T insertBefore, T newElement)
    {
        int index = indexOf(insertBefore);
        if (index < 0)
            throw new IllegalArgumentException("Element to insert before not in list");
        add(index, newElement);
    }

    public void insertAfter(T insertAfter, T newElement)
    {
        int index = indexOf(insertAfter);
        if (index < 0)
            throw new IllegalArgumentException("Element to insert after not in list");
        add(index+1, newElement);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Traversal and Visitor Support
    ///////////////////////////////////////////////////////////////////////////

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
        return this;
    }

    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTListNode(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Searching
    ///////////////////////////////////////////////////////////////////////////

    public <T extends IASTNode> Set<T> findAll(Class<T> targetClass)
    {
        return ASTNodeUtil.findAll(this, targetClass);
    }

    @SuppressWarnings("hiding")
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

    ///////////////////////////////////////////////////////////////////////////
    // Source Manipulation
    ///////////////////////////////////////////////////////////////////////////

    // These methods are all inherited from ArrayList but are overridden to call #setParent
    @Override public T set(int index, T element) { if (element != null) element.setParent(this); return super.set(index, element); }
    @Override public boolean add(T element) { if (element != null) element.setParent(this); return super.add(element); }
    @Override public void add(int index, T element) { if (element != null) element.setParent(this); super.add(index, element); }
    @Override public boolean addAll(Collection<? extends T> c) { for (T element : c) if (element != null) element.setParent(this); return super.addAll(c); }
    @Override public boolean addAll(int index, Collection<? extends T> c) { for (T element : c) if (element != null) element.setParent(this); return super.addAll(index, c); }

	@Override
	public String fullyQualifiedName() {
		return getParent().fullyQualifiedName() + ".[]";
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