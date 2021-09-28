package org.e792a8.acme.core.workspace;

import java.io.IOException;

import org.e792a8.acme.core.workspace.internal.Directory;
import org.e792a8.acme.core.workspace.internal.RootGroup;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class AcmeWorkspace {
	public static IRootGroup getRootGroup() {
		try {
			return new RootGroup(ResourcesPlugin.getWorkspace().getRoot());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IDirectory getDirectoryByFullPath(IPath fullPath) {
		if (fullPath.segmentCount() <= 0) {
			return getRootGroup();
		}
		return new Directory(fullPath, fullPath.lastSegment());
	}

	public static IDirectory getDirectoryByFullPath(String fullPath) {
		IPath fpath = Path.fromPortableString(fullPath);
		return getDirectoryByFullPath(fpath);
	}

	public static IWorkspaceFile findByFullPath(IPath fullPath) {
		// TODO
		return null;
	}
}
