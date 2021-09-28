package org.e792a8.acme.core.workspace.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.core.workspace.internal.DirectoryJson.JudgeJson;
import org.e792a8.acme.core.workspace.internal.DirectoryJson.SolutionJson;
import org.eclipse.core.runtime.IPath;

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

	private void setProblemDefault(IPath loc, DirectoryJson json) {
		JudgeJson jj = new JudgeJson();
		jj.type = "strict";
		jj.args = "";
		json.judge = jj;
		SolutionJson sj = new SolutionJson();
		sj.lang = "cpp";
		sj.path = "sol.cpp";
		json.solution = sj;
		try {
			loc.append(sj.path).toFile().createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		json.tests = new ArrayList<>();
	}

	private void setGroupDefault(DirectoryJson json) {
		json.children = new ArrayList<>();
	}

	@Override
	public IDirectory finish() throws IOException {
		if (createSub && directory.isGroup()) {
			IPath dLoc = directory.getLocation();
			IPath newLoc = dLoc.append(fileName);
			IPath newFullPath = directory.getFullPath().append(fileName);
			newLoc.toFile().mkdirs();
			DirectoryJson json = new DirectoryJson();
			json.type = type;
			json.name = name;
			json.url = url;
			if ("problem".equals(type)) {
				setProblemDefault(newLoc, json);
			} else if ("group".equals(type)) {
				setGroupDefault(json);
			}
			JsonParser.writeJson(newFullPath, json);

			json = JsonParser.readJson(directory.getFullPath());
			json.children.add(fileName);
			JsonParser.writeJson(directory.getFullPath(), json);
			return new Directory(newFullPath, fileName);
		} else {
			IPath loc = directory.getLocation();
			IPath fullPath = directory.getFullPath();
			String fname = directory.getFileName();
			DirectoryJson json = null;
			if (name == null) {
				name = directory.getName();
			}
			if (name == null) {
				name = "Default";
			}
			if (url == null) {
				url = directory.getUrl();
			}
			if (url == null) {
				url = "";
			}
			if (type == null) {
				if (directory.isGroup())
					type = "group";
				else if (directory.isProblem())
					type = "problem";
				else
					return null;
				json = JsonParser.readJson(fullPath);
			} else {
				if ("group".equals(type)) {
					if (directory.isGroup()) {
						json = JsonParser.readJson(fullPath);
					} else {
						for (File f : loc.toFile().listFiles()) {
							f.delete();
						}
						json = new DirectoryJson();
						setGroupDefault(json);
					}
				} else if ("problem".equals(type)) {
					if (directory.isProblem()) {
						json = JsonParser.readJson(fullPath);
					} else {
						for (File f : loc.toFile().listFiles()) {
							f.delete();
						}
						json = new DirectoryJson();
						setProblemDefault(loc, json);
					}
				} else {
					return null;
				}
			}
			json.type = type;
			json.name = name;
			json.url = url;
			JsonParser.writeJson(fullPath, json);
			return new Directory(fullPath, fname);
		}
	}

}
