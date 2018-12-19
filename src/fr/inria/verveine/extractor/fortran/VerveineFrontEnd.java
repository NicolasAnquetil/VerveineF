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

public class VerveineFrontEnd implements Callable<Boolean> {

	private FortranStream inputStream;
	private FortranTokenStream tokens;
	private FortranLexer lexer;
	private IFortranParser parser;
	private FortranLexicalPrepass prepass;
	private int sourceForm;
	private  ArrayList<String> includeDirs;

	public VerveineFrontEnd(String args[]) throws Exception {
		this(args, "fortran.ofp.parser.java.FortranParserActionNull");
	}
	public VerveineFrontEnd(String args[], String type) throws Exception {
		String filename;
		includeDirs = new ArrayList<String>();

		ArrayList<String> newArgsList = new ArrayList<>();
		String [] newArgs;
		// skipping options
		int i=0;
		while (i<args.length && args[i].startsWith("-")) {
			if (args[i].equals("-I")) {
				includeDirs.add(args[i+1]);
				i++;
			}
			else {
				newArgsList.add(args[i]);
			}
			i++;
		}
		newArgs = newArgsList.toArray(new String[newArgsList.size()]);


		for ( ; i<args.length ; i++) {
			filename = args[i];

			/* Make sure the file exists. */
			File file = new File(filename);
			if (file.exists() == false) {
				System.err.println("Error: " + filename + " does not exist!");
			} else {
				includeDirs.add(file.getParent());
			}

			String path = file.getAbsolutePath();

			this.inputStream = new FortranStream(filename);
			this.lexer = new FortranLexer(inputStream);

			// Changes associated with antlr version 3.3 require that includeDirs
			// be set here as the tokens are loaded by the constructor.
			this.lexer.setIncludeDirs(includeDirs);
			this.tokens = new FortranTokenStream(lexer);

			this.parser = new FortranParser2008(tokens);
			this.parser.initialize(newArgs, type, filename, path);

			this.prepass = new FortranLexicalPrepass(lexer, tokens, parser);
			this.sourceForm = inputStream.getSourceForm();

			call();
		}
	}

	private static boolean parseMainProgram(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		// try parsing the main program
		parser.main_program();

		return parser.hasErrorOccurred();
	} // end parseMainProgram()

	private static boolean parseModule(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.module();
		return parser.hasErrorOccurred();
	} // end parseModule()

	private static boolean parseSubmodule(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.submodule();
		return parser.hasErrorOccurred();
	} // end parseSubmodule()

	private static boolean parseBlockData(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.block_data();

		return parser.hasErrorOccurred();
	} // end parseBlockData()

	private static boolean parseSubroutine(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.subroutine_subprogram();

		return parser.hasErrorOccurred();
	} // end parserSubroutine()

	private static boolean parseFunction(FortranTokenStream tokens,
			IFortranParser parser, int start) throws Exception {
		parser.ext_function_subprogram();
		return parser.hasErrorOccurred();
	} // end parseFunction()

	private static boolean parseProgramUnit(FortranLexer lexer,
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

	public IFortranParser getParser() {
		return this.parser;
	}

	public Boolean call() throws Exception {
		boolean error = false;

		int sourceForm = inputStream.getSourceForm();

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
			if (error) {
				System.out.println("Parser failed");
				return false;
			}
		} // end while (not end of file)

		// Call the end_of_file action here so that it comes after the
		// end_program_stmt occurs.
		getParser().eofAction();

		// Call the cleanUp method for the give action class. This is more
		// important in the case of a C action *class* since it could easily
		// have created memory that's outside of the jvm.
		getParser().getAction().cleanUp();

		return true;
	} // end call()

	public int getSourceForm() {
		return this.sourceForm;
	}

} // end class FrontEnd
