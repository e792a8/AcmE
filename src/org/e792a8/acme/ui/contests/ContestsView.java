package org.e792a8.acme.ui.contests;

import java.util.ArrayList;
import java.util.Iterator;

import org.e792a8.acme.control.ContestManager;
import org.e792a8.acme.workspace.DirectoryHandle;
import org.e792a8.acme.workspace.WorkspaceParser;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public class ContestsView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.contests.ContestsView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action addItemAction;
	private Action doubleClickAction;

	class ViewContentProvider implements ITreeContentProvider {
		private IPath invisibleRoot;

		@Override
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			return WorkspaceParser.getParent((IPath) child);
		}

		@Override
		public Object[] getChildren(Object parent) {
			DirectoryHandle handle = ContestManager.readDirectory((IPath) parent);
			ArrayList<IPath> children = new ArrayList<>();
			Iterator<String> itr = handle.children.iterator();
			while (itr.hasNext()) {
				children.add(handle.absPath.append(itr.next()));
			}
			return children.toArray();
		}

		@Override
		public boolean hasChildren(Object parent) {
			DirectoryHandle handle = ContestManager.readDirectory((IPath) parent);
			if ("group".equals(handle.type) && handle.children != null && handle.children.size() > 0)
				return true;
			return false;
		}

		private void initialize() {
			invisibleRoot = ContestManager.getRootPath();
		}
	}

	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object obj) {
			return WorkspaceParser.readDirHandle((IPath) obj).name;
		}

		@Override
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if ("group".equals(WorkspaceParser.readDirHandle((IPath) obj).type))
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ViewLabelProvider());

		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	public void refreshView() {
		viewer.setContentProvider(viewer.getContentProvider());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				ContestsView.this.fillContextMenu(manager);
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
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addItemAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		Action addRootItemAction = new Action() {
			@Override
			public void run() {
				new WizardDialog(null, new NewWizard(ContestManager.getRootPath())).open();
				refreshView();
			}
		};
		addRootItemAction.setText("Add");
		addRootItemAction.setToolTipText("Add a problem / group under root");
		addRootItemAction.setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		manager.add(addRootItemAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		addItemAction = new Action() {
			@Override
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				new WizardDialog(null, new NewWizard(selection)).open();
				refreshView();
			}
		};
		addItemAction.setText("Add");
		addItemAction.setToolTipText("Add a problem / group");
		addItemAction.setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		doubleClickAction = new Action() {
			@Override
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				if (obj == null || !(obj instanceof IPath)) {
					obj = ContestManager.getRootPath();
				}
				ContestManager.openDirectory((IPath) obj);
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(event -> {
			doubleClickAction.run();
		});
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
