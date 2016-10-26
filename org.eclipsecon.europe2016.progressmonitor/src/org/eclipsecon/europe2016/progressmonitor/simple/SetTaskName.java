package org.eclipsecon.europe2016.progressmonitor.simple;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;

public class SetTaskName implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "My task name", 4);
		
		showMessage("Step 1", "Task name = 'My task name'");
		
		aMethod(subMonitor.split(1));
		showMessage("With split(int)", "Task name = 'Task name'");
		
		aMethod(subMonitor.split(1, SubMonitor.SUPPRESS_NONE));
		showMessage("With split(int, int)", "Task name = 'Another task name'");
		
		SubMonitor childMonitor1 = subMonitor.split(1);
		childMonitor1.setTaskName("Child Monitor");
		showMessage("With split(int)", "Task name = 'Child Monitor'");
		
		SubMonitor childMonitor2 = subMonitor.split(1, SubMonitor.SUPPRESS_SETTASKNAME);
		childMonitor2.setTaskName("The last child Monitor");
		showMessage("With split(int)", "Task name = 'Child Monitor'");
		
	}

	private void aMethod(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Another task name", 10);
		subMonitor.worked(10);
	}
}
