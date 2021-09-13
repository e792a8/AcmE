package org.e792a8.acme.ui.contests;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public class ContestsView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.contests.ContestsView";

	TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	IPath lastSelectedDirectory;
	private final Controller controller = new Controller(this);

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);

		viewer.setContentProvider(new ContestsViewContentProvider(this));
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ContestsViewLabelProvider(this));

		getSite().setSelectionProvider(viewer);
		hookContextMenu();
		hookClickEvents();
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
		manager.add(controller.new AddItemAction());
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(controller.new AddRootItemAction());
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void hookClickEvents() {
		viewer.addPostSelectionChangedListener(new ItemSelectionListener(this));
		viewer.addDoubleClickListener(controller.new DoubleClickAction());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
