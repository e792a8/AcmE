package org.e792a8.acme.ui.editor;

import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.ui.editors.text.TextEditor;

public class CodeEditor extends TextEditor {
	public static final String ID = "org.e792a8.acme.ui.editor.CodeEditor";

	public CodeEditor() {
		super();
		setDocumentProvider(new CodeDocumentProvider());
		setSourceViewerConfiguration(new CodeConfiguration());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setFocus() {
		AcmeUI.fireOpenDirectory(((CodeEditorInput) getEditorInput()).solutionConfig.directory);
		super.setFocus();
	}

}
