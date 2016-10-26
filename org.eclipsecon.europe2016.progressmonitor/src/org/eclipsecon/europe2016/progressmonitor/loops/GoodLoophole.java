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

public class GoodLoophole implements ICoreRunnable {
	
	private static final List<String> LIST = Arrays.asList("Fox", "Bunny", "Dog", "Cat", "Pig", "Duck"); 
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		// I want the loop the consume at most 50 ticks out of the 100 of submonitor
		subMonitor.setWorkRemaining(LIST.size() * 2); // LIST.size() * 100/50
		for (String animal : LIST) {
			doSomethingFor(Duration.ofMillis(1000), subMonitor.split(1));
			if ("Fox".equals(animal)) {
				break;
			}
		}
		showMessage("Step 1", "Found 'Dog' after iterating on half of the list.\n"
				+ "Iterating the whole list would have taken 50% of the bar.\n"
				+ "We have thus consumed only 25% of the bar.");

		subMonitor.setWorkRemaining(50);
		doSomethingFor(Duration.ofSeconds(5), subMonitor.split(50));
	}
}
