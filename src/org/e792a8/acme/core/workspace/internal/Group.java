package org.e792a8.acme.core.workspace.internal;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.core.workspace.IGroup;
import org.eclipse.core.runtime.IPath;

public class Group extends Directory implements IGroup {

	public Group(IPath fullPath, String fileName) {
		super(fullPath, fileName);
	}

	@Override
	public boolean isValid() {
		if (super.isValid() && "group".equals(getJson().type) && getJson().children != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<IDirectory> getSubDirectories() {
		List<IDirectory> ls = new LinkedList<>();
		List<String> sub = getJson().children;
		for (String fn : sub) {
			ls.add(new Directory(getFullPath().append(fn), fn));
		}
		return ls;
	}

	@Override
	public IDirectoryBuilder createSubDirectory() {
		return new DirectoryBuilder(this, true);
	}

}
