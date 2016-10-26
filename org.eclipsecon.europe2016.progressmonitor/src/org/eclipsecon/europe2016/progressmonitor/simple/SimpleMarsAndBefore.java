package org.eclipsecon.europe2016.progressmonitor.simple;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;
import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;
import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;

public class SimpleMarsAndBefore implements ICoreRunnable {
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "A very simple case", 100);
		doSomethingFor1Sec(subMonitor.newChild(10));
		showMessage("Step 1", "10 ticks out of the 100 of the monitor have been consumed. 90 left.");
		doSomethingFor1Sec(subMonitor.newChild(30));
		showMessage("Step 2", "30 more ticks out of the 100 of the monitor have been consumed. (90-30)=60 left.");
		doSomethingFor(Duration.ofSeconds(2), subMonitor.newChild(60));
		showMessage("Step 3", "All ticks have been consumed.\nNo need to call done() on monitor.");
	}
}
