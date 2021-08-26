package org.e792a8.acme.workspace;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public class DirectoryHandle {
	public IPath absPath;
	public String type;
	public String name;
	public String url;
	public List<String> children;
	public List<SolutionHandle> solutions;
	public List<TestPointHandle> testPoints;
}
