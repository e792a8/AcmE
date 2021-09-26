package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
	private IDirectory lastSelectedDirectory;

	private DeleteItemAction deleteItemAction = new DeleteItemAction();
	private AddItemAction addItemAction = new AddItemAction();
	private AddRootItemAction addRootItemAction = new AddRootItemAction();
	private DoubleClickAction doubleClickAction = new DoubleClickAction();
	private ItemSelectionListener itemSelectionListener = new ItemSelectionListener();
	private DirectoryActionObserver directoryActionObserver = new DirectoryActionObserver();

	private class DirectoryActionObserver implements IDirectoryActionObserver {

		@Override
		public void open(IDirectory directory) {
			refreshView();
		}

		@Override
		public void close(IDirectory directory) {
			refreshView();
		}

	}

	private class AddItemAction extends Action implements Listener {

		AddItemAction() {
			setText("Add");
			setToolTipText("Add a problem / group");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}

		@Override
		public void run() {
			if (lastSelectedDirectory.isGroup()) {
				new WizardDialog(null, new NewItemWizard(lastSelectedDirectory.toGroup())).open();
				refreshView();
			}
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	private class AddRootItemAction extends Action implements Listener {
		AddRootItemAction() {
			setText("Add");
			setToolTipText("Add a problem / group under root");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}

		@Override
		public void run() {
			new WizardDialog(null, new NewItemWizard(AcmeWorkspace.getRootGroup())).open();
			refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	private class DoubleClickAction extends Action implements IDoubleClickListener {

		@Override
		public void run() {
			IDirectory dir = lastSelectedDirectory;
			AcmeUI.fireOpenDirectory(dir);
		}

		@Override
		public void doubleClick(DoubleClickEvent event) {
			run();
		}

	}

	private class DeleteItemAction extends Action implements Listener {

		public DeleteItemAction() {
			setText("Delete");
			setToolTipText("Delete this item and everything inside");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		}

		@Override
		public void run() {
			AcmeUI.fireCloseDirectory(lastSelectedDirectory); // TODO change to close
			lastSelectedDirectory.delete();
			refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

	private class ItemSelectionListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
				IDirectory dir = (IDirectory) ((IStructuredSelection) selection).getFirstElement();
				lastSelectedDirectory = dir;
			} else {
				lastSelectedDirectory = AcmeWorkspace.getRootGroup();
			}
		}

	}

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

		AcmeUI.addDirectoryActionObserver(directoryActionObserver);
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
		manager.add(deleteItemAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addRootItemAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void hookClickEvents() {
		viewer.addPostSelectionChangedListener(itemSelectionListener);
		viewer.addDoubleClickListener(doubleClickAction);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		AcmeUI.deleteDirectoryActionObserver(directoryActionObserver);
		super.dispose();
	}
}
