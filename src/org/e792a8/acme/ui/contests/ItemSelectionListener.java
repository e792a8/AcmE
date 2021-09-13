package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
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
			DirectoryConfig handle = WorkspaceManager
				.readDirectory((IPath) ((IStructuredSelection) selection).getFirstElement());
			if (handle != null) {
				contestsView.lastSelectedDirectory = handle.absPath;
			} else {
				contestsView.lastSelectedDirectory = null;
			}
		} else {
			contestsView.lastSelectedDirectory = WorkspaceManager.readRoot().absPath;
		}
	}

}
