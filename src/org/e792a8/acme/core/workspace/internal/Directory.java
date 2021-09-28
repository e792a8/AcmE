package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;

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

	protected DirectoryJson getJson() throws IOException {
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
		DirectoryJson json;
		try {
			json = getJson();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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
	public void delete() throws IOException {
		if (getFullPath().segmentCount() > 0) {
			Group parent = (Group) getParentGroup();
			IPath pp = parent.getFullPath();
			DirectoryJson json = parent.getJson();
			for (String fn : json.children) {
				if (getFileName().equals(fn)) {
					json.children.remove(fn);
					try {
						JsonParser.writeJson(pp, json);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		try {
			return getJson().name;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUrl() {
		try {
			return getJson().url;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isGroup() {
		try {
			return "group".equals(getJson().type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isProblem() {
		try {
			return "problem".equals(getJson().type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
