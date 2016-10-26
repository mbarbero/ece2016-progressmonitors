package org.eclipsecon.europe2016.progressmonitor.cancellation;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;
import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;

public class ForceCheckCancelOxygenAndLater implements ICoreRunnable {
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "A very simple case", 100).checkCanceled();
		doSomethingFor1Sec(subMonitor.split(10).checkCanceled());
		doSomethingFor1Sec(subMonitor.split(30).checkCanceled());
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(60).checkCanceled());
	}
}
