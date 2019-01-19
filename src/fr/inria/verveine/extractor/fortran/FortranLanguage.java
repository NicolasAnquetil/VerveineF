package fr.inria.verveine.extractor.fortran;

import java.util.Arrays;

public class FortranLanguage {

	// possible forms of Fortran code
	public static final int FIXED_FORM = 2;
	public static final int FREE_FORM = 1;
	public static final int UNKNOWN_SOURCE_FORM = -1;

	/**
	 * List of all known Intrinsic Function in Fortran<br>
	 * Extracted from <a href="http://ergodic.ugr.es/cphysn/LECCIONES/FORTRAN/f77to90/a5.html">http://ergodic.ugr.es/cphysn/LECCIONES/FORTRAN/f77to90/a5.html</a>
	 * <p>
	 * Must be ordered for binary search in the array
	 */
	protected static final String[] intrinsicFunctions = {
			"ABS",
			"ACHAR", 
			"ACOS", 
			"ADJUSTL", 
			"ADJUSTR", 
			"AIMAG",
			"AINT",
			"ALL", 
			"ALLOCATED", 
			"ALOG", 
			"ALOG10", 
			"AMAX0",
			"AMAX1",
			"AMIN0",
			"AMIN1",
			"AMOD",
			"ANINT",
			"ANY", 
			"ASIN", 
			"ASSOCIATED",
			"ATAN", 
			"ATAN2", 
			"BIT_SIZE", 
			"BTEST", 
			"CABS",
			"CCOS", 
			"CEILING",
			"CEXP", 
			"CHAR", 
			"CLOG", 
			"CMPLX",
			"CONJG",
			"COS", 
			"COSH", 
			"COUNT", 
			"CSHIFT", 
			"CSIN", 
			"CSQRT", 
			"DABS",
			"DACOS", 
			"DASIN", 
			"DATAN", 
			"DATAN2", 
			"DBLE",
			"DCOS", 
			"DCOSH", 
			"DDIM",
			"DEXP", 
			"DIGITS", 
			"DIM",
			"DINT",
			"DLOG", 
			"DLOG10", 
			"DMAX1",
			"DMIN1",
			"DMOD",
			"DNINT",
			"DOT_PRODUCT", 
			"DPROD",
			"DSIGN",
			"DSIN", 
			"DSINH", 
			"DSQRT", 
			"DTAN", 
			"DTANH", 
			"EOSHIFT", 
			"EPSILON", 
			"EXP", 
			"EXPONENT", 
			"FLOAT",
			"FLOOR",
			"FRACTION", 
			"HUGE", 
			"IABS",
			"IACHAR", 
			"IAND", 
			"IBCLR", 
			"IBITS", 
			"IBSET", 
			"ICHAR", 
			"IDIM",
			"IDINT",
			"IDNINT",
			"IEOR", 
			"IFIX",
			"INDEX", 
			"INT",
			"IOR", 
			"ISHIFT", 
			"ISHIFTC", 
			"ISIGN",
			"KIND", 
			"LBOUND", 
			"LEN", 
			"LEN_TRIM", 
			"LGE", 
			"LGT", 
			"LLE", 
			"LLT", 
			"LOG", 
			"LOG10", 
			"LOGICAL", 
			"MATMUL", 
			"MAX",
			"MAX0",
			"MAX1",
			"MAXEXPONENT", 
			"MAXLOC", 
			"MAXVAL", 
			"MERGE", 
			"MIN",
			"MIN0",
			"MIN1",
			"MINEXPONENT", 
			"MINLOC", 
			"MINVAL", 
			"MOD",
			"MODULO",
			"NEAREST", 
			"NINT",
			"NOT", 
			"PACK", 
			"PRECISION", 
			"PRESENT",
			"PRODUCT", 
			"RADIX", 
			"RANGE", 
			"REAL",
			"REPEAT", 
			"RESHAPE", 
			"RRSPACING", 
			"SCALE", 
			"SCAN", 
			"SELECTED_INT_KIND", 
			"SELECTED_REAL_KIND", 
			"SET_EXPONENT", 
			"SHAPE", 
			"SIGN", 
			"SIN", 
			"SINH", 
			"SIZE", 
			"SNGL", 
			"SPACING", 
			"SPREAD", 
			"SQRT", 
			"SUM ", 
			"TAN", 
			"TANH", 
			"TINY", 
			"TRANSFER", 
			"TRANSPOSE ", 
			"TRIM", 
			"UBOUND", 
			"UNPACK", 
			"VERIFY"
	};


	/**
	 * List of all known Intrinsic Subroutines in Fortran<br>
	 * Extracted from <a href="http://ergodic.ugr.es/cphysn/LECCIONES/FORTRAN/f77to90/a5.html">http://ergodic.ugr.es/cphysn/LECCIONES/FORTRAN/f77to90/a5.html</a>
	 * <p>
	 * Must be ordered for binary search in the array
	 */
	protected static final String[] intrinsicSubroutines = {
		"DATE_AND_TIME", 
		"MVBITS", 
		"RANDOM_NUMBER", 
		"RANDOM_SEED",
		"SYSTEM_CLOCK"
	};
	
	public static boolean isIntrinsicFunction(String name) {
		return Arrays.binarySearch(intrinsicFunctions, name.toUpperCase()) >= 0;
	}

	public static boolean isIntrinsicSubroutine(String name) {
		return Arrays.binarySearch(intrinsicSubroutines, name.toUpperCase()) >= 0;
	}

	public static boolean isIntrinsic(String name) {
		return isIntrinsicFunction(name) || isIntrinsicSubroutine(name); 
	}

}
