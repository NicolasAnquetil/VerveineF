package fr.inria.verveine.extractor.fortran.plugin;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.photran.internal.core.vpg.PhotranVPG;

import eu.synectique.verveine.core.VerveineParser;
import eu.synectique.verveine.core.gen.famix.CSourceLanguage;
import eu.synectique.verveine.core.gen.famix.SourceLanguage;

import fr.inria.verveine.extractor.fortran.visitors.InvokVisitor;
import fr.inria.verveine.extractor.fortran.visitors.ScopeDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.SubprgDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.VarDefVisitor;


@SuppressWarnings("restriction")
public class VerveineFParser extends VerveineParser {

	/**
	 * Dictionary used to create all entities. Contains a Famix repository
	 */
	private FDictionary dico;

	private static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

    //private SearchPathProperties properties;

	public VerveineFParser() {
		super();
		dico = new FDictionary(getFamixRepo());
	}

	public boolean parse() {
        ICProject fproject = createEclipseProject("Carmel", "/home/anquetil/Documents/RMod/Tools/Fortran/workspace/");
        if (fproject == null) {
        	System.out.println("// could not create the project :-(");
        	return false;
        }
        else {
        	System.out.println("project successfully created");
        }

		computeIndex(fproject);

        try {
    		if (linkToExisting()) {
    			// incremental parsing ...
    		}

    		runAllVisitors( dico, fproject);

		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

        return true;
	}

	private void runAllVisitors(FDictionary dico, ICProject proj) throws CoreException {

		proj.accept(new ScopeDefVisitor(dico));
		proj.accept(new SubprgDefVisitor(dico));
		proj.accept(new VarDefVisitor(dico));
		proj.accept(new InvokVisitor(dico));
	}

	private void configWorkspace(IWorkspace workspace) {
		IWorkspaceDescription workspaceDesc = workspace.getDescription();
		workspaceDesc.setAutoBuilding(false); // we do not want the workspace to rebuild the project every time a new resource is added
		try {
			workspace.setDescription(workspaceDesc);
		} catch (CoreException exc) {
			System.err.println("Error trying to set workspace description: " + exc.getMessage());
		}

	}

	private ICProject createEclipseProject(String projName, String sourcePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		configWorkspace(workspace);
		IWorkspaceRoot root = workspace.getRoot();

		final IProject project = root.getProject(projName);

		try {
			if (! project.exists()) {
				project.create(/*eclipseProjDesc,*/ NULL_PROGRESS_MONITOR);
			}
			project.open(NULL_PROGRESS_MONITOR);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		ICProjectDescription cProjectDesc = CoreModel.getDefault().getProjectDescription(project, true);
		cProjectDesc.setCdtProjectCreated();

        return CoreModel.getDefault().getCModel().getCProject(project.getName());
	}

	private void computeIndex(ICProject cproject) {
		System.out.println("Indexing source files");

        PhotranVPG.getInstance().ensureVPGIsUpToDate(NULL_PROGRESS_MONITOR);      
	}

	@Override
	protected SourceLanguage getMyLgge() {
		return new CSourceLanguage();
	}

}
