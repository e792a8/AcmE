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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Directory implements IDirectory {
	private IPath fullPath;
	private IGroup parent = null;
	private String fileName = null;

	public Directory(IGroup parent, IPath fullPath) {
		this.parent = parent;
		this.fullPath = fullPath;
		this.fileName = fullPath.lastSegment();
	}

	public Directory(IGroup parent, String fileName) {
		this.parent = parent;
		this.fileName = fileName;
		this.fullPath = parent.getFullPath().append(fileName);
	}

	public Directory(IPath fullPath) {
		this.fullPath = fullPath;
		String fn = fullPath.lastSegment();
		this.fileName = (fn == null ? "." : fn);
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
		Document doc = ConfigParser.readDoc(getLocation());
		if (doc != null && "directory".equals(doc.getDocumentElement().getTagName())
			&& ("problem".equals(doc.getDocumentElement().getTagName())
				|| "group".equals(doc.getDocumentElement().getTagName()))) {
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
		if (parent != null) {
			return parent;
		}
		return parent = AcmeWorkspace.getDirectoryByFullPath(getFullPath().removeLastSegments(1)).toGroup();
	}

	@Override
	public void delete() {
		if (getFullPath().segmentCount() > 0) {
			IGroup parent = getParentGroup();
			Document doc = ConfigParser.readDoc(parent.getLocation());
			NodeList nl = doc.getDocumentElement().getElementsByTagName("child");
			for (int i = 0; i < nl.getLength(); ++i) {
				Element e = (Element) nl.item(i);
				if (getFileName().equals(e.getAttribute("path"))) {
					doc.getDocumentElement().removeChild(e);
					break;
				}
			}
			FileSystem.rmDir(getFile());
			ConfigParser.writeDoc(doc, parent.getLocation());
		}
	}

	@Override
	public IDirectory getDirectory() {
		return this;
	}

	@Override
	public String getName() {
		return ConfigParser.getAttribute(getLocation(), "name");
	}

	@Override
	public String getUrl() {
		return ConfigParser.getAttribute(getLocation(), "url");
	}

	@Override
	public boolean isGroup() {
		return "group".equals(ConfigParser.getAttribute(getLocation(), "type"));
	}

	@Override
	public boolean isProblem() {
		return "problem".equals(ConfigParser.getAttribute(getLocation(), "type"));
	}

	@Override
	public IGroup toGroup() {
		if (this instanceof IGroup) {
			return (IGroup) this;
		}
		if (isGroup()) {
			if (parent != null) {
				return new Group(parent, getFullPath());
			} else {
				return new Group(getFullPath());
			}
		}
		return null;
	}

	@Override
	public IProblem toProblem() {
		if (this instanceof IProblem) {
			return (IProblem) this;
		}
		if (isProblem()) {
			if (parent != null) {
				return new Problem(parent, getFullPath());
			} else {
				return new Problem(getFullPath());
			}
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
