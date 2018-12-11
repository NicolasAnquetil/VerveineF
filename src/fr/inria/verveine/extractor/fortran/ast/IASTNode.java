package fr.inria.verveine.extractor.fortran.ast;

import java.util.Set;

public interface IASTNode {

	public IASTNode getParent();

	public void setParent(IASTNode parent);

	public Iterable<? extends IASTNode> getChildren();

	public String fullyQualifiedName();

	public void accept(IASTVisitor visitor);

	<T extends IASTNode> Set<T> findAll(Class<T> targetClass);

	<T extends IASTNode> T findNearestAncestor(Class<T> targetClass);

	<T extends IASTNode> T findFirst(Class<T> targetClass);

	<T extends IASTNode> T findLast(Class<T> targetClass);

	ASTToken findFirstToken();

	ASTToken findLastToken();

	boolean isFirstChildInList();

    public boolean isNullNode();

    public boolean isTopLevelNode();

}
