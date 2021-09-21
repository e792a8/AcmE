package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class CodeEditorInputFactory implements IElementFactory {

	static final String ID = "org.e792a8.acme.ui.editor.CodeEditorInputFactory";

	@Override
	public IAdaptable createElement(IMemento memento) {
		String dir = memento.getString("DIRPATH");
		String name = memento.getString("FILENAME");
		DirectoryConfig dirConf = WorkspaceManager
			.readDirectory(WorkspaceManager.getRootPath().append(dir));
		if (name.equals(dirConf.solution.path)) {
			return new CodeEditorInput(dirConf.solution);
		}
		return null;
	}

	static void saveState(IMemento memento, SolutionConfig config) {
		String s = config.directory.absPath
			.makeRelativeTo(WorkspaceManager.getRootPath()).toString();
		memento.putString("DIRPATH", s);
		memento.putString("FILENAME", config.path);
	}

}
