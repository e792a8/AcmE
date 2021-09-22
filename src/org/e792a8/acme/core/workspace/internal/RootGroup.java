package org.e792a8.acme.core.workspace.internal;

import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IRootGroup;
import org.eclipse.core.resources.IWorkspaceRoot;

public class RootGroup extends Group implements IRootGroup {

	public RootGroup(IWorkspaceRoot wsRoot) {
		super(wsRoot.getFullPath());
	}

	@Override
	public String getName() {
		return "ROOT";
	}

	@Override
	public IGroup getParentGroup() {
		return this;
	}

	@Override
	public String getFileName() {
		return ".";
	}

}
