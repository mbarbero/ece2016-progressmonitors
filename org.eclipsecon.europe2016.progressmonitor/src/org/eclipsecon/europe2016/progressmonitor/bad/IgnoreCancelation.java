package org.eclipsecon.europe2016.progressmonitor.bad;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class IgnoreCancelation implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 1000);
		for(int i = 0; i < 1000; i++) {
			doSomethingFor(Duration.ofMillis(10));
			subMonitor.worked(1);
		}
	}

}
