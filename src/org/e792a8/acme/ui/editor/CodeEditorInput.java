package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.SolutionConfig;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class CodeEditorInput extends FileStoreEditorInput {

	public CodeEditorInput(SolutionConfig config) {
		super(EFS.getLocalFileSystem().getStore(config.directory.absPath.append(config.path)));
		// TODO Auto-generated constructor stub
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

}
