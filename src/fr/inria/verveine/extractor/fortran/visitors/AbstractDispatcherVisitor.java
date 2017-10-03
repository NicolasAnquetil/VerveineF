package fr.inria.verveine.extractor.fortran.visitors;

import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICElementVisitor;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.IParent;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.photran.core.IFortranAST;
import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.lexer.Token;
import org.eclipse.photran.internal.core.parser.ASTVisitor;
import org.eclipse.photran.internal.core.vpg.PhotranVPG;

import fr.inria.verveine.extractor.fortran.plugin.FDictionary;

/**
 * The superclass of all visitors. These visitors visit an AST to create FAMIX entities.<BR>
 * This abstract class dispatches more finely the visits than what CDT's ASTVisitor normally does. 
 * This visitor also merges two APIs: visit methods on AST (ASTVisitor) and visit methods on ICElements (ICElementVisitor).
 */
@SuppressWarnings("restriction")
public abstract class AbstractDispatcherVisitor extends ASTVisitor implements ICElementVisitor {


	protected PhotranVPG vpg;
	
	protected FDictionary dico;
	
	protected String filename;

	// CONSTRUCTOR ==========================================================================================================================

	public AbstractDispatcherVisitor(FDictionary dico) {
		super();
		this.dico = dico;
		this.vpg = PhotranVPG.getInstance();

		if (msgTrace() != null ) {
			System.out.println(msgTrace());
		}
	}

	abstract protected String msgTrace();

	// VISITING METODS ON ICELEMENT HIERARCHY (ICElementVisitor) ===========================================================================

	@Override
	public boolean visit(ICElement elt) {
		switch (elt.getElementType()) {
		case ICElement.C_PROJECT:
			visit( (ICProject) elt);
			break;
		case ICElement.C_CCONTAINER:
			visit( (ICContainer) elt);
			break;
		case ICElement.C_UNIT:
			visit( (ITranslationUnit) elt);
			break;
		default:
			//  hopefully this should never happen
		}
		return false;
	}

	public void visit(ICProject project) {
		visitChildren(project);
	}

	public void visit(ICContainer cont) {
		visitChildren(cont);
	}

	public void visit(ITranslationUnit elt) {
		// this is the method merging ICElementVisitor and ASTVisitor
		filename = elt.getElementName();
		if (elt.getElementName().toLowerCase().endsWith(".f90") ) {
			IFortranAST ast =  vpg.acquireTransientAST(elt.getFile());
			ast.accept(this);
		}
	}

	// UTILITIES ======================================================================================================

	protected Definition firstDefinition(Token tk) {
		if (tk.resolveBinding().size() > 0) {
			return tk.resolveBinding().get(0);
		}
		else {
			return null;
		}
	}

	protected void visitChildren(IParent elt) {
		try {
			for (ICElement child : elt.getChildren()) {
				child.accept(this);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}