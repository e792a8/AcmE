package org.e792a8.acme.core.workspace;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public interface IWorkspaceFile extends IWorkspaceElement {
	String getFileName();

	IPath getFullPath();

	IPath getLocation();

	File getFile();
}
