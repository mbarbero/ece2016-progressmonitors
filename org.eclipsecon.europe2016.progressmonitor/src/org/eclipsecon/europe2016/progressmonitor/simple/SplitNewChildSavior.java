package org.eclipsecon.europe2016.progressmonitor.simple;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;
import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;
public class SplitNewChildSavior implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		
		badMethod(subMonitor.split(2));
		SubMonitor splittedMonitor = subMonitor.split(6);
		showMessage("Step 1", "20% of the bar is consumed");
		badMethod(splittedMonitor);
		splittedMonitor = subMonitor.split(2);
		showMessage("Step 2", "80% of the bar is consumed");
		badMethod(splittedMonitor);
		showMessage("Step 3", "88% of the bar is consumed\n (but we don't really care, we reached the end)");
	}
	
	void badMethod(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		doSomethingFor1Sec(subMonitor.split(40));
	}

}
