package org.e792a8.acme.core.workspace.internal;

import java.io.File;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.core.workspace.ITestPointElement;
import org.e792a8.acme.core.workspace.IWorkspaceElement;
import org.eclipse.core.runtime.IPath;

public abstract class TestPointElement implements ITestPointElement {
	private ITestPoint testPoint;
	private String fileName;

	TestPointElement(ITestPoint testPoint, String fileName) {
		this.testPoint = testPoint;
		this.fileName = fileName;
	}

	@Override
	public IProblem getProblem() {
		return getTestPoint().getProblem();
	}

	@Override
	public void delete() {
		getFile().delete();
	}

	@Override
	public boolean isValid() {
		return getFile().isFile();
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
	public ITestPoint getTestPoint() {
		return testPoint;
	}

	@Override
	public int compareTo(IWorkspaceElement arg0) {
		int res = getProblem().compareTo(arg0);
		if (res == 0) {
			res = getFileName().compareTo(((ITestPointElement) arg0).getFileName());
		}
		return res;
	}

}
