package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.IWorkspaceElement;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IRunTestObserver;
import org.eclipse.ui.editors.text.TextEditor;

public class CodeEditor extends TextEditor {
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
	};

	public CodeEditor() {
		super();
		setDocumentProvider(new CodeDocumentProvider());
		setSourceViewerConfiguration(new CodeConfiguration());
		AcmeUI.addRunTestObserver(runTestObserver);
	}

	@Override
	public void dispose() {
		AcmeUI.deleteRunTestObserver(runTestObserver);
		super.dispose();
	}

	@Override
	public void setFocus() {
		AcmeUI.fireOpenDirectory(((CodeEditorInput) getEditorInput()).getSolution().getDirectory());
		super.setFocus();
	}

}
