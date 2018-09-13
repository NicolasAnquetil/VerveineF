package fr.inria.verveine.extractor.fortran.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.IPathEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.photran.internal.core.FProjectNature;
import org.eclipse.photran.internal.core.properties.SearchPathProperties;
import org.eclipse.photran.internal.core.vpg.PhotranVPG;

import eu.synectique.verveine.core.VerveineParser;
import eu.synectique.verveine.core.gen.famix.FortranSourceLanguage;
import eu.synectique.verveine.core.gen.famix.SourceLanguage;
import fr.inria.verveine.extractor.fortran.utilities.Constants;
import fr.inria.verveine.extractor.fortran.utilities.FileUtil;
import fr.inria.verveine.extractor.fortran.utilities.TextProgressMonitor;
import fr.inria.verveine.extractor.fortran.visitors.CommentVisitor;
import fr.inria.verveine.extractor.fortran.visitors.InvokAccessVisitor;
import fr.inria.verveine.extractor.fortran.visitors.ScopeDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.SubprgDefVisitor;
import fr.inria.verveine.extractor.fortran.visitors.VarDefVisitor;


@SuppressWarnings("restriction")
public class VerveineFParser extends VerveineParser {
	public static final String WORKSPACE_NAME = "tempWS";

	public static final String DEFAULT_PROJECT_NAME = "tempProj";

	public static final String SOURCE_ROOT_DIR = "src";

	private static final String VERVEINEF_VERSION = "0.4.0_20180731";

	/**
	 * Dictionary used to create all entities. Contains a Famix repository
	 */
	private FDictionary dico = null;

	/**
	 * Directory where the project to analyze is located
	 */
	private String userProjectDir = null;

	/**
	 * Temporary variable to gather macros defined from the command line
	 */
	private Map<String,String> argDefined;

	private FortranSourceLanguage srcLggeInstance;


	public VerveineFParser() {
		super();
		dico = new FDictionary(getFamixRepo());
		this.argDefined = new HashMap<String,String>();
		userProjectDir = null;
	}

	public boolean parse() {
        IProject project = createEclipseProject(DEFAULT_PROJECT_NAME, userProjectDir );
        if (project == null) {
        	System.out.println("// could not create the eclipse project for code indexing, must give up");
        	return false;
        }

        configIndexer(project);
		computeIndex(project);

        try {
    		if (linkToExisting()) {
    			// incremental parsing ...
    		}

    		runAllVisitors( dico, project);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}

        deleteEclipseProject( project);

        return true;
	}

	private void runAllVisitors(FDictionary dico, IProject proj) throws CoreException {
		ICProject cproj = CoreModel.getDefault().getCModel().getCProject(proj.getName());

		cproj.accept(new ScopeDefVisitor(dico));
		cproj.accept(new SubprgDefVisitor(dico));
		cproj.accept(new VarDefVisitor(dico));
		cproj.accept(new CommentVisitor(dico));

		cproj.accept(new InvokAccessVisitor(dico));
	}

	private IProject createEclipseProject(String projName , String sourcePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		configWorkspace(workspace);
		IWorkspaceRoot root = workspace.getRoot();

		// we make a directory at the workspace root to copy source files
		IPath eclipseProjPath = root.getRawLocation().removeLastSegments(1).append(WORKSPACE_NAME).append(projName);
		eclipseProjPath.toFile().mkdirs();

		final IProject project = root.getProject(projName);
		if (project.exists()) {
			// delete to recreate a fresh one
			deleteEclipseProject( project);
		}

		IProjectDescription eclipseProjDesc = workspace.newProjectDescription(project.getName());
		eclipseProjDesc.setLocation(eclipseProjPath);

		try {
			project.create(eclipseProjDesc, Constants.NULL_PROGRESS_MONITOR);
			project.open(Constants.NULL_PROGRESS_MONITOR);
		} catch (CoreException e1) {
			e1.printStackTrace();
			return null;
		}

		try {
			// now we make it a C project and a Fortran project
			CCorePlugin.getDefault().createCProject(eclipseProjDesc, project, Constants.NULL_PROGRESS_MONITOR, project.getName());
            FProjectNature.addFNature(project, new NullProgressMonitor());
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}

		ICProjectDescription cProjectDesc = CoreModel.getDefault().getProjectDescription(project, true);
		cProjectDesc.setCdtProjectCreated();

		if (! copysourceCodeInProject(sourcePath, project) ) {
			return null;
		}

        return project;
	}

