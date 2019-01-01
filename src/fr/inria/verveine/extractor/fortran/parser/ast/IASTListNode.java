package fr.inria.verveine.extractor.fortran.parser.ast;

import java.util.List;

public interface IASTListNode<T> extends List<T>, IASTNode
{
    void insertBefore(T insertBefore, T newElement);
    void insertAfter(T insertAfter, T newElement);
}
