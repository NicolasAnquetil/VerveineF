package fr.inria.verveine.extractor.fortran.ast;

import java.util.List;

/* Dumby  class
 * Could not find the error class in the depot. Only .class could be found */
public class ErrorRecoveryInfo {

	public ASTToken errorLookahead;
	public Object errorState;
	public Object expectedLookaheadSymbols;


	public ErrorRecoveryInfo(Object errorState, ASTToken errorLookahead, Object expectedLookaheadSymbols) {
		ErrorRecoveryInfo newErrorRecoveryInfo = new ErrorRecoveryInfo();
		newErrorRecoveryInfo.errorState = errorState;
		newErrorRecoveryInfo.errorLookahead = errorLookahead;
		newErrorRecoveryInfo.expectedLookaheadSymbols = expectedLookaheadSymbols;
		
	}

	public ErrorRecoveryInfo() {
		// TODO Auto-generated constructor stub
	}

	public List<IASTNode> getDiscardedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	public String describeExpectedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

}
