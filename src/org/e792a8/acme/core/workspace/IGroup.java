package org.e792a8.acme.core.workspace;

import java.util.List;

public interface IGroup extends IDirectory {
	List<IDirectory> getSubDirectories();

	IDirectoryBuilder createSubDirectory();
}
