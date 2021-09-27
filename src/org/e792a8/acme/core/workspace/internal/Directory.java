package org.e792a8.acme.core.workspace.internal;

import java.io.File;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.utils.FileSystem;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

public class Directory implements IDirectory {
	private IPath fullPath;
	private String fileName = null;

	protected DirectoryJson getJson() {
		return JsonParser.readJson(getFullPath());
	}

	public Directory(IPath fullPath, String fileName) {
		this.fullPath = fullPath;
		this.fileName = fileName;
	}

	@Override
	public String getFileName() {
		if (fileName == null) {
			if (getFullPath().segmentCount() <= 0) {
				return ".";
			}
			return fileName = getFullPath().lastSegment();
		}
		return fileName;
	}

	@Override
	public boolean isValid() {
		DirectoryJson json = getJson();
		if (json != null && ("problem".equals(json.type) || "group".equals(json.type))) {
			return true;
		}
		return false;
	}

	@Override
	public File getFile() {
		return getLocation().toFile();
	}

	@Override
	public IPath getFullPath() {
		return fullPath;
	}

	@Override
	public IPath getLocation() {
		return ResourcesPlugin.getWorkspace().getRoot()
			.getLocation().append(getFullPath());
	}

	@Override
	public IGroup getParentGroup() {
		IPath fpath = getFullPath();
		if (fpath.segmentCount() > 0) {
			return AcmeWorkspace.getDirectoryByFullPath(fpath.removeLastSegments(1)).toGroup();
		}
		return this.toGroup();
	}

	@Override
	public void delete() {
		if (getFullPath().segmentCount() > 0) {
			Group parent = (Group) getParentGroup();
			IPath pp = parent.getFullPath();
			DirectoryJson json = parent.getJson();
			for (String fn : json.children) {
				if (getFileName().equals(fn)) {
					json.children.remove(fn);
					JsonParser.writeJson(pp, json);
					break;
				}
			}
			FileSystem.rmDir(getFile());
		}
	}

	@Override
	public IDirectory getDirectory() {
		return this;
	}

	@Override
	public String getName() {
		return getJson().name;
	}

	@Override
	public String getUrl() {
		return getJson().url;
	}

	@Override
	public boolean isGroup() {
		return "group".equals(getJson().type);
	}

	@Override
	public boolean isProblem() {
		return "problem".equals(getJson().type);
	}

	@Override
	public IGroup toGroup() {
		if (this instanceof IGroup) {
			return (IGroup) this;
		}
		if (isGroup()) {
			return new Group(getFullPath(), getFileName());
		}
		return null;
	}

	@Override
	public IProblem toProblem() {
		if (this instanceof IProblem) {
			return (IProblem) this;
		}
		if (isProblem()) {
			return new Problem(getFullPath(), getFileName());
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof IDirectory) {
			if (getFullPath().equals(((IDirectory) obj).getFullPath())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IDirectoryBuilder modify() {
		// TODO directory modify support
		return null;
	}
}
