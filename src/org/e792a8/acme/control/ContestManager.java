package org.e792a8.acme.control;

import org.e792a8.acme.workspace.DirectoryHandle;
import org.e792a8.acme.workspace.WorkspaceParser;
import org.eclipse.core.runtime.IPath;

public class ContestManager {
	public static DirectoryHandle readDirectoryHandle(IPath path) {
		return WorkspaceParser.readDir(path);
	}

	public static boolean writeDirectoryHandle(DirectoryHandle handle) {
		return WorkspaceParser.writeDir(handle);
	}
}
