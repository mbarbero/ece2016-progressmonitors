package org.eclipsecon.europe2016.progressmonitor.bad;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class IgnoreIt implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		doSomethingFor(Duration.ofSeconds(5));
	}

}
