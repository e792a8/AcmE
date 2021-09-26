package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
			if (contestsView.lastSelectedDirectory.isGroup()) {
				new WizardDialog(null, new NewItemWizard(contestsView.lastSelectedDirectory.toGroup())).open();
				contestsView.refreshView();
			}
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
			new WizardDialog(null, new NewItemWizard(AcmeWorkspace.getRootGroup())).open();
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
			IDirectory dir = contestsView.lastSelectedDirectory;
			AcmeUI.fireOpenDirectory(dir);
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
			AcmeUI.fireCloseDirectory(contestsView.lastSelectedDirectory); // TODO change to close
			contestsView.lastSelectedDirectory.delete();
			contestsView.refreshView();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

}
