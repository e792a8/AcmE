package org.e792a8.acme.ui;

import org.e792a8.acme.core.workspace.IWorkspaceElement;

public interface IRunTestObserver {
	void before(IWorkspaceElement config);

	void after(IWorkspaceElement config);
}
