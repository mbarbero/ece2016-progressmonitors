package org.eclipsecon.europe2016.progressmonitor.branches;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;
import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;
public class ComplexIf implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		
		myMethod(true, true, true, subMonitor.split(1));
		showMessage("Step 1", "33% of the bar is consumed");
		myMethod(true, true, false, subMonitor.split(1));
		showMessage("Step 2", "66% of the bar is consumed");
		myMethod(false, false, false, subMonitor.split(1));
		showMessage("Step 3", "100% of the bar is consumed");
	}

	void myMethod(boolean c1, boolean c2, boolean c3, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		doSomethingFor1Sec(subMonitor.split(10));
		
		if (c1) {
			doSomethingFor1Sec(subMonitor.split(30));
		}
		subMonitor.setWorkRemaining(60);
		
		if (c2) {
			if (c3) {
				doSomethingFor1Sec(subMonitor.split(30));
			} else {
				doSomethingFor1Sec(subMonitor.setWorkRemaining(40).split(10));
			}

			doSomethingFor1Sec(subMonitor.split(20));
		}
		
		doSomethingFor1Sec(subMonitor.setWorkRemaining(10).split(10));
		
	}
}
