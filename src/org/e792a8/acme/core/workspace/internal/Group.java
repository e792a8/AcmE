package org.e792a8.acme.core.workspace.internal;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.core.workspace.IGroup;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Group extends Directory implements IGroup {

	public Group(IPath fullPath) {
		super(fullPath);
	}

	public Group(IGroup parent, IPath fullPath) {
		super(parent, fullPath);
	}

	@Override
	public boolean isValid() {
		if (super.isValid() && "group".equals(ConfigParser.readDoc(getLocation())
			.getDocumentElement().getAttribute("type"))) {
			return true;
		}
		return false;
	}

	@Override
	public List<IDirectory> getSubDirectories() {
		List<IDirectory> ls = new LinkedList<>();
		NodeList nl = ConfigParser.readDoc(getLocation()).getDocumentElement().getElementsByTagName("child");
		for (int i = 0; i < nl.getLength(); ++i) {
			ls.add(new Directory(this, ((Element) nl.item(i)).getAttribute("path")));
		}
		return ls;
	}

	@Override
	public IDirectoryBuilder createSubDirectory() {
		// TODO create subdirectory support
		return null;
	}

}
