package org.eclipsecon.europe2016.progressmonitor.loops;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class LoopSkip3 implements ICoreRunnable {

	private static final List<String> LIST = Arrays.asList("Fox", "Bunny", "Dog", "Cat", "Pig", "Duck"); 
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		List<String> filteredList = LIST.stream().filter((a) -> !shouldSkip(a)).collect(Collectors.toList());
		
		SubMonitor loopMonitor = subMonitor.split(50).setWorkRemaining(filteredList.size());
		for (String animal : filteredList) {
			doSomethingFor(Duration.ofSeconds(3), loopMonitor.split(1));
		}
		
		doSomethingFor(Duration.ofSeconds(2), subMonitor.split(50));
	}

	private boolean shouldSkip(String animal) {
		return animal.startsWith("B") || animal.startsWith("P") || animal.startsWith("C") || animal.startsWith("D");
	}

}
