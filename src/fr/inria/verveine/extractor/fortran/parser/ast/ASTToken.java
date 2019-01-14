/*******************************************************************************
 * Copyright (c) 2007 University of Illinois at Urbana-Champaign and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     UIUC - Initial API and implementation
 *******************************************************************************/
package fr.inria.verveine.extractor.fortran.parser.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Token;

import fortran.ofp.parser.java.FortranToken;

/**
 * Tokens are returned by the lexical analyzer and serve as leaf nodes in the AST.
 * <p>
 * This is the implementation of {@link IToken} used by the Fortran parser.
 */
public class ASTToken implements Token, IASTNode
{

    protected FortranToken actualToken = null;

    /**
     * Whitespace and whitetext appearing before this token that should be associated with this token
     */
    protected String whiteBefore = null;

    /**
     * Whitespace and whitetext appearing after this token that should be associated with this token, not the next
     */
    protected String whiteAfter = ""; //$NON-NLS-1$

    public ASTToken(FortranToken tok) {
    	actualToken = tok;
    }

	/**
     * Returns whitespace and whitetext appearing before this token that should be associated with this token<BR>
     * Get it, either from value set before {@link #setWhiteBefore(String)} or from the whiteText associated
     * to the underlying OFP token
     */
    public String getWhiteBefore() {

    	return (whiteBefore == null) ? actualToken.getWhiteText() : whiteBefore;
   	}

    /**
     * Sets whitespace and whitetext appearing before this token that should be associated with this token
     */
    public void setWhiteBefore(String value) {
    	whiteBefore = (value == null ? "" : value);
    }

    /**
     * Returns whitespace and whitetext appearing after this token that should be associated with this token, not the next
     */
    public String getWhiteAfter() { return whiteAfter; }

    /**
     * Sets whitespace and whitetext appearing after this token that should be associated with this token, not the next
     */
    public void setWhiteAfter(String value) { whiteAfter = value == null ? "" : value; } //$NON-NLS-1$

    /**
     * @param commentPattern
     * @return
     */
    private List<ASTToken> getPrecedingComments(Pattern commentPattern)
    {
/*        String whitetext = getWhiteBefore();
        Matcher m = commentPattern.matcher(whitetext);
        int startStreamOffset = getStreamOffset() - whitetext.length();
        int startFileOffset = getFileOffset() - whitetext.length();
*/
        List<ASTToken> result = new LinkedList<ASTToken>();
/*        
        for (int startSearchFrom = 0; m.find(startSearchFrom); startSearchFrom = m.end())
        {
            ASTToken token = new ASTToken(this);
            token.setTerminal(Terminal.SKIP);
            String prefix = m.group(1);
            String directive = m.group(2).trim();
            String suffix = directive.length() >= m.group(2).length() ? ""  : m.group(2).substring(directive.length()); //$NON-NLS-1$
            token.setWhiteBefore(prefix);
            token.setText(directive);
            token.setWhiteAfter(suffix);
            token.setStreamOffset(startStreamOffset + m.start());
            token.setFileOffset(startFileOffset + m.start());
            token.setLength(prefix.length() + directive.length());
            token.setParent(null);
            token.setLine(token.getLine() - countNewlines(whitetext.substring(m.start())));
            token.setCol(1);
            result.add(token);
        }
*/
        return result;
    }

    public FortranToken getActualToken() {
    	return actualToken;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // IASTNode Implementation
    ///////////////////////////////////////////////////////////////////////////

    private IASTNode parent = null;
    
     public IASTNode getParent()
    {
        return parent;
    }

    public void setParent(IASTNode parent)
    {
        this.parent = parent;
    }

    public void accept(IASTVisitor visitor)
    {
        visitor.visitToken(this);
    }

    public <T extends IASTNode> Set<T> findAll(Class<T> targetClass)
    {
        return ASTNodeUtil.findAll(this, targetClass);
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
        return this;
    }

    public ASTToken findLastToken()
    {
        return this;
    }

    public <T extends IASTNode> T findNearestAncestor(Class<T> targetClass)
    {
        return ASTNodeUtil.findNearestAncestor(this, targetClass);
    }

    public Iterable<? extends IASTNode> getChildren()
    {
        return new LinkedList<IASTNode>();
    }

    public boolean isFirstChildInList()
    {
        return ASTNodeUtil.isFirstChildInList(this);
    }

    public void replaceChild(IASTNode node, IASTNode withNode)
    {
        throw new UnsupportedOperationException();
    }

	@Override
	public String fullyQualifiedName() {
		return getParent().fullyQualifiedName()+".<"+actualToken.getText()+">";
	}

    @Override
    public String getText() { return actualToken.getText(); }
    
    @Override
    public void setText(String text) {
    	actualToken.setText(text);
    }

	@Override
    public int getLine()
    {
        return actualToken.getLine();
    }

	@Override
	public void setLine(int line) {
		actualToken.setLine(line);
	}

	@Override
	public int getChannel() {
		return actualToken.getChannel();
	}

	@Override
	public int getCharPositionInLine() {
		return actualToken.getCharPositionInLine();
	}

	@Override
	public CharStream getInputStream() {
		return actualToken.getInputStream();
	}

	@Override
	public int getTokenIndex() {
		return actualToken.getTokenIndex();
	}

	public int getStartIndex() {
		return actualToken.getStartIndex();
	}

	public int getStopIndex() {
		return actualToken.getStopIndex();
	}

	@Override
	public int getType() {
		return actualToken.getType();
	}

	@Override
	public void setChannel(int channel) {
		actualToken.setChannel(channel);
	}

	@Override
	public void setCharPositionInLine(int pos) {
		actualToken.setCharPositionInLine(pos);
	}

	@Override
	public void setInputStream(CharStream input) {
		actualToken.setInputStream(input);
	}

	@Override
	public void setTokenIndex(int index) {
		actualToken.setTokenIndex(index);
	}

	@Override
	public void setType(int type) {
		actualToken.setType(type);
	}
	
	@Override
	public String toString() {
		return actualToken.toString();
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
