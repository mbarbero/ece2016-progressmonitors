package org.eclipsecon.europe2016.progressmonitor.views;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipsecon.europe2016.progressmonitor.bad.IgnoreCancelation;
import org.eclipsecon.europe2016.progressmonitor.bad.IgnoreIt;
import org.eclipsecon.europe2016.progressmonitor.bad.StucksAtBeginning;
import org.eclipsecon.europe2016.progressmonitor.bad.StucksAtEnd;
import org.eclipsecon.europe2016.progressmonitor.branches.BadBranching;
import org.eclipsecon.europe2016.progressmonitor.branches.ComplexIf;
import org.eclipsecon.europe2016.progressmonitor.branches.ComplexIf2;
import org.eclipsecon.europe2016.progressmonitor.branches.SimpleIf;
import org.eclipsecon.europe2016.progressmonitor.cancellation.CheckCancelMarsAndBefore;
import org.eclipsecon.europe2016.progressmonitor.cancellation.CheckCancelNeonAndLater;
import org.eclipsecon.europe2016.progressmonitor.cancellation.ForceCheckCancelOxygenAndLater;
import org.eclipsecon.europe2016.progressmonitor.depthfirstsearch.FileBrowsing;
import org.eclipsecon.europe2016.progressmonitor.loops.GoodLoophole;
import org.eclipsecon.europe2016.progressmonitor.loops.LoopSkip1;
import org.eclipsecon.europe2016.progressmonitor.loops.LoopSkip2;
import org.eclipsecon.europe2016.progressmonitor.loops.LoopSkip3;
import org.eclipsecon.europe2016.progressmonitor.loops.NestedLoops;
import org.eclipsecon.europe2016.progressmonitor.loops.SimpleLoop;
import org.eclipsecon.europe2016.progressmonitor.loops.UnboundedLoop;
import org.eclipsecon.europe2016.progressmonitor.loops.WrongLoophole;
import org.eclipsecon.europe2016.progressmonitor.simple.SetTaskName;
import org.eclipsecon.europe2016.progressmonitor.simple.SetWorkRemaining;
import org.eclipsecon.europe2016.progressmonitor.simple.SimpleMarsAndBefore;
import org.eclipsecon.europe2016.progressmonitor.simple.SimpleNeonAndLater;
import org.eclipsecon.europe2016.progressmonitor.simple.SplitNewChildSavior;
import org.osgi.framework.Bundle;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class DemoRunner extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipsecon.europe2016.progressmonitor.views.DemoRunner";

	private TreeViewer viewer;
	private Action debug;
	private Action run;
	private Action showSource;
	

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		private final Class<? extends ICoreRunnable> runnable;

		public TreeObject(String name, Class<? extends ICoreRunnable> runnable) {
			this.name = name;
			this.runnable = runnable;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}
		
		public Class<? extends ICoreRunnable> getRunnable() {
			return runnable;
		}

		public String toString() {
			return getName();
		}

		public <T> T getAdapter(Class<T> key) {
			return null;
		}
		
		public void scheduleJob() {
			try {
				Job job = Job.create(name, runnable.newInstance());
				job.schedule(500);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList<TreeObject> children;

		public TreeParent(String name) {
			super(name, null);
			children = new ArrayList<>();
		}

		public TreeParent addChild(TreeParent child) {
			return (TreeParent) addChild((TreeObject)child);
		}
		
		public TreeObject addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
			return child;
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements ITreeContentProvider {
		private TreeParent invisibleRoot;

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() {
			invisibleRoot = new TreeParent("Root");
			
			TreeParent p1 = invisibleRoot.addChild(new TreeParent("Bad usage"));
			p1.addChild(new TreeObject("Ignore it", IgnoreIt.class));
			p1.addChild(new TreeObject("Fast at beginning", StucksAtEnd.class));
			p1.addChild(new TreeObject("Fast at end", StucksAtBeginning.class));
			p1.addChild(new TreeObject("Ignore Cancelation", IgnoreCancelation.class));

			TreeParent p2 = invisibleRoot.addChild(new TreeParent("Simple cases"));
			p2.addChild(new TreeObject("Basic (Eclipse Mars/4.5-)", SimpleMarsAndBefore.class));
			p2.addChild(new TreeObject("Basic (Eclipse Neon/4.6+)", SimpleNeonAndLater.class));
			p2.addChild(new TreeObject("Protect you from bad code", SplitNewChildSavior.class));
			p2.addChild(new TreeObject("Set work remaining", SetWorkRemaining.class));
			p2.addChild(new TreeObject("Set task name", SetTaskName.class));
			
			TreeParent p3 = invisibleRoot.addChild(new TreeParent("Check for Cancelation"));
			p3.addChild(new TreeObject("Check Cancelation (Eclipse Mars/4.5-)", CheckCancelMarsAndBefore.class));
			p3.addChild(new TreeObject("Check Cancelation (Eclipse Neon/4.6+)", CheckCancelNeonAndLater.class));
			p3.addChild(new TreeObject("Force Check Cancelation (Eclipse Oxygen/4.7+)", ForceCheckCancelOxygenAndLater.class));

			TreeParent p4 = invisibleRoot.addChild(new TreeParent("Branches"));
			p4.addChild(new TreeObject("Simple branching", SimpleIf.class));
			p4.addChild(new TreeObject("Anti-pattern", BadBranching.class));
			p4.addChild(new TreeObject("Complex branching", ComplexIf.class));
			p4.addChild(new TreeObject("Complex branching (2)", ComplexIf2.class));
			
			TreeParent p5 = invisibleRoot.addChild(new TreeParent("Loops"));
			p5.addChild(new TreeObject("Simple loop", SimpleLoop.class));
			p5.addChild(new TreeObject("Nested loops", NestedLoops.class));
			p5.addChild(new TreeObject("Skipping loop elements", LoopSkip1.class));
			p5.addChild(new TreeObject("Wrong skipping many loop elements", LoopSkip2.class));
			p5.addChild(new TreeObject("Good skipping many loop elements", LoopSkip3.class));
			p5.addChild(new TreeObject("Unbounded loop", UnboundedLoop.class));
			p5.addChild(new TreeObject("Wrong breaking loop", WrongLoophole.class));
			p5.addChild(new TreeObject("Good breaking loop", GoodLoophole.class));
			
			TreeParent p6 = invisibleRoot.addChild(new TreeParent("Ever growing task"));
			p6.addChild(new TreeObject("File browsing", FileBrowsing.class));
		}
	}

	class ViewLabelProvider extends LabelProvider {

		private Image javaFileImage;

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			if (javaFileImage == null) {
				javaFileImage = getImageDescriptor("org.eclipse.jdt.ui", "icons/full/obj16/jcu_obj.png").createImage();
			}
			Image image = javaFileImage;
			if (obj instanceof TreeParent) {
				image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			return image;
		}
		
		@Override
		public void dispose() {
			super.dispose();
			javaFileImage.dispose();
		}
	}

	/**
	 * The constructor.
	 */
	public DemoRunner() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ViewLabelProvider());
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				DemoRunner.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(debug);
		manager.add(new Separator());
		manager.add(run);
		manager.add(new Separator());
		manager.add(showSource);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(debug);
		manager.add(run);
		manager.add(showSource);
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(debug);
		manager.add(run);
		manager.add(showSource);
		manager.add(new Separator());
	}

	private void makeActions() {
		debug = new Action() {
			public void run() {
				showSource.run();
				run.run();
			}
		};
		debug.setText("Start Example Job and show source code");
		debug.setToolTipText("Start Example Job and show source code");
		debug.setImageDescriptor(getImageDescriptor("org.eclipse.debug.ui", "icons/full/etool16/debug_exc.gif"));
		
		run = new Action() {
			public void run() {
				Object selection = viewer.getStructuredSelection().getFirstElement();
				if (selection instanceof TreeObject && !(selection instanceof TreeParent)) {
					TreeObject treeObject = (TreeObject) selection;
					treeObject.scheduleJob();
					try {
						getSite().getPage().showView("org.eclipse.ui.views.ProgressView");
					} catch (PartInitException e) {
						throw new RuntimeException(e);
					}
				}
			}
		};
		run.setText("Start Example Job");
		run.setToolTipText("Start Example Job");
		run.setImageDescriptor(getImageDescriptor("org.eclipse.debug.ui", "icons/full/etool16/run_exc.gif"));

		showSource = new Action() {
			public void run() {
				Object selection = viewer.getStructuredSelection().getFirstElement();
				if (selection instanceof TreeObject && !(selection instanceof TreeParent)) {
					TreeObject treeObject = (TreeObject) selection;
					WorkbenchJob wj = new OpenSourceFile(treeObject, getSite().getPage());
					wj.schedule();
				}
			}
		};
		showSource.setText("Show source code");
		showSource.setToolTipText("Show source code");
		showSource.setImageDescriptor(getImageDescriptor("org.eclipse.search", "icons/full/elcl16/search_goto.png"));
	}

	private static ImageDescriptor getImageDescriptor(String name, String path) {
		Bundle bundle = Platform.getBundle(name);
		if (path != null) {
			URL iconURL = FileLocator.find(bundle, new Path(path), null);
			if (iconURL != null) {
				return ImageDescriptor.createFromURL(iconURL);
			}
		}
		return null;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				debug.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
