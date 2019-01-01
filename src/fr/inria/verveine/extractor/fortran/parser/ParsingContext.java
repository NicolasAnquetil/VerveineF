package fr.inria.verveine.extractor.fortran.parser;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTListNode;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTListNode;
import fr.inria.verveine.extractor.fortran.parser.ast.IASTNode;


/**
 * Stores intermediary results of the parser to build the AST<br>
 * Two ways to store these results:
 * <ul>
 * <li> A stack of values: {@link #pushValueStack(IASTNode)},  {@link #topValueStack()},  {@link #popValueStack()}<br>
 * 		Used by "upper" rules to pass info to their "inner" rules
 * <li> A dictionary of rule results: {@link #valueSet(String,Object)},  {@link #valueGetString()}<br>
 * 		Used by "lower" rules to pass info to their "owner" rules
 * </ul>
 * @author anquetil
 *
 */
public class ParsingContext {
	
	protected Stack<IASTNode> valueStack;
	protected int valueStackTop;

	protected Dictionary<String,Object> ruleValues;

	public ParsingContext() {
		valueStack = new Stack<>();
		ruleValues = new Hashtable<>();
	}

	public void pushValueStack(IASTNode node) {
		valueStack.push(node);
	}

	public IASTNode topValueStack() {
		return valueStack.peek();
	}

	public IASTNode popValueStack() {
		return valueStack.pop();
	}

	/**
	 * Helper method for island grammar parsing: Allows to pop many entries from the parsingContext valuesStack
	 */
	public IASTListNode<IASTNode> popAllValueStack(Validator valid) {
		IASTListNode<IASTNode> poped = new ASTListNode<>();

		IASTNode topNode = topValueStack();
		while ( valid.validate(topNode) ) {
			poped.add( topNode);
			popValueStack();
			topNode = topValueStack();
		}
		return poped;
	}

	public void valueSet(String key, Object value) {
		ruleValues.put(key, value);
	}

	public Object valueGet(String key) {
		return ruleValues.get(key);
	}

	/**
	 * {@link #valueGet(String)} + {@link #valueClear(String)}
	 */
	public Object valueRetreive(String key) {
		Object tmp = valueGet(key);
		valueClear(key);
		return tmp;
	}

	public void valueClear(String key) {
		ruleValues.remove(key);
	}
	
}