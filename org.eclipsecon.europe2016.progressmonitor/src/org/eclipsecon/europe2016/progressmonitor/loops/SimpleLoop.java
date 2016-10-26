package org.eclipsecon.europe2016.progressmonitor.loops;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class SimpleLoop implements ICoreRunnable {

	private static final List<String> LIST = Arrays.asList("Fox", "Bunny", "Dog", "Cat", "Pig", "Duck"); 
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		SubMonitor loopMonitor = subMonitor.split(50).setWorkRemaining(LIST.size());
		for (String animal : LIST) {
			doSomethingFor(Duration.ofMillis(500), loopMonitor.split(1));
		}
		
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(50));
	}
}
