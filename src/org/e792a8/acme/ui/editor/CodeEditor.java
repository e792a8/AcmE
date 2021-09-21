package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IRunTestObserver;
import org.eclipse.ui.editors.text.TextEditor;

public class CodeEditor extends TextEditor {
	public static final String ID = "org.e792a8.acme.ui.editor.CodeEditor";
	private IRunTestObserver runTestObserver = new IRunTestObserver() {

		@Override
		public void before(Object config) {
			DirectoryConfig dir = null;
			if (config instanceof DirectoryConfig) {
				dir = (DirectoryConfig) config;
			} else if (config instanceof TestPointConfig) {
				dir = ((TestPointConfig) config).directory;
			} else {
				return;
			}
			if (((CodeEditorInput) getEditorInput()).solutionConfig.directory == dir) {
				performSave(true, null);
			}
		}

		@Override
		public void after(Object config) {
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
		AcmeUI.fireOpenDirectory(((CodeEditorInput) getEditorInput()).solutionConfig.directory);
		super.setFocus();
	}

}
