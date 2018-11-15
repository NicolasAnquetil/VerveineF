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
package fr.inria.verveine.extractor.fortran.ast;

import java.util.Iterator;
import java.util.List;

import fr.inria.verveine.extractor.fortran.ast.ASTNodeUtil.NonNullIterator;

@SuppressWarnings("all")
public abstract class ASTNodeWithErrorRecoverySymbols extends ASTNode
{
    ErrorRecoveryInfo errorInfo = null;

    @Override public Iterable<? extends IASTNode> getChildren()
    {
        return new Iterable<IASTNode>()
        {
            public Iterator<IASTNode> iterator()
            {
                return new NonNullIterator<IASTNode>(new Iterator<IASTNode>()
                {
                    private int index = 0;
                    private int numChildren = getNumASTFields();
                    private int numErrorChildren = errorInfo == null ? 0 : errorInfo.getDiscardedSymbols().size();

                    public boolean hasNext()
                    {
                        return index < numChildren + numErrorChildren;
                    }

                    public IASTNode next()
                    {
                        if (index < numChildren)
                            return getASTField(index++);
                        else
                            return errorInfo.<IASTNode>getDiscardedSymbols().get(index++ - numChildren);
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                });
            }
        };
    }

    public ASTToken getErrorASTToken()
    {
        return errorInfo == null ? null : errorInfo.errorLookahead;
    }

    public String describeTerminalsExpectedAtErrorPoint()
    {
        return errorInfo == null ? "(none)" : errorInfo.describeExpectedSymbols();
    }

    public List<IASTNode> getSymbolsDiscardedDuringErrorRecovery()
    {
        return errorInfo == null ? null : errorInfo.<IASTNode>getDiscardedSymbols();
    }

}
