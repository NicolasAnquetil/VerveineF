package fr.inria.verveine.extractor.fortran.plugin;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.photran.internal.core.analysis.binding.Definition;

import ch.akuhn.fame.Repository;
import eu.synectique.verveine.core.Dictionary;
import eu.synectique.verveine.core.gen.famix.Association;
import eu.synectique.verveine.core.gen.famix.Attribute;
import eu.synectique.verveine.core.gen.famix.BehaviouralEntity;
import eu.synectique.verveine.core.gen.famix.BehaviouralReference;
import eu.synectique.verveine.core.gen.famix.CFile;
import eu.synectique.verveine.core.gen.famix.CompilationUnit;
import eu.synectique.verveine.core.gen.famix.ContainerEntity;
import eu.synectique.verveine.core.gen.famix.DereferencedInvocation;
import eu.synectique.verveine.core.gen.famix.Function;
import eu.synectique.verveine.core.gen.famix.GlobalVariable;
import eu.synectique.verveine.core.gen.famix.Header;
import eu.synectique.verveine.core.gen.famix.ImplicitVariable;
import eu.synectique.verveine.core.gen.famix.Include;
import eu.synectique.verveine.core.gen.famix.IndexedFileAnchor;
import eu.synectique.verveine.core.gen.famix.Method;
import eu.synectique.verveine.core.gen.famix.Module;
import eu.synectique.verveine.core.gen.famix.MultipleFileAnchor;
import eu.synectique.verveine.core.gen.famix.NamedEntity;
import eu.synectique.verveine.core.gen.famix.Namespace;
import eu.synectique.verveine.core.gen.famix.Package;
import eu.synectique.verveine.core.gen.famix.Parameter;
import eu.synectique.verveine.core.gen.famix.ParameterType;
import eu.synectique.verveine.core.gen.famix.ParameterizableClass;
import eu.synectique.verveine.core.gen.famix.ParameterizedType;
import eu.synectique.verveine.core.gen.famix.PreprocessorIfdef;
import eu.synectique.verveine.core.gen.famix.ScopingEntity;
import eu.synectique.verveine.core.gen.famix.SourceAnchor;
import eu.synectique.verveine.core.gen.famix.SourcedEntity;
import eu.synectique.verveine.core.gen.famix.StructuralEntity;
import eu.synectique.verveine.core.gen.famix.Type;
import eu.synectique.verveine.core.gen.famix.TypeAlias;
import eu.synectique.verveine.core.gen.famix.UnknownVariable;


@SuppressWarnings("restriction")
public class FDictionary extends Dictionary<Definition> {
	
 	public FDictionary(Repository famixRepo) {
		super(famixRepo);
	}

	@SuppressWarnings("unchecked")
 	public <T extends NamedEntity> T getEntityByKey(Class<T> clazz, Definition key) {
 		NamedEntity found = super.getEntityByKey(key); 
		if ((found != null) && ! clazz.isInstance(found)) {
			return null;
		}
		else {
			return (T) found;
		}
 	}

	@SuppressWarnings("unchecked")
	protected <T extends NamedEntity> T getEntityIfNotNull(Class<T> clazz, Definition key) {
		if (key == null) {
			return null;
		}
		else {
			NamedEntity found = keyToEntity.get(key);
			if ((found != null) && ! clazz.isInstance(found)) {
				return null;
			}
			else {
				return (T)found;
			}
		}
	}

	protected IndexedFileAnchor createIndexedSourceAnchor(String filename, int beg, int end) {
		IndexedFileAnchor fa;

		fa = new IndexedFileAnchor();
		fa.setStartPos(beg+1);
		fa.setEndPos(end+1);
		fa.setFileName( filename);

		famixRepoAdd(fa);

		return fa;
	}
	
	/**
	 * Adds location information to a Famix Entity.
	 * Location informations are: <b>name</b> of the source file and <b>position</b> in this file.
	 * @param fmx -- Famix Entity to add the anchor to
	 * @param filename -- name of the file being visited
	 * @param ast -- ASTNode, where the information are extracted
	 * @return the Famix SourceAnchor added to fmx. May be null in case of incorrect/null parameter
	 */
	public SourceAnchor addSourceAnchor(SourcedEntity fmx, String filename, IASTFileLocation anchor) {

		if (anchor == null) {
			return null;
		}
		else {
			int beg = anchor.getNodeOffset();
			int end = beg + anchor.getNodeLength();

			return addSourceAnchor( fmx, filename, beg, end);
		}
	}

	public SourceAnchor addSourceAnchor(SourcedEntity fmx, String filename, int start, int end) {
			IndexedFileAnchor fa = null;

			if (fmx == null) {
				return null;
			}

			fa = createIndexedSourceAnchor(filename, start, end);
			fmx.setSourceAnchor(fa);

			return fa;
		}

	/**
	 * Adds location information to a Famix Entity that may be defined/declared in various files.
	 * Currently only used for BehaviouralEntities (functions, methods).
	 * Location informations are: <b>name</b> of the source file and <b>position</b> in this file.
	 * @param fmx -- Famix Entity to add the anchor to
	 * @param filename -- name of the file being visited
	 * @param ast -- ASTNode, where the information are extracted
	 * @return the Famix SourceAnchor added to fmx. May be null in case of incorrect/null parameter
	 */
	public SourceAnchor addSourceAnchorMulti(SourcedEntity fmx, String filename, IASTFileLocation anchor) {

		if (anchor == null) {
			return null;
		}
		else {
			int beg = anchor.getNodeOffset();
			int end = beg + anchor.getNodeLength();

			return addSourceAnchorMulti( fmx, filename, beg, end);
		}
	}

