package fr.inria.verveine.extractor.fortran.ast;

public interface IActionStmt extends IASTNode, IExecutableConstruct
{

    ASTToken getLabel();
    void setLabel(ASTToken label);
}

