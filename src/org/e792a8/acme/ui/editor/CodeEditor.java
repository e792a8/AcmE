package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.IWorkspaceElement;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IRunTestObserver;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.editors.text.TextEditor;

public class CodeEditor extends TextEditor implements IPartListener {
	public static final String ID = "org.e792a8.acme.ui.editor.CodeEditor";
	private IRunTestObserver runTestObserver = new IRunTestObserver() {

		@Override
		public void before(IWorkspaceElement element) {
			IDirectory dir = null;
			if (element instanceof IDirectory) {
				dir = (IDirectory) element;
			} else if (element instanceof ITestPoint) {
				dir = ((ITestPoint) element).getDirectory();
			} else {
				return;
			}
			if (((CodeEditorInput) getEditorInput()).getSolution().getDirectory() == dir) {
				performSave(true, null);
			}
		}

		@Override
		public void after(IWorkspaceElement element) {
		}

		@Override
		public void handleException(Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	};

	public CodeEditor() {
		super();
		setDocumentProvider(new CodeDocumentProvider());
		setSourceViewerConfiguration(new CodeConfiguration());
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		IEditorInput input = getEditorInput();
		if (input instanceof CodeEditorInput) {
			if (((CodeEditorInput) input).getSolution().isValid()) {
				super.doSave(progressMonitor);
			}
		} else {
			super.doSave(progressMonitor);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		AcmeUI.addRunTestObserver(runTestObserver);
		getSite().getPage().addPartListener(this);
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part.equals(this)) {
			AcmeUI.fireOpenDirectory(((CodeEditorInput) getEditorInput()).getSolution().getDirectory());
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part.equals(this)) {
			getSite().getPage().removePartListener(this);
			AcmeUI.deleteRunTestObserver(runTestObserver);
			AcmeUI.fireCloseDirectory(((CodeEditorInput) getEditorInput()).getSolution().getDirectory());
			doSave(null);
		}

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
	}

}
