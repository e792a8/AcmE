package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class AddItemAction extends Action {

	ContestsView contestsView;

	AddItemAction(ContestsView view) {
		contestsView = view;
		setText("Add");
		setToolTipText("Add a problem / group");
		setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	@Override
	public void run() {
		IStructuredSelection selection = contestsView.viewer.getStructuredSelection();
		new WizardDialog(null, new NewWizard(selection)).open();
		contestsView.refreshView();
	}
}

class AddRootItemAction extends Action {
	ContestsView contestsView;

	AddRootItemAction(ContestsView view) {
		contestsView = view;
		setText("Add");
		setToolTipText("Add a problem / group under root");
		setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	@Override
	public void run() {
		new WizardDialog(null, new NewWizard(WorkspaceManager.readRoot().absPath)).open();
		contestsView.refreshView();
	}
}

class DoubleClickAction extends Action {
	ContestsView contestsView;

	DoubleClickAction(ContestsView view) {
		contestsView = view;
	}

	@Override
	public void run() {
		// TODO
	}
}
