package org.e792a8.acme.core.workspace;

public interface IDirectory extends IWorkspaceFile {

	String getName();

	String getUrl();

	IGroup getParentGroup();

	IGroup toGroup();

	IProblem toProblem();

	boolean isProblem();

	boolean isGroup();

	IDirectoryBuilder modify();

}
