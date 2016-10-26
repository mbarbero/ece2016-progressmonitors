package org.eclipsecon.europe2016.progressmonitor.branches;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class BadBranching implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		myMethod(false, monitor);
	}

	void myMethod(boolean c1, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		if (c1) {
			doSomethingFor(Duration.ofSeconds(5), subMonitor.split(50));
		} else {
			subMonitor.worked(50);
		}
		
		doSomethingFor(Duration.ofSeconds(5), subMonitor.split(50));
	}
}
