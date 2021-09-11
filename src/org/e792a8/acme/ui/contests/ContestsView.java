package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
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
	private IPath lastSelectedDirectory;
	private final ISelectionListener selectionChangedListener = (part, selection) -> {
		if (part == ContestsView.this) {
			if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
				DirectoryConfig handle = WorkspaceManager
					.readDirectory((IPath) ((IStructuredSelection) selection).getFirstElement());
				if (handle != null) {
					lastSelectedDirectory = handle.absPath;
				} else {
					lastSelectedDirectory = null;
				}
			} else {
				lastSelectedDirectory = WorkspaceManager.readRoot().absPath;
			}
			return;
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);

		viewer.setContentProvider(new ContestsViewContentProvider(this));
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ContestsViewLabelProvider(this));

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
		fillLocalToolBar(bars.getToolBarManager());
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
				new WizardDialog(null, new NewWizard(WorkspaceManager.readRoot().absPath)).open();
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
				// TODO
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
