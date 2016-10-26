package org.eclipsecon.europe2016.progressmonitor.depthfirstsearch;

import java.io.File;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import static org.eclipsecon.europe2016.progressmonitor.Utils.doSomethingFor;

public class FileBrowsing implements ICoreRunnable {

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		File root = new File("/Library/Frameworks");
		SubMonitor subMonitor = SubMonitor.convert(monitor);
		Deque<File> queue = new ArrayDeque<>();
		queue.add(root);
		
		while (!queue.isEmpty()) {
			subMonitor.setWorkRemaining(Math.max(queue.size(), 100));
			File file = queue.removeLast();
			subMonitor.setTaskName("Browsing " + file.getAbsolutePath());
			doSomethingFor(Duration.ofMillis(20), subMonitor.split(1));
			if (file.isDirectory()) {
				queue.addAll(Arrays.asList(file.listFiles()));
			}
		}
	}

}
