package org.e792a8.acme.core.workspace.internal;

import java.io.IOException;
import java.util.ArrayList;

import org.e792a8.acme.core.workspace.IGroup;
import org.e792a8.acme.core.workspace.IRootGroup;
import org.eclipse.core.resources.IWorkspaceRoot;

public class RootGroup extends Group implements IRootGroup {

	public RootGroup(IWorkspaceRoot wsRoot) throws IOException {
		super(wsRoot.getFullPath(), ".");
		testRoot();
	}

	private void testRoot() throws IOException {
		if (!isValid()) {
			DirectoryJson json = new DirectoryJson();
			json.type = "group";
			json.name = "ROOT";
			json.url = "";
			json.children = new ArrayList<>();
			JsonParser.writeJson(getFullPath(), json);
		}
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
