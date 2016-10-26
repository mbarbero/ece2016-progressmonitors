package org.eclipsecon.europe2016.progressmonitor.loops;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;
import static org.eclipsecon.europe2016.progressmonitor.Utils.streamOfString;

public class UnboundedLoop implements ICoreRunnable {
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor);
		for (String s : streamOfString()) {
			doSomethingFor(Duration.ofMillis(10), 
					subMonitor.setWorkRemaining(100).split(1));
		}
	}
}
