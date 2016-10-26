package org.eclipsecon.europe2016.progressmonitor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class Utils {

	private static final Duration REFRESH_RATE = Duration.ofMillis(16);

	public static void doSomethingFor(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static void doSomethingFor1Sec() {
		doSomethingFor(Duration.ofSeconds(1));
	}
	
	public static void doSomethingFor1Sec(IProgressMonitor monitor) {
		doSomethingFor(Duration.ofSeconds(1), monitor);
	}
	
	public static void doSomethingFor(Duration duration, IProgressMonitor monitor) {
		if (duration.compareTo(REFRESH_RATE) > 0) {
			long iter = duration.toMillis() / 16;
			SubMonitor subMonitor = SubMonitor.convert(monitor, (int) iter);
			for(int i =0; i < iter; i++) {
				doSomethingFor(REFRESH_RATE);
				subMonitor.split(1);
			}
		} else {
			doSomethingFor(duration);
		}
	}
	
	public static void workSomethingFor(Duration duration, IProgressMonitor monitor) {
		if (duration.compareTo(REFRESH_RATE) > 0) {
			long iter = duration.toMillis() / 16;
			SubMonitor subMonitor = SubMonitor.convert(monitor, (int) iter);
			for(int i =0; i < iter; i++) {
				doSomethingFor(REFRESH_RATE);
				subMonitor.worked(1);
			}
		} else {
			doSomethingFor(duration);
		}
	}
	
	public static void showMessage(String title, String msg) {
		PlatformUI.getWorkbench().getDisplay().syncExec(() -> {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, msg);
		});
	}
	
	public static Iterable<String> streamOfString() {
		List<String> ret = new ArrayList<>();
		for (int i =0; i < 250; i++) {
			ret.add("aString"+i);
		}
		return ret;
	}
}
