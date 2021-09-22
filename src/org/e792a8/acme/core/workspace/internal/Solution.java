package org.e792a8.acme.core.workspace.internal;

import java.io.File;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ISolution;
import org.eclipse.core.runtime.IPath;

public class Solution implements ISolution {
	private IProblem problem;
	private String lang;
	private String fileName;

	public Solution(IProblem problem, String lang, String fileName) {
		this.problem = problem;
		this.lang = lang;
		this.fileName = fileName;
	}

	@Override
	public IProblem getProblem() {
		return problem;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		return getProblem().isValid() && getLocation().toFile().isFile();
	}

	@Override
	public IDirectory getDirectory() {
		return getProblem();
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public IPath getFullPath() {
		return getProblem().getFullPath().append(getFileName());
	}

	@Override
	public IPath getLocation() {
		return getProblem().getLocation().append(getFileName());
	}

	@Override
	public File getFile() {
		return getLocation().toFile();
	}

	@Override
	public String getLang() {
		return lang;
	}

}
