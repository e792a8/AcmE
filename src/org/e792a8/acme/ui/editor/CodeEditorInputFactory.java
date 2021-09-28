package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.ISolution;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class CodeEditorInputFactory implements IElementFactory {

	static final String ID = "org.e792a8.acme.ui.editor.CodeEditorInputFactory";

	@Override
	public IAdaptable createElement(IMemento memento) {
		String dir = memento.getString("DIRPATH");
		IDirectory directory = AcmeWorkspace.getDirectoryByFullPath(dir);
		if (directory != null && directory.isProblem()) {
			return new CodeEditorInput(directory.toProblem().getSolution());
		}
		return null;
	}

	static void saveState(IMemento memento, ISolution config) {
		if (!config.isValid()) {
			return;
		}
		String s = config.getProblem().getFullPath().toPortableString();
		memento.putString("DIRPATH", s);
		memento.putString("FILENAME", config.getFileName());
	}

}
