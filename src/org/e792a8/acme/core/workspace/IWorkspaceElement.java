package org.e792a8.acme.core.workspace;

import java.io.IOException;

public interface IWorkspaceElement extends Comparable<IWorkspaceElement> {
	void delete() throws IOException;

	boolean isValid();

	IDirectory getDirectory();

}