	private void deleteEclipseProject(IProject project) {
		try {
			// delete content if the project exists
			if (project.exists()) {
				project.delete(/*deleteContent*/true, /*force*/true, Constants.NULL_PROGRESS_MONITOR);
				project.refreshLocal(IResource.DEPTH_INFINITE, Constants.NULL_PROGRESS_MONITOR);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
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

	/**
	 * sets include path (system, given by user) and macros into the project
	 */
	private void configIndexer(IProject proj) {
		ICProject cproj = CoreModel.getDefault().getCModel().getCProject(proj.getName());

		IPath projPath = cproj.getPath();
		IPathEntry[] oldEntries=null;
		try {			
			oldEntries = cproj.getRawPathEntries();
		} catch (CModelException e) {
			e.printStackTrace();
			return;
		}

		IPathEntry[] newEntries = new IPathEntry[
		                                         oldEntries.length +
		                                         argDefined.size()
		                                         ];
		int i;

		/* copy old entries */
		for (i=0; i < oldEntries.length; i++) {
			newEntries[i] = oldEntries[i];
		}

		/* macros  defined */
		for (Map.Entry<String, String> macro : argDefined.entrySet()) {
			newEntries[i++] = CoreModel.newMacroEntry(projPath, macro.getKey(), macro.getValue());
		}

		try {			
			cproj.setRawPathEntries(newEntries, Constants.NULL_PROGRESS_MONITOR);

		} catch (CModelException e) {
			e.printStackTrace();
		}
	}

	private boolean copysourceCodeInProject(String sourcePath, final IProject project) {
		File projSrc = new File(sourcePath);
		if (! projSrc.exists()) {
			System.err.println("Project directory "+sourcePath+ " not found !");
			return false;
		}
		FileUtil.copySourceFilesInProject(project, SOURCE_ROOT_DIR, projSrc);
		ICProjectDescriptionManager descManager = CoreModel.getDefault().getProjectDescriptionManager();
        try {
			descManager.updateProjectDescriptions(new IProject[] { project }, Constants.NULL_PROGRESS_MONITOR);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
        
        return true;
	}

	private void computeIndex(IProject project) {		
        TextProgressMonitor monitor = new TextProgressMonitor();
        monitor.setTaskName("Indexing source files");
        new SearchPathProperties().setProperty(project, SearchPathProperties.ENABLE_VPG_PROPERTY_NAME, "true");
		PhotranVPG.getInstance().ensureVPGIsUpToDate( monitor);
	}

	@Override
	protected SourceLanguage getMyLgge() {
		if (srcLggeInstance == null) {
			srcLggeInstance = new FortranSourceLanguage();
		}
		return srcLggeInstance;
	}


	public void setOptions(String[] args) {
		int i = 0;
		while (i < args.length && args[i].trim().startsWith("-")) {
		    String arg = args[i++].trim();

			if (arg.equals("-h")) {
				usage();
			}
			else if (arg.equals("-v")) {
				version();
			}
			else if (arg.startsWith("-D")) {
				parseMacroDefinition(arg);
			}
			else {
				int j = super.setOption(i - 1, args);
				if (j > 0) {     // j is the number of args consumed by super.setOption()
					i += j;      // advance by that number of args
					i--;         // 1 will be added at the beginning of the loop ("args[i++]")
				}
				else {
					System.err.println("** Unrecognized option: " + arg);
					usage();
				}
			}
		}

		for ( ; i < args.length; i++) {
			userProjectDir = args[i];
		}
		
		if (userProjectDir == null) {
			System.err.println("Nos project directory set");
			usage();
		}
	}

	private void parseMacroDefinition(String arg) {
		int i;
		String macro;
		String value;

		i = arg.indexOf('=');
		if (i < 0) {
			macro=arg.substring(2);  // remove '-D' at the beginning
			value = "";
		}
		else {
			macro = arg.substring(2, i);
			value = arg.substring(i+1);
		}
		argDefined.put(macro, value);
	}

	protected void usage() {
		System.err.println("Usage: VerveineF [options] <Fortran project directory>");
		System.err.println("Recognized options:");
		System.err.println("      -h: prints this message");
		System.err.println("      -v: prints the version");
		System.err.println("      -o <output-file-name>: changes the name of the output file (default: output.mse)");
		System.err.println("      -D<macro>: defines a C/C++ macro");
		System.err.println("      <Fortran project directory>: directory containing the Fortran project to export in MSE");
		System.exit(0);
	}

	protected void version() {
		System.out.println("VerveineF version:"+VERVEINEF_VERSION);
		System.exit(0);
	}

}
