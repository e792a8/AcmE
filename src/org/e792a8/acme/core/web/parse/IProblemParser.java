package org.e792a8.acme.core.web.parse;

import java.io.IOException;

import org.e792a8.acme.core.workspace.IProblem;

public interface IProblemParser {
	public IProblem parseTo(IProblem problem) throws IOException;
}
