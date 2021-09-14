package org.e792a8.acme.ui.contests;

import java.io.File;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.e792a8.acme.ui.editor.CodeEditor;
import org.e792a8.acme.ui.editor.CodeEditorInput;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

class Controller {

	ContestsView contestsView;

	Controller(ContestsView view) {
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
			File f = config.absPath.append(config.solution.path).toFile();
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().openEditor(new CodeEditorInput(config.solution), CodeEditor.ID);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void doubleClick(DoubleClickEvent event) {
			run();
		}

	}

}