	public SourceAnchor addSourceAnchorMulti(SourcedEntity fmx, String filename, int start, int end) {
		MultipleFileAnchor mfa;

		if (fmx == null) {
			return null;
		}

		mfa = (MultipleFileAnchor) fmx.getSourceAnchor();
		if (mfa == null) {
			mfa = new MultipleFileAnchor();
			fmx.setSourceAnchor(mfa);
			famixRepoAdd(mfa);
		}

		// check if we already have this filename in the MultipleFileAnchor
		/*for (AbstractFileAnchor f : mfa.getAllFiles()) {
			if ( f.getFileName().equals(filename) ) {
				// note: Could check also the position in the file ...
				return mfa;
			}
		}*/

		mfa.addAllFiles( createIndexedSourceAnchor(filename, start, end) );

		return mfa;
	}

	public <T extends NamedEntity> T ensureFamixEntity(Class<T> fmxClass, Definition key, String name) {
		return super.ensureFamixEntity(fmxClass, key, name, /*persistIt*/true);
	}

	public Module ensureFamixModule(Definition key, String name, Package owner) {
		Module fmx = ensureFamixEntity(Module.class, key, name, /*persistIt*/true);
		fmx.setParentPackage(owner);

		return fmx;
	}

	public UnknownVariable ensureFamixUnknownVariable(Definition key, String name, Package parent) {
		UnknownVariable fmx = null;
		
		if (key != null) {
			fmx = getEntityByKey(UnknownVariable.class, key);
		}

		if (fmx == null) {
			fmx = super.ensureFamixEntity(UnknownVariable.class, key, name, /*persistIt*/true);
			fmx.setParentPackage(parent);
		}
		
		return fmx;
	}

	public GlobalVariable ensureFamixGlobalVariable(Definition key, String name, ScopingEntity parent) {
		GlobalVariable fmx;
		fmx = ensureFamixEntity(GlobalVariable.class, key, name, /*persistIt*/true);
		fmx.setParentScope(parent);

		return fmx;
	}

	public Namespace ensureFamixNamespace(Definition key, String name, ScopingEntity parent) {
		Namespace fmx = super.ensureFamixNamespace(key, name);
		if (parent != null) {
			fmx.setParentScope(parent);
		}
		return fmx;
	}

	public Package ensureFamixPackage(Definition key, String name, Package parent) {
		Package fmx = super.ensureFamixEntity(Package.class, key, name, /*persitIt*/true);
		fmx.setIsStub(false);
		if (parent != null) {
			fmx.setParentPackage(parent);
		}
		return fmx;
	}

	public Type ensureFamixType(Definition key, String name, ContainerEntity owner) {
		Type fmx = getEntityIfNotNull(Type.class, key);

		if (fmx == null) {
			fmx = super.ensureFamixType(key, name, owner, /*persistIt*/true);
		}
		
		return fmx;
	}

	/**
	 * May return null
	 * /
	public Type ensureFamixPrimitiveType(int type) {
		StubBinding bnd = StubBinding.getInstance(Type.class, "_primitive_/"+type);
		return ensureFamixPrimitiveType(bnd, primitiveTypeName(type));
	}
	 */

	public Function ensureFamixFunction(Definition key, String name, String sig, ContainerEntity parent) {
		Function fmx = getEntityIfNotNull(Function.class, key);

		if (fmx == null) {
			fmx = super.ensureFamixFunction(key, name, sig, /*returnType*/null, parent, /*persistIt*/true);
			fmx.setCyclomaticComplexity(1);
			fmx.setNumberOfStatements(0);
		}
		return fmx;
	}

	public Attribute ensureFamixAttribute(Definition key, String name, Type parent) {
		Attribute fmx = getEntityIfNotNull(Attribute.class, key);

		if (fmx == null) {
			fmx = super.ensureFamixAttribute(key, name, /*type*/null, parent, /*persistIt*/true);
		}

		return fmx;
	}

	/**
	 * Returns a Famix Parameter associated with the IBinding.
	 * The Entity is created if it does not exist.<br>
	 * Params: see {@link Dictionary#ensureFamixParameter(Object, String, Type, eu.synectique.verveine.core.gen.famix.BehaviouralEntity, boolean)}.
	 * @param persistIt -- whether to persist or not the entity eventually created
	 * @return the Famix Entity found or created. May return null if "bnd" is null or in case of a Famix error
	 */
	public Parameter ensureFamixParameter(Definition bnd, String name, BehaviouralEntity owner) {
		Parameter fmx = null;

		// --------------- to avoid useless computations if we can
		fmx = getEntityByKey(Parameter.class, bnd);
		if (fmx != null) {
			return fmx;
		}

		if (fmx == null) {
			fmx = super.createFamixParameter(bnd, name, /*type*/null, owner, /*persistIt*/true);
		}

		return fmx;
	}

	// UTILITIES =========================================================================================================================================

}
