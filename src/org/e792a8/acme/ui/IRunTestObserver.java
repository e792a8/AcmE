package org.e792a8.acme.ui;

import org.e792a8.acme.core.workspace.IWorkspaceElement;

public interface IRunTestObserver {
	public void before(IWorkspaceElement config);

	public void after(IWorkspaceElement config);

	public void handleException(Exception e);
}
