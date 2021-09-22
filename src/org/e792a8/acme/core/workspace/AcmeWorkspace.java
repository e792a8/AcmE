package org.e792a8.acme.core.workspace;

import org.e792a8.acme.core.workspace.internal.ConfigParser;
import org.e792a8.acme.core.workspace.internal.Directory;
import org.e792a8.acme.core.workspace.internal.RootGroup;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class AcmeWorkspace {
	public static IRootGroup getRootGroup() {
		ConfigParser.testRoot();
		return new RootGroup(ResourcesPlugin.getWorkspace().getRoot());
	}

	public static IDirectory getDirectoryByFullPath(IPath fullPath) {
		if (fullPath.segmentCount() <= 0) {
			return getRootGroup();
		}
		return new Directory(fullPath);
	}

	public static IDirectory getDirectoryByFullPath(String fullPath) {
		return new Directory(Path.fromPortableString(fullPath));
	}

	public static IWorkspaceFile findByFullPath(IPath fullPath) {
		// TODO
		return null;
	}
}
