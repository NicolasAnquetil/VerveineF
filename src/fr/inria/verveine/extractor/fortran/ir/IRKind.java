package fr.inria.verveine.extractor.fortran.ir;

public enum IRKind {
	COMMENT,
	PROGRAM, MODULE, FUNCTION , SUBPROGRAM, GLOBALVAR,
	VARACCESS, FCTCALL, SUBPRGCALL; 
}
