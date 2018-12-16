/**
 * Copyright (c) 2005, 2006 Los Alamos National Security, LLC.  This
 * material was produced under U.S. Government contract DE-
 * AC52-06NA25396 for Los Alamos National Laboratory (LANL), which is
 * operated by the Los Alamos National Security, LLC (LANS) for the
 * U.S. Department of Energy. The U.S. Government has rights to use,
 * reproduce, and distribute this software. NEITHER THE GOVERNMENT NOR
 * LANS MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR ASSUMES ANY
 * LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified to
 * produce derivative works, such modified software should be clearly
 * marked, so as not to confuse it with the version available from
 * LANL.
 *  
 * Additionally, this program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package fr.inria.verveine.extractor.fortran;

import java.io.*;

// the concrete parser class
import fortran.ofp.parser.java.FortranParser2008;

import fortran.ofp.parser.java.FortranLexer;
import fortran.ofp.parser.java.FortranLexicalPrepass;
import fortran.ofp.parser.java.FortranStream;
import fortran.ofp.parser.java.FortranTokenStream;
import fortran.ofp.parser.java.IFortranParser;

import org.antlr.runtime.*;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * The class is a copy of OpenFortranProject {@link fortran.ofp.FrontEnd}.
 * <p>
 * It was necessary to copy it because some changes were required in attributes and methods that are
 * declared <code>private</code> in the original class
 * </p>
 * @author NicolasAnquetil
 */
public class VerveineFFrontEnd implements Callable<Boolean> {

	protected FortranStream inputStream;
	protected FortranTokenStream tokens;
	protected FortranLexer lexer;
	protected IFortranParser parser;
	protected FortranLexicalPrepass prepass;
	protected String filename;
	protected int sourceForm;
	protected boolean verbose = false;
	protected static boolean hasErrorOccurred = false;
	protected static ArrayList<String> includeDirs;

