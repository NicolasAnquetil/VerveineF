package fr.inria.verveine.extractor.fortran.ir;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import fr.inria.verveine.extractor.fortran.parser.ast.ASTModuleNode;


public class SourceAnchorTest extends AbstractIrTest {

	public static final String SOURCE_CODE = "module a_module\n" +	// '\n' at pos 15
			"integer :: VAR1\n" +									// '\n' at pos 31
			"integer :: &\n" +										// '\n' at pos 44
			"     VAR2\n" +											// '\n' at pos 54
			"integer :: VAR3\n" +									// '\n' at pos 70
			"integer :: &\n" +										// '\n' at pos 83
			"     VAR4\n" +											// '\n' at pos 93
			"end module\n";											// '\n' at pos 104

	@Test
	public void testAnchorModule() {
		parseCode(SOURCE_CODE);

		Collection<IREntity> mods = dico.allWithKind(IRKind.MODULE);
		assertEquals(1, mods.size());

		IREntity mod = mods.iterator().next(); 
		assertEquals(0, mod.getData(IREntity.ANCHOR_START));
		assertEquals(104, mod.getData(IREntity.ANCHOR_END));   // EOS token
	}


	@Test
	public void testAnchorVars() {
		parseCode(SOURCE_CODE);

		Collection<IREntity> vars = dico.allWithKind(IRKind.VARIABLE);
		assertEquals(4, vars.size());

		for (IREntity var : vars) {
			switch (var.getName()) {
			case "VAR1" :
				assertEquals(16, var.getData(IREntity.ANCHOR_START));
				assertEquals(31, var.getData(IREntity.ANCHOR_END));
				break;
			case "VAR2" :
				assertEquals(32, var.getData(IREntity.ANCHOR_START));
				// because line break after & was suppressed, indexes are -1, see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(53, var.getData(IREntity.ANCHOR_END));
				// one line break extra after line to correct indexes, see VerveineFortranStream.convertFreeFormInputBuffer()
				break;
			case "VAR3" :
				assertEquals(55, var.getData(IREntity.ANCHOR_START));
				assertEquals(70, var.getData(IREntity.ANCHOR_END));
				break;
			case "VAR4" :
				assertEquals(71, var.getData(IREntity.ANCHOR_START));
				// because line break after & was suppressed, indexes are -1 , see VerveineFortranStream.convertFreeFormInputBuffer()
				assertEquals(92, var.getData(IREntity.ANCHOR_END));
				break;
			}
		}
	}

}