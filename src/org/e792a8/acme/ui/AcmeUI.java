package org.e792a8.acme.ui;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;

public class AcmeUI {
	static List<IOpenDirectoryObserver> openDirectoryObservers = new LinkedList<>();

	public static void addOpenDirectoryObserver(IOpenDirectoryObserver observer) {
		openDirectoryObservers.add(observer);
	}

	public static void deleteOpenDirectoryObserver(IOpenDirectoryObserver observer) {
		openDirectoryObservers.remove(observer);
	}

	public static void fireOpenDirectory(DirectoryConfig config) {
		for (IOpenDirectoryObserver o : openDirectoryObservers) {
			o.run(config);
		}
	}
}
