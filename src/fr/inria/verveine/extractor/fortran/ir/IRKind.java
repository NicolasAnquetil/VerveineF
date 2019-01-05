package fr.inria.verveine.extractor.fortran.ir;

public enum IRKind {
	COMMENT,
	COMPILATION_UNIT, PROGRAM, MODULE, FUNCTION , SUBPROGRAM, VARIABLE,
	SUBPRGCALL, NAMEREF, USEMODULE; 
}
