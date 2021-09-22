package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

class ItemSelectionListener implements ISelectionChangedListener {

	ContestsView contestsView;

	ItemSelectionListener(ContestsView view) {
		contestsView = view;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			IDirectory dir = (IDirectory) ((IStructuredSelection) selection).getFirstElement();
			contestsView.lastSelectedDirectory = dir;
		} else {
			contestsView.lastSelectedDirectory = AcmeWorkspace.getRootGroup();
		}
	}

}
