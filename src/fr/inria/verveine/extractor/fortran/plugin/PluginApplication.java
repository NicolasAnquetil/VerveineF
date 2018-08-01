package fr.inria.verveine.extractor.fortran.plugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class PluginApplication implements IApplication {

	@Override
	public Object start(IApplicationContext ctxt) throws Exception {
		System.out.println("Starting VerveineF");
		
		String[] appArgs = (String[])ctxt.getArguments().get(IApplicationContext.APPLICATION_ARGS);

		VerveineFParser verveine = new VerveineFParser();
		verveine.setOptions(appArgs);
		if (verveine.parse()) {
			verveine.emitMSE();
		}
		else {
			System.err.println("Error in model creation, aborting");
		}
		return null;
	}

	@Override
	public void stop() {
		// nothing
	}

}
