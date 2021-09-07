package org.e792a8.acme.core.workspace;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public class DirectoryConfig {
	public IPath absPath;
	public String type;
	public String name;
	public String url;
	public List<String> children;
	public List<SolutionConfig> solutions;
	public List<TestPointConfig> testPoints;
}
