package fr.inria.verveine.extractor.fortran.utilities;

import org.eclipse.core.runtime.NullProgressMonitor;

public class TextProgressMonitor extends NullProgressMonitor {

	private int done;
	private int total;
	private String name;

	public TextProgressMonitor() {
		done = 0;
		name = "";
	}

	@Override
	public void subTask(String subname) {
		System.out.println(this.name + " "+ subname);
		super.subTask(name);
	}

	@Override
	public void beginTask(String name, int totalWork) {
		if (name != null && name.length() > 0) {
			this.name = name;
			this.total = totalWork;
		}
	}

	@Override
	public void done() {
		System.out.println(name + " done");
		super.done();
	}

	@Override
	public void setCanceled(boolean cancelled) {
		System.out.println(name + " cancelled");
		super.setCanceled(cancelled);
	}

	@Override
	public void worked(int work) {
		done += work;
		System.out.println(name + " done: "+done+"/"+total);
		super.worked(work);
	}

	@Override
	public void setTaskName(String name) {
		this.name = name;
		super.setTaskName(name);
	}

}
