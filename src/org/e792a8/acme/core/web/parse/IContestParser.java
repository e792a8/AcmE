package org.e792a8.acme.core.web.parse;

import java.io.IOException;

import org.e792a8.acme.core.workspace.IGroup;

public interface IContestParser {
	public IGroup parseTo(IGroup group) throws IOException;
}
