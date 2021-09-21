package org.e792a8.acme.core.workspace;

public interface IDirectory extends IWorkspaceFile {

	String getName();

	String getUrl();

	IDirectory getParentDirectory();

}
