package org.e792a8.acme.ui.editor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.e792a8.acme.utils.FileSystem;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class CodeDocumentProvider extends FileDocumentProvider {
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		if (element instanceof CodeEditorInput) {
			CodeEditorInput input = (CodeEditorInput) element;
			IDocument document = createEmptyDocument();
			try {
				setDocumentContent(document, new FileInputStream(input.getFile()), "UTF-8");
			} catch (FileNotFoundException | CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return document;
		}
		return null;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
		throws CoreException {
		if (element instanceof CodeEditorInput) {
			CodeEditorInput input = (CodeEditorInput) element;
			FileSystem.write(input.getFile(), document.get());
		} else {
			super.doSaveDocument(monitor, element, document, overwrite);
		}
	}

	@Override
	public boolean isReadOnly(Object element) {
		if (element instanceof CodeEditorInput) {
			CodeEditorInput input = (CodeEditorInput) element;
			return !input.getFile().canWrite();
		}
		return super.isReadOnly(element);
	}

	@Override
	public boolean isModifiable(Object element) {
		if (element instanceof CodeEditorInput) {
			return !isReadOnly(element);
		}
		return super.isModifiable(element);
	}
}
