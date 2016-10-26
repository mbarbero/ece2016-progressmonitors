package org.eclipsecon.europe2016.progressmonitor.cancellation;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class CheckCancelNeonAndLater implements ICoreRunnable {
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "A very simple case", 100);
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(10));
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(30));
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(60));
	}
}
