package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DirectoryBuilder implements IDirectoryBuilder {
	private IDirectory directory;
	private boolean createSub;
	private String type = null;
	private String name = null;
	private String url = null;
	private String fileName = null;

	public DirectoryBuilder(IDirectory directory, boolean createSub) {
		this.directory = directory;
		this.createSub = createSub;
	}

	@Override
	public IDirectoryBuilder setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public IDirectoryBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IDirectoryBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public IDirectoryBuilder setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	@Override
	public IDirectory finish() {
		if (createSub && directory.isGroup()) {
			IPath dLoc = directory.getLocation();
			IPath newLoc = dLoc.append(fileName);
			newLoc.toFile().mkdirs();
			Document doc = ConfigParser.newDocument();
			Element elem = doc.createElement("directory");
			elem.setAttribute("type", type);
			elem.setAttribute("name", name);
			elem.setAttribute("url", url);
			doc.appendChild(elem);
			if ("problem".equals(type)) {
				Element e = doc.createElement("judge");
				e.setAttribute("type", "strict");
				elem.appendChild(e);
				e = doc.createElement("solution");
				e.setAttribute("lang", "cpp");
				e.setAttribute("path", "sol.cpp");
				try {
					newLoc.append("sol.cpp").toFile().createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				elem.appendChild(e);
			}
			ConfigParser.writeDoc(doc, newLoc);

			doc = ConfigParser.readDoc(dLoc);
			elem = doc.createElement("child");
			elem.setAttribute("path", fileName);
			doc.getDocumentElement().appendChild(elem);
			ConfigParser.writeDoc(doc, dLoc);
			return new Directory(newLoc);
		} else {
			IPath loc = directory.getLocation();
			Document doc = null;
			Element elem = null;
			if (name == null) {
				name = directory.getName();
			}
			if (url == null) {
				url = directory.getUrl();
			}
			if (type == null) {
				if (directory.isGroup())
					type = "group";
				else if (directory.isProblem())
					type = "problem";
				else
					return null;
				doc = ConfigParser.readDoc(directory.getLocation());
				elem = doc.getDocumentElement();
			} else {
				if ("group".equals(type)) {
					if (directory.isGroup()) {
						doc = ConfigParser.readDoc(directory.getLocation());
						elem = doc.getDocumentElement();
					} else {
						doc = ConfigParser.newDocument();
						elem = doc.createElement("directory");
						doc.appendChild(elem);
						for (File f : loc.toFile().listFiles()) {
							f.delete();
						}
					}
				} else if ("problem".equals(type)) {
					if (directory.isProblem()) {
						doc = ConfigParser.readDoc(loc);
						elem = doc.getDocumentElement();
					} else {
						doc = ConfigParser.newDocument();
						elem = doc.createElement("directory");
						doc.appendChild(elem);
						for (File f : loc.toFile().listFiles()) {
							f.delete();
						}
					}
				} else {
					return null;
				}
			}
			elem.setAttribute("type", type);
			elem.setAttribute("name", name);
			elem.setAttribute("url", url);
			ConfigParser.writeDoc(doc, loc);
			return new Directory(loc);
		}
	}

}
