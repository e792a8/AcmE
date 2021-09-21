package org.e792a8.acme.ui;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;

public class AcmeUI {
	static List<IOpenDirectoryObserver> openDirectoryObservers = new LinkedList<>();
	static List<IRunTestObserver> runTestObservers = new LinkedList<>();

	public static void addOpenDirectoryObserver(IOpenDirectoryObserver observer) {
		openDirectoryObservers.add(observer);
	}

	public static void deleteOpenDirectoryObserver(IOpenDirectoryObserver observer) {
		openDirectoryObservers.remove(observer);
	}

	public static void fireOpenDirectory(DirectoryConfig config) {
		for (IOpenDirectoryObserver o : openDirectoryObservers) {
			try {
				o.run(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void addRunTestObserver(IRunTestObserver observer) {
		runTestObservers.add(observer);
	}

	public static void deleteRunTestObserver(IRunTestObserver observer) {
		runTestObservers.remove(observer);
	}

	public static void fireBeforeRun(Object config) {
		for (IRunTestObserver o : runTestObservers) {
			try {
				o.before(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void fireAfterRun(Object config) {
		for (IRunTestObserver o : runTestObservers) {
			try {
				o.after(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
