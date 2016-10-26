package org.eclipsecon.europe2016.progressmonitor.cancellation;

import java.time.Duration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;
import static org.eclipsecon.europe2016.progressmonitor.Utils.workSomethingFor;

public class CheckCancelMarsAndBefore implements ICoreRunnable {
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "A very simple case", 100);

		workSomethingFor(Duration.ofSeconds(2), subMonitor.newChild(10));
		if (monitor.isCanceled()) {
			showMessage("Step 1", "Canceled here during 'Step 1'");
			throw new OperationCanceledException();
		}

		workSomethingFor(Duration.ofSeconds(2), subMonitor.newChild(30));
		if (monitor.isCanceled()) {
			showMessage("Step 2", "Canceled here during 'Step 2'");
			throw new OperationCanceledException();
		}

		workSomethingFor(Duration.ofSeconds(2), subMonitor.newChild(60));
		if (monitor.isCanceled()) {
			showMessage("Step 3", "Canceled here during 'Step 3'");
			throw new OperationCanceledException();
		}
	}
}
