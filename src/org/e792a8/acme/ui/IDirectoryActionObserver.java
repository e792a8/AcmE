package org.e792a8.acme.ui;

import org.e792a8.acme.core.workspace.IDirectory;

public interface IDirectoryActionObserver {
	public void open(IDirectory directory);

	public void close(IDirectory directory);
}
