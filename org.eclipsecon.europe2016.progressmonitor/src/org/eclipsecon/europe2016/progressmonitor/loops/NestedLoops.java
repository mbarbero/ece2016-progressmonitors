package org.eclipsecon.europe2016.progressmonitor.loops;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;
import static org.eclipsecon.europe2016.progressmonitor.Utils.showMessage;

public class NestedLoops implements ICoreRunnable {

	private static final List<String> LIST = Arrays.asList("Fox", "Bunny", "Dog", "Cat", "Pig", "Duck");
	private static final List<String> LIST2 = Arrays.asList("Red", "Green", "Blue", "White", "Orange", "Yellow", "Black");
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		SubMonitor outerLoopMonitor = subMonitor.split(50).setWorkRemaining(LIST.size());
		for (String animal : LIST) {
			SubMonitor innerLoopMonitor = outerLoopMonitor.split(1).setWorkRemaining(LIST2.size() + 1);
			for (String color : LIST2) {
				doSomethingFor(Duration.ofMillis(150), innerLoopMonitor.split(1));
			}
			doSomethingFor(Duration.ofMillis(500), innerLoopMonitor.split(1));
			
		}
		showMessage("End of nested loop", "We have consumed the 50% of the bar");
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(50));
	}
}
