package org.e792a8.acme.ui;

import org.e792a8.acme.core.workspace.DirectoryConfig;

public interface IDirectoryActionObserver {
	public void open(DirectoryConfig directory);

	public void close(DirectoryConfig directory);
}
