package fr.inria.verveine.extractor.fortran.ast;

import java.util.HashSet;
import java.util.Set;

/**
 * A temporary NULL ASTNode while we did not implement all the classes needed for parsing completly Fortran
 * A ASTNullNode is created each time we need to return an ASTNode but it was not already implemented
 * {@link ASTNode#getASTField(int)}
 */
public class ASTNullNode extends ASTNode {

	@Override
	protected int getNumASTFields() {
		return 0;
	}

	@Override
	protected IASTNode getASTField(int index) {
		return null;
	}

	@Override
	public void accept(IASTVisitor visitor) {
	}

	@Override
	public <T extends IASTNode> Set<T> findAll(Class<T> targetClass) {
		return new HashSet<T>();
	}

	@Override
	public <T extends IASTNode> T findNearestAncestor(Class<T> targetClass) {
		return (T) this;
	}

	@Override
	public <T extends IASTNode> T findFirst(Class<T> targetClass) {
		return (T) this;
	}

	@Override
	public <T extends IASTNode> T findLast(Class<T> targetClass) {
		return (T) this;
	}

	@Override
	public boolean isFirstChildInList() {
		return false;
	}

	@Override
	protected void setASTField(int index, IASTNode value) {
	}

}
