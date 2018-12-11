package fr.inria.verveine.extractor.fortran.ast;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

import org.antlr.runtime.Token;

/**
 * Stores intermediary results of the parser to build the AST<br>
 * Two ways to store these results:
 * <ul>
 * <li> A stack of values: {@link #pushValueStack(IASTNode)},  {@link #valueStackTop()},  {@link #popValueStack()}<br>
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

	public IASTNode valueStackTop(int i) {
		assert(i < 0);
		if (i == -1) {
			return valueStackTop(); // equivalent to valueStack.elementAt(valueStack.size()-1)
		}
		return valueStack.elementAt(valueStack.size()+i); // i is negative ...
	}

	public IASTNode valueStackTop() {
		return valueStack.peek();
	}

	public IASTNode popValueStack() {
		return valueStack.pop();
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