package org.e792a8.acme.core.workspace.internal;

import java.io.IOException;
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
		try {
			if (super.isValid() && "group".equals(getJson().type) && getJson().children != null) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public List<IDirectory> getSubDirectories() {
		List<IDirectory> ls = new LinkedList<>();
		List<String> sub;
		try {
			sub = getJson().children;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
