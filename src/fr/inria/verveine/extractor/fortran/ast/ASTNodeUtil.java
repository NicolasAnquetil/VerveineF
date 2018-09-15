package fr.inria.verveine.extractor.fortran.ast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ASTNodeUtil {

	private ASTNodeUtil() {}

    public static <T extends IASTNode> Set<T> findAll(IASTNode node, final Class<T> clazz)
    {
        class V extends ASTVisitor
        {
            Set<T> result = new HashSet<T>();

            @Override public void visitASTNode(ASTNode node)
            {
                if (clazz.isAssignableFrom(node.getClass()))
                    result.add((T)node);
                traverseChildren(node);
            }

            @Override public void visitToken(ASTToken node)
            {
                if (clazz.isAssignableFrom(node.getClass()))
                    result.add((T)node);
            }

        };

        V v = new V();
        node.accept(v);
        return v.result;
    }

	protected static final class Notification extends Error
	{
		private static final long serialVersionUID = 1L;

		private Object result;

		public Notification(Object result) { this.result = result; }

		public Object getResult() { return result; }
	}

    @SuppressWarnings("unchecked")
    public static <T extends IASTNode> T findNearestAncestor(IASTNode node, Class<T> targetClass)
    {
        for (IASTNode parent = node.getParent(); parent != null; parent = parent.getParent())
            if (targetClass.isAssignableFrom(parent.getClass()))
                return (T)parent;
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IASTNode> T findFirst(IASTNode node, final Class<T> clazz)
    {
        try
        {
            node.accept(new ASTVisitor()
            {
                @Override
                public void visitASTNode(ASTNode node)
                {
                    if (clazz.isAssignableFrom(node.getClass()))
                        throw new Notification(node);
                    traverseChildren(node);
                }

                @Override public void visitToken(ASTToken node)
                {
                    if (clazz.isAssignableFrom(node.getClass()))
                        throw new Notification(node);
                }

            });
            return null;
        }
        catch (Notification n)
        {
            return (T)n.getResult();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IASTNode> T findLast(IASTNode node, final Class<T> clazz)
    {
        class FindLastVisitor extends ASTVisitor
        {
            T result = null;

            @Override public void visitASTNode(ASTNode node)
            {
                if (clazz.isAssignableFrom(node.getClass()))
                    result = (T)node;
                traverseChildren(node);
            }

            @Override public void visitToken(ASTToken node)
            {
                if (clazz.isAssignableFrom(node.getClass()))
                    result = (T)node;
            }
        };

        FindLastVisitor v = new FindLastVisitor();
        node.accept(v);
        return v.result;
    }

    public static ASTToken findFirstToken(IASTNode node)
    {
        return findFirst(node, ASTToken.class);
    }

    public static ASTToken findLastToken(IASTNode node)
    {
        return findLast(node, ASTToken.class);
    }


	public static boolean isFirstChildInList(IASTNode astToken)
	{
		return astToken.getParent() != null
				&& astToken.getParent() instanceof ASTListNode
				&& ((ASTListNode<?>)astToken.getParent()).size() > 0
				&& ((ASTListNode<?>)astToken.getParent()).get(0) == astToken;
	}

	///////////////////////////////////////////////////////////////////////////
	// Utility Classes
	///////////////////////////////////////////////////////////////////////////

	public static final class NonNullIterator<T> implements Iterator<T>
	{
		private Iterator<T> wrappedIterator;
		private T next;

		public NonNullIterator(Iterator<T> wrappedIterator)
		{
			this.wrappedIterator = wrappedIterator;
			findNext();
		}

		private void findNext()
		{
			do
			{
				if (!this.wrappedIterator.hasNext())
				{
					this.next = null;
					return;
				}

				this.next = this.wrappedIterator.next();
			}
			while (this.next == null);
		}

		public boolean hasNext()
		{
			return this.next != null;
		}

		public T next()
		{
			T result = this.next;
			findNext();
			return result;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}

