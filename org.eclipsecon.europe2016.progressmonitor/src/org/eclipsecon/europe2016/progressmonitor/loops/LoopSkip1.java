package org.eclipsecon.europe2016.progressmonitor.loops;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;
import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor1Sec;

public class LoopSkip1 implements ICoreRunnable {
	private static final List<String> LIST = Arrays.asList("Fox", "Bunny", "Dog", "Cat", "Pig", "Duck"); 
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		SubMonitor loopMonitor = subMonitor.split(50);
		int workRemaining = LIST.size();
		for (String animal : LIST) {
			loopMonitor.setWorkRemaining(workRemaining--);
			
			if (animal.startsWith("B")) {
				continue;
			}
			
			doSomethingFor(Duration.ofSeconds(2), loopMonitor.split(1));
		}
		
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(50));
	}
}
