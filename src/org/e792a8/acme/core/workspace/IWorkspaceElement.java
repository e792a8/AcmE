package org.e792a8.acme.core.workspace;

public interface IWorkspaceElement {
	void delete();

	boolean isValid();

	IDirectory getDirectory();

	@Override
	boolean equals(Object obj);
}
