package org.eclipsecon.europe2016.progressmonitor.branches;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;
import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;

public class SimpleIf implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		
		myMethod(false, false, subMonitor.split(1));
		showMessage("Step 1", "25% of the bar is consumed");
		myMethod(true, false, subMonitor.split(1));
		showMessage("Step 2", "50% of the bar is consumed");
		myMethod(false, true, subMonitor.split(1));
		showMessage("Step 3", "75% of the bar is consumed");
		myMethod(true, true, subMonitor.split(1));
		showMessage("Step 4", "100% of the bar is consumed");
	}

	void myMethod(boolean c1, boolean c2, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		doSomethingFor1Sec(subMonitor.split(20));
		
		if (c1) {
			doSomethingFor1Sec(subMonitor.split(30));
		}
		subMonitor.setWorkRemaining(50);
		
		if (c2) {
			doSomethingFor1Sec(subMonitor.split(30));
		}
		
		doSomethingFor1Sec(subMonitor.setWorkRemaining(20).split(20));
	}
}
