package org.e792a8.acme.core.workspace;

import java.io.IOException;

public interface IWorkspaceElement {
	void delete() throws IOException;

	boolean isValid();

	IDirectory getDirectory();

	@Override
	boolean equals(Object obj);
}
