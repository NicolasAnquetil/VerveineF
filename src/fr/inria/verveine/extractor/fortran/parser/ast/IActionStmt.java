package fr.inria.verveine.extractor.fortran.parser.ast;

public interface IActionStmt extends IASTNode, IExecutableConstruct
{

    ASTToken getLabel();
    void setLabel(ASTToken label);
}

