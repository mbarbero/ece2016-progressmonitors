package org.eclipsecon.europe2016.progressmonitor.views;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipsecon.europe2016.progressmonitor.views.DemoRunner.TreeObject;

class OpenSourceFile extends WorkbenchJob {
	private static final String SOURCE_ROOT = "file:/Users/mbarbero/git/ece2016-progressmonitors/org.eclipsecon.europe2016.progressmonitor/src/";
	private final IWorkbenchPage page;
	private final TreeObject treeObject;
	
	OpenSourceFile(TreeObject treeObject, IWorkbenchPage page) {
		super("Open source file in editor");
		this.treeObject = treeObject;
		this.page = page;
	}
	
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			openSourceEditor(monitor, treeObject);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		return Status.OK_STATUS;
	}

	private void openSourceEditor(IProgressMonitor monitor, TreeObject treeObject) throws CoreException, PartInitException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
		IProject dummyProject = getDummyProject(subMonitor.split(1));
		IFile file = getFileLink(dummyProject, treeObject.getRunnable(), subMonitor.split(1));
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		page.openEditor(new FileEditorInput(file), desc.getId());
	}

	private IFile getFileLink(IProject project, Class<? extends ICoreRunnable> clazz, IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
		IFile file = project.getFile(new Path(clazz.getSimpleName() + ".java"));
		file.createLink(URI.create(SOURCE_ROOT + clazz.getName().replace('.', File.separatorChar) + ".java"), IResource.REPLACE, subMonitor.split(1));
		subMonitor.setWorkRemaining(1);
		if (!file.isSynchronized(IResource.DEPTH_INFINITE)) {
			file.refreshLocal(IResource.DEPTH_INFINITE, subMonitor.split(1));
		}
		return file;
	}
	
	private IProject getDummyProject(IProgressMonitor monitor) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("dummyProject");
		SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		if (!project.exists()) {
			try {
				project.create(subMonitor.split(8));
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		
		subMonitor.setWorkRemaining(2);
		
		if (!project.isOpen()) {
			try {
				project.open(subMonitor.split(2));
			} catch (OperationCanceledException | CoreException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			project.setHidden(false);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		
		return project;
	}
}