	public VerveineFFrontEnd(String[] args, String filename, String type)
			throws IOException {

		File file = new File(filename);
		String path = file.getAbsolutePath();

		this.inputStream = new FortranStream(filename);

		boolean riceCAF = false;
		boolean lanlExtensions = false;

		this.lexer = new FortranLexer(inputStream);

		// Changes associated with antlr version 3.3 require that includeDirs
		// be set here as the tokens are loaded by the constructor.
		//this.lexer.setIncludeDirs(includeDirs);
		this.tokens = new FortranTokenStream(lexer);

		// check to see if using RiceCAF parser extensions
		//
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--RiceCAF")) {
				riceCAF = true;
			}
		}

		// check to see if using LOPe parser extensions
		//
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--LOPExt")) {
				lanlExtensions = true;
			}
		}

		if (riceCAF == true) {
			// laksono 08.06.2010: only output if we have --verbose option set
			if (verbose) {
				System.out.println("FortranLexer: using Rice University's CAF extensions");
				System.out.println("ERROR: Not available at this time");
				System.exit(1);
			}
			// this.parser = new FortranParserRiceCAF(tokens);
		}
		else if (lanlExtensions == true) {
			if (verbose) {
				System.out.println("FortranLexer: using OFP's LOPe research extensions");
				System.out.println("ERROR: Not available at this time");
				System.exit(1);
			}
			// this.parser = new FortranParserLOPe(tokens);
		}
		else {
			this.parser = new FortranParser2008(tokens);
		}

		this.parser.initialize(args, type, filename, path);

		this.prepass = new FortranLexicalPrepass(lexer, tokens, parser);
		this.filename = filename;
		this.sourceForm = inputStream.getSourceForm();
	}

	protected static boolean parseMainProgram(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		// try parsing the main program
		parser.main_program();

		return parser.hasErrorOccurred();
	} // end parseMainProgram()

	protected static boolean parseModule(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.module();
		return parser.hasErrorOccurred();
	} // end parseModule()

	protected static boolean parseSubmodule(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.submodule();
		return parser.hasErrorOccurred();
	} // end parseSubmodule()

	protected static boolean parseBlockData(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.block_data();

		return parser.hasErrorOccurred();
	} // end parseBlockData()

	protected static boolean parseSubroutine(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.subroutine_subprogram();

		return parser.hasErrorOccurred();
	} // end parserSubroutine()

	protected static boolean parseFunction(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.ext_function_subprogram();
		return parser.hasErrorOccurred();
	} // end parseFunction()

	protected static boolean parseProgramUnit(FortranLexer lexer,
			FortranTokenStream tokens, IFortranParser parser) throws Exception {
		int firstToken;
		int lookAhead = 1;
		int start;
		boolean error = false;

		// check for opening with an include file
		parser.checkForInclude();

		// first token on the *line*. will check to see if it's
		// equal to module, block, etc. to determine what rule of
		// the grammar to start with.
		try {
			lookAhead = 1;
			do {
				firstToken = tokens.LA(lookAhead);
				lookAhead++;
			} while (firstToken == FortranLexer.LINE_COMMENT
					|| firstToken == FortranLexer.T_EOS);


			// mark the location of the first token we're looking at
			start = tokens.mark();

			// attempt to match the program unit
			// each of the parse routines called will first try and match
			// the unit they represent (function, block, etc.). if that
			// fails, they may or may not try and match it as a main
			// program; it depends on how it fails.
			//
			// due to Sale's algorithm, we know that if the token matches
			// then the parser should be able to successfully match.
			//
			if (firstToken != FortranLexer.EOF) {
				// CER (2011.10.18): Module is now (F2008) a prefix-spec so
				// must look for subroutine and functions before module stmts.
				// Part of fix for bug 3425005.
				if (tokens.lookForToken(FortranLexer.T_SUBROUTINE) == true) {
					// try matching a subroutine
					error = parseSubroutine(tokens, parser, start);
				}
				else if (tokens.lookForToken(FortranLexer.T_FUNCTION) == true) {
					// try matching a function
					error = parseFunction(tokens, parser, start);
				}
				else if (firstToken == FortranLexer.T_MODULE
						&& tokens.LA(lookAhead) != FortranLexer.T_PROCEDURE) {
					// try matching a module
					error = parseModule(tokens, parser, start);
				}
				else if (firstToken == FortranLexer.T_SUBMODULE) {
					// try matching a submodule
					error = parseSubmodule(tokens, parser, start);
				}
				else if ( firstToken == FortranLexer.T_BLOCKDATA
						|| (firstToken == FortranLexer.T_BLOCK
						&& tokens.LA(lookAhead) == FortranLexer.T_DATA)) {
					// try matching block data
					error = parseBlockData(tokens, parser, start);
				}
				else {
					// what's left should be a main program
					error = parseMainProgram(tokens, parser, start);
				} // end else(unhandled token)
			} // end if(file had nothing but comments empty)
		} catch (RecognitionException e) {
			e.printStackTrace();
			error = true;
		} // end try/catch(parsing program unit)

		return error;
	} // end parseProgramUnit()

	public void setVerbose(Boolean vFlag, Boolean sFlag) {
		this.verbose = vFlag;
	}

	public IFortranParser getParser() {
		return this.parser;
	}

	public Boolean call() throws Exception {
		boolean error = false;

		int sourceForm = inputStream.getSourceForm();

		if (sourceForm == FortranStream.FIXED_FORM)
			if (verbose)       System.out.println(filename + " is FIXED FORM");
			else if (verbose)  System.out.println(filename + " is FREE FORM");

		// determine whether the file is fixed or free form and
		// set the source form in the prepass so it knows how to handle lines.
		prepass.setSourceForm(sourceForm);

		// apply Sale's algorithm to the tokens to allow keywords
		// as identifiers. also, fixup labeled do's, etc.
		prepass.performPrepass();

		// overwrite the old token stream with the (possibly) modified one
		tokens.finalizeTokenStream();

		// parse each program unit in a given file
		while (tokens.LA(1) != FortranLexer.EOF) {
			// attempt to parse the current program unit
			error = parseProgramUnit(lexer, tokens, parser);

			// see if we successfully parse the program unit or not
			if (verbose && error) {
				System.out.println("Parser failed");
				return new Boolean(error);
			}
		} // end while (not end of file)

		// Call the end_of_file action here so that it comes after the
		// end_program_stmt occurs.
		getParser().eofAction();

		// Call the cleanUp method for the give action class. This is more
		// important in the case of a C action *class* since it could easily
		// have created memory that's outside of the jvm.
		getParser().getAction().cleanUp();

		if (verbose) {
			System.out.println("Parser exiting normally");
		}

		return new Boolean(error);
	} // end call()

	public int getSourceForm() {
		return this.sourceForm;
	}

	public static boolean getError() {
		return hasErrorOccurred;
	}

} // end class FrontEnd
