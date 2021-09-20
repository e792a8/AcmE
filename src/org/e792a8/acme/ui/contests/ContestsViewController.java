package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class ContestsViewController {

	ContestsView contestsView;

	ContestsViewController(ContestsView view) {
		contestsView = view;
	}

	class AddItemAction extends Action implements Listener {

		AddItemAction() {
			setText("Add");
			setToolTipText("Add a problem / group");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}

		@Override
		public void run() {
			IStructuredSelection selection = contestsView.viewer.getStructuredSelection();
			new WizardDialog(null, new NewItemWizard(contestsView.lastSelectedDirectory)).open();
			contestsView.refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class AddRootItemAction extends Action implements Listener {
		AddRootItemAction() {
			setText("Add");
			setToolTipText("Add a problem / group under root");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		}

		@Override
		public void run() {
			new WizardDialog(null, new NewItemWizard(WorkspaceManager.readRoot().absPath)).open();
			contestsView.refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class DoubleClickAction extends Action implements IDoubleClickListener {

		@Override
		public void run() {
			DirectoryConfig config = WorkspaceManager.readDirectory(contestsView.lastSelectedDirectory);
			if (!"problem".equals(config.type)) {
				return;
			}
			AcmeUI.fireOpenDirectory(config);
		}

		@Override
		public void doubleClick(DoubleClickEvent event) {
			run();
		}

	}

	class DeleteItemAction extends Action implements Listener {

		public DeleteItemAction() {
			setText("Delete");
			setToolTipText("Delete this item and everything inside");
			setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		}

		@Override
		public void run() {
			AcmeUI.fireOpenDirectory(null);
			WorkspaceManager.deleteDirectory(contestsView.lastSelectedDirectory);
			contestsView.refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

}
